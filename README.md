# CWS – Car WorkShop (implementación JDBC)

Aplicación de gestión de un taller mecánico. Este módulo, `cws_services_jdbc`, implementa las
**interfaces de servicio** que se dan en `cws_service_interfaces`, accediendo a una base de datos
HSQLDB mediante **JDBC**. Además incluye un cliente de consola propio (paquete `ui`) que usa esos
servicios.

Casos de uso implementados:

| Actor | Caso de uso | Servicio |
|---|---|---|
| Gerente (manager) | CRUD de mecánicos | `MechanicCrudService` |
| Cajero (cashier) | Facturar órdenes de trabajo | `InvoicingService.create` |
| Cajero | Buscar órdenes no facturadas de un cliente | `InvoicingService.findNotInvoicedWorkOrdersByClientNif` |
| Jefe de taller (foreman) | CRUD de clientes, listar recomendados | `ClientCrudService` |
| Administrador | Generar bonos de 20 € por cada 3 órdenes de trabajo pagadas | `VoucherGenerationService.generateVouchers` |
| Administrador | Listar los bonos de un cliente | `VoucherGenerationService.findVouchersByClientNif` |

El resto de métodos de las interfaces lanzan `NotYetImplementedException`.

---

## 1. El proyecto completo

El proyecto Maven `cws2026` tiene varios módulos. Solo `cws_services_jdbc` es código propio; el resto
viene dado y **no se modifica**:

| Módulo | Contenido |
|---|---|
| `cws_util` | Utilidades: `ArgumentChecks`, `BusinessChecks`, `UserInteractionChecks`, excepciones, `Console`, `BaseMenu`, `Action`… |
| `cws_service_interfaces` | Interfaces de servicio (las fachadas) y sus DTOs. Son el contrato: no se tocan. |
| **`cws_services_jdbc`** | **Este módulo**: implementación de los servicios, la persistencia y la UI propia. |
| `cws_service_tests` | Tests de aceptación (Cucumber) y de robustez (JUnit). |
| `cws_client_*` | Clientes de consola oficiales (manager, cashier, foreman, mechanic). Usan este módulo a través de `Factories`. |
| `cws_database` | Base de datos HSQLDB (`test.script`). |

La conexión se configura en `src/connection.properties`. La base de datos se abre en modo fichero de
solo lectura (`files_readonly=true`): los cambios se hacen en memoria y se pierden al cerrar la
aplicación.

---

## 2. Arquitectura: patrón Capas (Layers)

El código está dividido en tres capas. Cada una tiene una responsabilidad y **solo depende de la
capa inmediatamente inferior**:

```
 ui            Interacción con el usuario: pedir datos, validar formato, mostrar resultados
  │  (llama a las fachadas a través de Factories.service)
  ▼
 service       Lógica de negocio: reglas, comprobaciones, cálculos, transacciones
  │  (llama a los gateways a través de Factories.persistence)
  ▼
 persistence   Acceso a datos: guardar y recuperar filas con JDBC
  │
  ▼
 HSQLDB
```

Paquetes (`src/uo/ri/cws/application/`):

```
ui/
  manager/   (mechanic/, voucher/)      cashier/      foreman/ (client/, reception/, vehicle/)
  mechanic/  util/ (Printer, InputReader)
service/
  ServiceFactoryImpl
  mechanic/crud/          MechanicCrudServiceImpl, MechanicAssembler, commands/
  invoice/create/         InvoicingServiceImpl, InvoiceAssembler, commands/
  client/crud/            ClientCrudServiceImpl, ClientAssembler, commands/
  paymentmean/voucher/    VoucherGenerationServiceImpl, VoucherAssembler, commands/
  paymentmean/crud/       PaymentMeanCrudServiceImpl (sin implementar)
  util/                   NifValidator
persistence/
  Gateway, PersistenceFactory, PersistenceFactoryImpl
  mechanic/ invoice/ workorder/ intervention/ client/ recommendation/
  paymentmean/ cash/ creditcard/ voucher/ vehicle/      (cada uno con su impl/)
  util/  assembler/BaseRecordAssembler   command/Command, CommandExecutor   executor/Jdbc
```

Qué **no** puede pasar (y no pasa en este código):
- La UI no tiene SQL, ni JDBC, ni conoce los gateways ni las clases `...Impl`.
- La capa de servicio no tiene SQL ni JDBC, y nunca escribe en consola.
- La persistencia no tiene reglas de negocio ni lanza `BusinessException`.
- Un servicio nunca usa otro servicio (cada uno abriría su propia conexión y se rompería la transacción):
  si un command necesita datos de otra entidad, usa directamente el **gateway** de esa tabla.

