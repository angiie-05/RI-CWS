package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class DeleteMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        String idMechanic = InputReader.readNotBlank("Type mechanic id ");

        // Process
        MechanicCrudService service = Factories.service.forMechanicCrudService();
        service.delete(idMechanic);

        Console.println("Mechanic deleted");
    }

}
