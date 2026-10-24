package uo.ri.cws.application.ui.foreman.client.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class AddClientAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        ClientDto client = new ClientDto();
        client.nif = InputReader.readNif("Nif");
        client.name = InputReader.readNotBlank("Name");
        client.surname = InputReader.readNotBlank("Surname");
        client.addressStreet = InputReader.readNotBlank("Street");
        client.addressCity = InputReader.readNotBlank("City");
        client.addressCountry = InputReader.readNotBlank("Country");
        client.addressZipcode = InputReader.readNotBlank("Zip code");
        client.phone = InputReader.readNotBlank("Phone");
        client.email = InputReader.readNotBlank("Email");
        String recommenderNif = InputReader.readOptionalNif("Recommender nif (blank if none)");

        // Process
        ClientCrudService service = Factories.service.forClientCrudService();
        client = service.addClient(client, recommenderNif);

        // Print result
        Console.println("Client added: " + client.id);
    }

}
