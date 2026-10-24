package uo.ri.cws.application.service.client.crud.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateClient implements Command<Void> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final ClientDto dto;

    public UpdateClient(ClientDto dto) {
        ArgumentChecks.isNotNull(dto, "The client cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "The client id cannot be empty");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ClientRecord> oc = cg.findById(dto.id);
        BusinessChecks.exists(oc, "The client does not exist");
        ClientRecord r = oc.get();
        BusinessChecks.hasVersion(r.version, dto.version,
                "The client has been modified by another user");
        if (!r.nif.equals(dto.nif)) {
            BusinessChecks.doesNotExist(cg.findByNif(dto.nif),
                    "There already exists another client with that nif");
        }

        r.nif = dto.nif;
        r.name = dto.name;
        r.surname = dto.surname;
        r.street = dto.addressStreet;
        r.city = dto.addressCity;
        r.country = dto.addressCountry;
        r.postalcode = dto.addressZipcode;
        r.phone = dto.phone;
        r.email = dto.email;
        r.version++;
        r.updatedAt = LocalDateTime.now();
        cg.update(r);
        return null;
    }
}
