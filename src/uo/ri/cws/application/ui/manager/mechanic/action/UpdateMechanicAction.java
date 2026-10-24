package uo.ri.cws.application.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String id = InputReader.readNotBlank("Type mechanic id to update");

        MechanicCrudService service = Factories.service.forMechanicCrudService();
        Optional<MechanicDto> om = service.findById(id);
        if (om.isEmpty()) {
            Console.println("There is no mechanic with that id");
            return;
        }
        MechanicDto mechanic = om.get();
        Printer.printMechanic(mechanic);

        // Ask for new data
        mechanic.nif = InputReader.readNif("Nif");
        mechanic.name = InputReader.readNotBlank("Name");
        mechanic.surname = InputReader.readNotBlank("Surname");

        // Process
        service.update(mechanic);

        // Print result
        Console.println("Mechanic updated");
    }

}