---

## 3. Patrón Fachada (Facade)

Cada subsistema ofrece un único punto de entrada: una **interfaz de servicio** (dada) y su
implementación (propia), que se llama igual que la interfaz con el sufijo `Impl`.

| Interfaz (cws_service_interfaces) | Implementación (este módulo) |
|---|---|
| `MechanicCrudService` | `service/mechanic/crud/MechanicCrudServiceImpl` |
| `InvoicingService` | `service/invoice/create/InvoicingServiceImpl` |
| `ClientCrudService` | `service/client/crud/ClientCrudServiceImpl` |
| `VoucherGenerationService` | `service/paymentmean/voucher/VoucherGenerationServiceImpl` |

La fachada **no tiene lógica**: cada método solo crea el command adecuado y lo ejecuta. Por ejemplo,
en `ClientCrudServiceImpl`:

```java
public ClientDto addClient(ClientDto client, String recommenderNif) throws BusinessException {
    return executor.execute(new AddClient(client, recommenderNif));
}
```

La UI solo conoce la interfaz (`ClientCrudService`), nunca `ClientCrudServiceImpl`.

---

## 4. Patrón Factoría Simple (Simple Factory)

La creación de objetos se concentra en factorías para que nadie dependa de las clases concretas.

- **`ServiceFactoryImpl`** (implementa `ServiceFactory`, dada): devuelve las fachadas.
  `forClientCrudService()` devuelve un `new ClientCrudServiceImpl()`. Los servicios que no se
  implementan lanzan `NotYetImplementedException`.
- **`PersistenceFactoryImpl`** (implementa `PersistenceFactory`): devuelve los gateways.
  `forClient()` devuelve un `new ClientGatewayImpl()`.
- **`uo.ri.conf.Factories`**: centraliza las dos factorías en atributos estáticos.

```java
public static ServiceFactory service = new ServiceFactoryImpl();
public static PersistenceFactory persistence = new PersistenceFactoryImpl();
```

Uso:
- La UI pide las fachadas: `Factories.service.forClientCrudService()`.
- Los commands piden los gateways: `Factories.persistence.forClient()`.

El acoplamiento con las clases `...Impl` no desaparece, pero queda **en un único sitio**: si mañana
cambia la implementación de un servicio o un gateway, solo se toca la factoría.

En `PersistenceFactory` solo están los gateways que se usan; el resto sigue comentado
(`SparePartGateway`, `SubstitutionGateway`, `VehicleTypeGateway`).

---

## 5. Patrones Transaction Script y Command

### Transaction Script
La lógica de negocio se organiza en **procedimientos, uno por operación**: añadir un cliente,
facturar, generar bonos… Cada uno se ejecuta en **una sola transacción**: o se hace entero o no se
hace nada.

### Command
Cada transaction script es una clase que implementa `Command<T>` (interfaz dada) con un único método
`T execute()`. Las acciones comunes (abrir conexión, transacción, commit, rollback) no se repiten en
cada command: las hace el **invoker**, `CommandExecutor`.

```java
public <T> T execute(Command<T> cmd) throws BusinessException {
    try (Connection c = Jdbc.createThreadConnection()) {
        c.setAutoCommit(false);
        try {
            T res = cmd.execute();
            c.commit();
            return res;
        } catch (Exception e) {
            c.rollback();
            throw e;
        }
    } catch (SQLException e) {
        throw new PersistenceException(e);
    }
}
```

`Jdbc` guarda la conexión en un `ThreadLocal`. Así, **todos los gateways** que se usan dentro de un
command recuperan la misma conexión con `Jdbc.getCurrentConnection()`, y todo queda en la misma
transacción. Los gateways **nunca** crean, cierran, confirman ni deshacen conexiones.

Normas que siguen todos los commands:
- **Constructor**: recibe los parámetros (un DTO, un id, un nif…) y los valida con `ArgumentChecks`.
  Si son nulos o están en blanco lanza `IllegalArgumentException`.
- **`execute()`**: comprueba las reglas de negocio con `BusinessChecks` (lanzan `BusinessException`)
  y hace el trabajo usando los gateways.
- **Valor de retorno**: el DTO completo si se añade algo, `Void` (`return null`) si se borra o
  actualiza, y un DTO, un `Optional` o una lista si se consulta.
- **Nombre**: el de la clase `Action` de la UI sin el sufijo `Action`
  (`AddMechanicAction` → `AddMechanic`, `ListMechanicAction` → `ListMechanic`,
  `InvoiceWorkorderAction` → `InvoiceWorkorder`).

