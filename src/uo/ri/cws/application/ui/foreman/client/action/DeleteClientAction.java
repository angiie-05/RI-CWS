package uo.ri.cws.application.ui.foreman.client.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class DeleteClientAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        String nif = InputReader.readNif("Nif of the client to delete");

        // Process
        ClientCrudService service = Factories.service.forClientCrudService();
        service.deleteClientByNif(nif);

        Console.println("Client deleted");
    }

}
