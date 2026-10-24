package uo.ri.cws.application.ui.foreman.client.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListClientsAction implements Action {

    @Override
    public void execute() throws BusinessException {

        // Process
        ClientCrudService service = Factories.service.forClientCrudService();
        List<ClientDto> clients = service.findAllClients();

        // Print result
        Console.println("\nList of clients\n");
        for (ClientDto c : clients) {
            Printer.printClient(c);
        }
    }

}
