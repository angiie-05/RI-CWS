package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.InvoiceAssembler;
import uo.ri.cws.application.service.util.NifValidator;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClient implements Command<List<InvoicingWorkOrderDto>> {

    private final WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    private final String nif;

    public FindNotInvoicedWorkOrdersByClient(String nif) {
        ArgumentChecks.isNotBlank(nif, "The client nif cannot be empty");
        ArgumentChecks.isTrue(NifValidator.isValid(nif), "The client nif is not valid");
        this.nif = nif;
    }

    @Override
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
        // work orders pending of invoicing are those in APPROVED state
        return InvoiceAssembler.toInvoicingWorkOrderDtoList(
                wg.findByClientNifAndState(nif, "APPROVED"));
    }
}
