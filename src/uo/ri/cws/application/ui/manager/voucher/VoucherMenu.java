package uo.ri.cws.application.ui.manager.voucher;

import uo.ri.cws.application.ui.manager.voucher.action.FindVouchersByClientNifAction;
import uo.ri.cws.application.ui.manager.voucher.action.GenerateVouchersAction;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class VoucherMenu extends BaseMenu {

    public VoucherMenu() {
        menuOptions = new Object[][] {
                { "Manager > Voucher management", null },
                { "Generate vouchers", GenerateVouchersAction.class },
                { "List vouchers by client nif", FindVouchersByClientNifAction.class },
                { "Show voucher summary", NotYetImplementedAction.class }, };
    }

}