Commands del proyecto:

| Paquete | Commands |
|---|---|
| `mechanic/crud/commands` | `AddMechanic`, `UpdateMechanic`, `DeleteMechanic`, `ListAllMechanics`, `ListMechanic` (por nif), `ListMechanicById` |
| `invoice/create/commands` | `InvoiceWorkorder`, `FindNotInvoicedWorkOrdersByClient` |
| `client/crud/commands` | `AddClient`, `UpdateClient`, `DeleteClient`, `ListClients`, `FindClientByNif`, `FindRecommendedClients` |
| `paymentmean/voucher/commands` | `GenerateVouchers`, `FindVouchersByClientNif` |

---

## 6. Patrón Table Data Gateway (TDG)

Cada tabla tiene un **gateway**: un objeto que contiene todo el SQL de esa tabla. Los commands no
saben SQL; solo llaman a métodos del gateway.

- La interfaz `Gateway<T>` (dada) define las operaciones comunes: `add`, `remove`, `update`,
  `findById` y `findAll`. Hay **un único método para añadir, borrar o modificar**: no existen métodos
  que actualicen solo un campo.
- Cada tabla tiene su paquete con **el nombre de la tabla en singular y sin la T**
  (`TClients` → `client`, `TCreditCards` → `creditcard`), con:
  - una interfaz `XxxGateway extends Gateway<XxxRecord>`, que solo añade **métodos de consulta**;
  - su implementación en `impl/XxxGatewayImpl`;
  - un `impl/XxxAssembler` que convierte un `ResultSet` en un Record.
- Los métodos de `Gateway` que ningún command necesita lanzan `NotYetImplementedException`, en lugar
  de tener SQL que nunca se ejecuta.
- Los gateways convierten toda `SQLException` en `PersistenceException`, para que la capa de
  servicio no dependa de JDBC.

Consultas añadidas a cada gateway (las únicas que necesitan los commands):

| Gateway | Métodos añadidos | Quién los usa |
|---|---|---|
| `MechanicGateway` | `findByNif` | `AddMechanic`, `ListMechanic` |
| `WorkOrderGateway` | `findByMechanicId` | `DeleteMechanic` |
| | `findByClientNifAndState` | `FindNotInvoicedWorkOrdersByClient` |
| | `findByClientIdAndInvoiceState` | `GenerateVouchers` |
| `InterventionGateway` | `findByMechanicId` | `DeleteMechanic` |
| `InvoiceGateway` | `findLastNumber` | `InvoiceWorkorder` |
| | `findByPayerId` | `DeleteClient` |
| `ClientGateway` | `findByNif`, `findRecommendedBy` | commands de clientes y bonos |
| `RecommendationGateway` | `findByRecommenderId`, `findByRecommendedId` | `DeleteClient` |
| `PaymentMeanGateway` | `findByClientId` | `DeleteClient` |
| `VoucherGateway` | `findByClientId` | `FindVouchersByClientNif` |
| `VehicleGateway` | `findByClientId` | `DeleteClient` |

Un gateway puede **consultar otras tablas para decidir qué filas de su tabla devolver**, pero solo
devuelve columnas de la suya. Por ejemplo, `InvoiceGateway.findByPayerId` une `TInvoices` con
`TCharges` y `TPaymentMeans` para encontrar las facturas pagadas por un cliente, pero devuelve solo
columnas de `TInvoices`.

### SQL externalizado: `queries.properties`
Todo el SQL está en `src/queries.properties` y se lee con `Queries.getSQLSentence(clave)`:
- agrupado por tabla, con un comentario `# TABLA` delante, y las tablas en orden alfabético;
- claves en mayúsculas con el formato `TABLA_MÉTODODELGATEWAY` (`TCLIENTS_FINDBYNIF`);
- sin reglas de negocio: los estados (`APPROVED`, `PAID`) no están escritos en el SQL, los decide
  el command y se pasan como parámetro.

---

## 7. Patrón DTO y Assemblers

Hay dos tipos de objetos de transferencia, uno por frontera entre capas:

| | DTO de servicio | Record de persistencia |
|---|---|---|
| Dónde se declara | En la interfaz de servicio (`MechanicDto`, `ClientDto`…) | En el gateway (`MechanicRecord`, `ClientRecord`…) |
| Para qué | Intercambiar datos entre UI y servicio | Copia exacta de una fila de la tabla |
| Campos | Los que define la interfaz | Las columnas de la tabla, en minúsculas (`total_amount`, `client_id`) |

