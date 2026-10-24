package uo.ri.cws.application.ui.manager.voucher.action;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.ui.util.InputReader;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.UserInteractionException;
import uo.ri.util.menu.Action;

public class FindVouchersByClientNifAction implements Action {

    @Override
    public void execute() throws BusinessException, UserInteractionException {

        // Get info
        String nif = InputReader.readNif("Client nif");

        // Process
        VoucherGenerationService service = Factories.service.forVoucherService();
        List<VoucherDto> vouchers = service.findVouchersByClientNif(nif);

        // Print result
        Console.println("\nClient vouchers\n");
        BigDecimal available = BigDecimal.ZERO;
        BigDecimal consumed = BigDecimal.ZERO;
        for (VoucherDto v : vouchers) {
            Printer.printVoucher(v);
            available = available.add(v.availableAmount);
            consumed = consumed.add(v.accumulatedAmount);
        }
        Console.printf("\n\t%d vouchers, total %.2f €, consumed %.2f €, available %.2f €\n",
                vouchers.size(), available.add(consumed), consumed, available);
    }

}
