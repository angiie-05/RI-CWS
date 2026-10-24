package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.voucher.VoucherAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final VoucherGateway vg = Factories.persistence.forVoucher();
    private final String nif;

    public FindVouchersByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be empty");
        this.nif = nif;
    }

    @Override
    public List<VoucherDto> execute() throws BusinessException {
        Optional<ClientRecord> client = cg.findByNif(nif);
        if (client.isEmpty()) {
            return new ArrayList<>();
        }
        return VoucherAssembler.toDtoList(vg.findByClientId(client.get().id));
    }
}