Cada servicio usa **solo los DTOs declarados en su propia interfaz**. Por eso `InvoicingService` y
`PaymentMeanCrudService` tienen DTOs con el mismo nombre: no se pueden compartir.

Todos los Records heredan de `Gateway.BaseRecord`, que tiene las columnas comunes a todas las
tablas: `id` (UUID generado al crear el record), `version`, `entityState`, `createdAt` y `updatedAt`.

Los **Assemblers** hacen las conversiones y nada más (ni lógica ni acceso a datos):
- en `persistence/.../impl`: `ResultSet` → Record (`MechanicAssembler`, que hereda de
  `BaseRecordAssembler`, el cual rellena las columnas comunes);
- en `service/...`: Record ↔ DTO (`service/mechanic/crud/MechanicAssembler`, `ClientAssembler`…).

### Herencia en la base de datos: medios de pago
`TPaymentMeans` es la tabla padre y `TCashes`, `TCreditCards` y `TVouchers` son las hijas: una fila
hija tiene el mismo `id` que su fila en `TPaymentMeans`, y las dos juntas forman un único medio de
pago. Se resuelve así:

- **Records**: `CashRecord`, `CreditCardRecord` y `VoucherRecord` heredan de `PaymentMeanRecord`
  (`dtype`, `accumulated`, `client_id`), igual que la jerarquía de la base de datos.
- **Lecturas**: el gateway hijo hace un `JOIN` con `TPaymentMeans` por `id` y devuelve el Record
  completo (`TVOUCHERS_FINDBYCLIENTID`).
- **Escrituras**: cada gateway escribe **solo en su tabla**, y el command coordina el orden con el
  mismo objeto:
  - al insertar, primero el padre y luego la hija (la hija tiene una clave ajena al padre):
    `pmg.add(voucher); vg.add(voucher);`
  - al borrar, primero la hija y luego el padre.

---

## 8. Recorrido completo de un caso de uso: añadir un cliente

1. **UI** – `ui/foreman/client/action/AddClientAction`
   - Lee los datos con `InputReader`, que comprueba el formato (NIF válido, campos no vacíos) y, si
     algo está mal, lanza `UserInteractionException`.
   - Obtiene la fachada: `Factories.service.forClientCrudService()`.
   - Llama a `service.addClient(client, recommenderNif)`.
2. **Fachada** – `ClientCrudServiceImpl.addClient`
   - `executor.execute(new AddClient(client, recommenderNif))`.
3. **Constructor del command** – `AddClient(...)`
   - `ArgumentChecks`: dto no nulo, ningún campo en blanco, recomendador nulo o no vacío.
4. **CommandExecutor**
   - Abre la conexión, la guarda en el hilo y desactiva el autocommit.
5. **`AddClient.execute()`**
   - Regla: no existe otro cliente con ese NIF (`cg.findByNif` + `BusinessChecks.doesNotExist`).
   - Si hay recomendador: no puede ser el propio cliente, y debe existir.
   - `cg.add(client)` → inserta en `TClients`.
   - Crea el medio de pago en metálico: `pmg.add(cash)` (`TPaymentMeans`) y `cashg.add(cash)`
     (`TCashes`).
   - Si hay recomendador: `rg.add(recommendation)` → `TRecommendations`.
   - Devuelve el DTO con el `id` y la `version` asignados.
6. **CommandExecutor** – hace `commit` y devuelve el DTO.
7. **UI** – muestra el id del cliente nuevo.

Si algo falla:
- Una regla de negocio no se cumple → `BusinessException` → `CommandExecutor` hace `rollback` (no
  queda nada a medias) → `BaseMenu` muestra el mensaje y la aplicación sigue.
- Error de base de datos → el gateway lanza `PersistenceException` → rollback.

---

## 9. Excepciones: quién lanza qué

| Excepción | Quién la lanza | Cuándo | Qué pasa |
|---|---|---|---|
| `UserInteractionException` | UI (`InputReader`, con `UserInteractionChecks`) | El usuario teclea un dato mal formado | `BaseMenu` muestra el mensaje |
| `IllegalArgumentException` | Constructor del command (`ArgumentChecks`) | Argumento nulo, vacío o con formato incorrecto | Error de programación del cliente del servicio |
| `BusinessException` | `execute()` del command (`BusinessChecks`) | No se cumple una regla de negocio | Rollback + mensaje al usuario |
| `PersistenceException` | Gateways | `SQLException` de JDBC | Rollback |
| `NotYetImplementedException` | Métodos no implementados | Funcionalidad que no forma parte de esta entrega | — |

