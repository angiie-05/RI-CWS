package uo.ri.cws.application.ui.cashier.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {
        List<String> workOrderIds = new ArrayList<>();

        // Ask the user the work order ids
        do {
            String id = InputReader.readNotBlank("Workorder id");
            workOrderIds.add(id);
        } while (moreWorkOrders());

        // Process
        InvoicingService service = Factories.service.forCreateInvoiceService();
        InvoiceDto invoice = service.create(workOrderIds);

        // Print result
        Printer.printInvoice(invoice);
    }

    private boolean moreWorkOrders() {
        return Console.readString("more work orders? (y/n) ")
                .equalsIgnoreCase("y");
    }

}
