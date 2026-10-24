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

public class FindClientByNifAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String nif = InputReader.readNif("Client nif");

        // Process
        ClientCrudService service = Factories.service.forClientCrudService();
        Optional<ClientDto> client = service.findClientByNif(nif);

        // Print result
        if (client.isEmpty()) {
            Console.println("There is no client with that nif");
            return;
        }
        Printer.printClient(client.get());
    }

}
