package uo.ri.cws.application.ui.foreman.client.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class UpdateClientAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String nif = InputReader.readNif("Nif of the client to update");

        ClientCrudService service = Factories.service.forClientCrudService();
        Optional<ClientDto> oc = service.findClientByNif(nif);
        if (oc.isEmpty()) {
            Console.println("There is no client with that nif");
            return;
        }
        ClientDto client = oc.get();
        Printer.printClient(client);

        // Ask for new data, all but the id
        client.nif = InputReader.readNif("Nif");
        client.name = InputReader.readNotBlank("Name");
        client.surname = InputReader.readNotBlank("Surname");
        client.addressStreet = InputReader.readNotBlank("Street");
        client.addressCity = InputReader.readNotBlank("City");
        client.addressCountry = InputReader.readNotBlank("Country");
        client.addressZipcode = InputReader.readNotBlank("Zip code");
        client.phone = InputReader.readNotBlank("Phone");
        client.email = InputReader.readNotBlank("Email");

        // Process
        service.updateClient(client);

        // Print result
        Console.println("Client updated");
    }

}
