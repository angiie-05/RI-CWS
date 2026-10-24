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

public class ListMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String nif = InputReader.readNif("nif");

        // Process
        MechanicCrudService service = Factories.service.forMechanicCrudService();
        Optional<MechanicDto> mechanic = service.findByNif(nif);

        // Print result
        if (mechanic.isEmpty()) {
            Console.println("There is no mechanic with that nif");
            return;
        }
        Console.println("\nMechanic information \n");
        Printer.printMechanic(mechanic.get());
    }
}
