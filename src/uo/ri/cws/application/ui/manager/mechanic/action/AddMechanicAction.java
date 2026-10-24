package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        MechanicDto mechanic = new MechanicDto();
        mechanic.nif = InputReader.readNif("nif");
        mechanic.name = InputReader.readNotBlank("Name");
        mechanic.surname = InputReader.readNotBlank("Surname");

        // Process
        MechanicCrudService service = Factories.service.forMechanicCrudService();
        mechanic = service.create(mechanic);

        // Print result
        Console.println("Mechanic added: " + mechanic.id);
    }

}
