package uo.ri.cws.application.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrdersByClientAction implements Action {

    /**
     * Process:
     * - Ask customer nif
     * - Display all uncharged workorders (state APPROVED).
     *   For each workorder, display id, description, date, state and amount
     */
    @Override
    public void execute() throws BusinessException, UserInteractionException {
        String nif = InputReader.readNif("Client nif ");

        InvoicingService service = Factories.service.forCreateInvoiceService();
        List<InvoicingWorkOrderDto> workOrders = service.findNotInvoicedWorkOrdersByClientNif(nif);

        Console.println("\nClient's not invoiced work orders\n");
        for (InvoicingWorkOrderDto wo : workOrders) {
            Printer.printInvoicingWorkOrder(wo);
        }
    }

}
