package uo.ri.cws.application.ui.manager.voucher.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GenerateVouchersAction implements Action {

    @Override
    public void execute() throws BusinessException {

        // Process
        VoucherGenerationService service = Factories.service.forVoucherService();
        int[] generated = service.generateVouchers();

        // Print result
        Console.println("Vouchers generated");
        Console.printf("\t20 euro vouchers for work orders: %d\n", generated[0]);
        Console.printf("\t25 euro vouchers for recommendations: %d\n", generated[1]);
        Console.printf("\t30 euro vouchers for invoices over 500 euro: %d\n", generated[2]);
    }

}