No hay ningún `catch` en la capa de servicio ni en la UI: el único que captura y hace rollback es
`CommandExecutor`.

---

## 10. Reglas de negocio de cada caso de uso

### Mecánicos (`MechanicCrudService`)
- **Añadir**: no puede existir otro mecánico con el mismo NIF.
- **Borrar**: el mecánico debe existir y no puede tener órdenes de trabajo asignadas ni
  intervenciones.
- **Actualizar**: el mecánico debe existir y la versión del DTO debe coincidir con la de la base de
  datos (**control de concurrencia optimista**: si otro usuario lo modificó antes, la versión ya no
  coincide). Se incrementa `version` y se actualiza `updatedAt`.

### Facturar (`InvoiceWorkorder`)
1. Todas las órdenes deben existir y estar en estado `APPROVED`.
2. Subtotal = suma de `total_amount` de las órdenes.
3. IVA: 21 % (18 % para fechas anteriores al 1/7/2012). Importe del IVA = subtotal × 21 / 100,
   redondeado a 2 decimales con `RoundingMode.HALF_EVEN`.
4. Total = subtotal + IVA. Número de factura = último número + 1 (`findLastNumber`).
5. Se inserta la factura en estado `ISSUED`, y cada orden pasa a `INVOICED`, se enlaza con la factura
   (`invoice_id`) y se incrementa su versión.

Los importes son `BigDecimal` (nunca `double`, que tiene errores de redondeo con decimales).
`BigDecimal` es inmutable: `subtotal = subtotal.add(x)` crea un objeto nuevo, por eso se reasigna.

### Órdenes no facturadas (`FindNotInvoicedWorkOrdersByClient`)
- El NIF debe tener formato válido (`NifValidator`); si no, `IllegalArgumentException`.
- Devuelve las órdenes en estado `APPROVED` de todos los vehículos del cliente. El command decide
  que "no facturada" significa `APPROVED` y se lo pasa al gateway.

### Clientes (`ClientCrudService`)
- **Añadir**: NIF único; si hay recomendador, debe existir y no ser el propio cliente. Se crea
  automáticamente su medio de pago en metálico y, si procede, la recomendación.
- **Actualizar**: el cliente debe existir, la versión debe coincidir y, si cambia el NIF, el nuevo no
  puede pertenecer a otro cliente.
- **Borrar**: no puede tener vehículos ni facturas pagadas. Se borran también sus recomendaciones
  (hechas y recibidas) y sus medios de pago (fila hija + fila de `TPaymentMeans`).
- **Recomendados**: si el recomendador no existe, devuelve una lista vacía.

### Generar bonos (`GenerateVouchers`)
Para cada cliente:
1. Obtiene sus órdenes de trabajo cuya factura está pagada (`PAID`).
2. Descarta las que ya se usaron para otro bono (`usedforvoucher`).
3. Calcula `bonos = órdenes disponibles / 3` (división entera).
4. Marca como usadas `bonos × 3` órdenes (no se pueden reutilizar).
5. Crea cada bono: medio de pago de tipo `Voucher` con 20,00 € disponibles, descripción
   "Por tres órdenes de trabajo" y un código generado por el sistema (UUID).

Devuelve `{bonos de 20, 0, 0}`: los bonos de 25 € y de 30 € no forman parte de esta entrega.
Los cálculos se hacen en el command, no en el SQL: la persistencia solo recupera filas.

---

## 11. La interfaz de usuario propia (`ui`)

Es un cliente de la capa de servicio, equivalente a los clientes oficiales `cws_client_*`:
- Cada opción de menú es una clase que implementa `Action` (dada). Su `execute()` pide datos, llama a
  la fachada y muestra el resultado con `Printer`.
- Las validaciones de formato de lo que teclea el usuario son responsabilidad de la UI:
  `InputReader` lee los datos y los comprueba con `UserInteractionChecks`.
- No contiene lógica de negocio ni acceso a datos.
- Las opciones no implementadas usan `NotYetImplementedAction` y muestran "Opción no implementada".

Puntos de entrada: `ui/manager/MainMenu`, `ui/cashier/MainMenu`, `ui/foreman/MainMenu` y
`ui/mechanic/MainMenu`.

---

## 12. Tests

Los tests están en `cws_service_tests`:
- **Aceptación** (Cucumber): un `.feature` por operación y una clase por escenario.
- **Robustez** (JUnit): comprueban que los argumentos inválidos lanzan `IllegalArgumentException`.

Para estos casos de uso se ejecuta `RunServiceTests_1` (mecánicos, facturación, órdenes no
facturadas, clientes y bonos): 128 tests en verde.
