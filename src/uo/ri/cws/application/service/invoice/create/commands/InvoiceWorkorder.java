package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.InvoiceAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class InvoiceWorkorder implements Command<InvoiceDto> {

    private static final LocalDate VAT_CHANGE_DATE = LocalDate.parse("2012-07-01");
    private static final BigDecimal CURRENT_VAT = new BigDecimal("21");
    private static final BigDecimal OLD_VAT = new BigDecimal("18");
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    private final InvoiceGateway ig = Factories.persistence.forInvoice();
    private final List<String> workOrderIds;

    public InvoiceWorkorder(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds, "The work order list cannot be null");
        ArgumentChecks.isFalse(workOrderIds.isEmpty(), "The work order list cannot be empty");
        for (String id : workOrderIds) {
            ArgumentChecks.isNotBlank(id, "The work order ids cannot be empty");
        }
        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {
        List<WorkOrderRecord> workOrders = loadApprovedWorkOrders();

        InvoiceRecord invoice = newInvoiceFor(workOrders);
        ig.add(invoice);

        markAsInvoiced(workOrders, invoice.id);
        return InvoiceAssembler.toDto(invoice);
    }

    /*
     * Every work order must exist and be in APPROVED state
     */
    private List<WorkOrderRecord> loadApprovedWorkOrders() throws BusinessException {
        List<WorkOrderRecord> res = new ArrayList<>();
        for (String id : workOrderIds) {
            Optional<WorkOrderRecord> ow = wg.findById(id);
            BusinessChecks.exists(ow, "The work order " + id + " does not exist");
            WorkOrderRecord w = ow.get();
            BusinessChecks.isTrue("APPROVED".equals(w.state),
                    "The work order " + id + " is not APPROVED");
            res.add(w);
        }
        return res;
    }

    private InvoiceRecord newInvoiceFor(List<WorkOrderRecord> workOrders) {
        LocalDate today = LocalDate.now();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (WorkOrderRecord w : workOrders) {
            subtotal = subtotal.add(w.total_amount);
        }
        subtotal = subtotal.setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal vatRate = vatPercentageAt(today);
        BigDecimal vatAmount = subtotal.multiply(vatRate)
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_EVEN);

        InvoiceRecord r = new InvoiceRecord();
        r.number = nextInvoiceNumber();
        r.date = today;
        r.state = "ISSUED";
        r.usedforvoucher = false;
        r.subtotal = subtotal;
        r.vatrate = vatRate;
        r.vatamount = vatAmount;
        r.total = subtotal.add(vatAmount);
        return r;
    }

    private long nextInvoiceNumber() {
        return ig.findLastNumber().orElse(0L) + 1;
    }

    private BigDecimal vatPercentageAt(LocalDate date) {
        return date.isAfter(VAT_CHANGE_DATE) ? CURRENT_VAT : OLD_VAT;
    }

    private void markAsInvoiced(List<WorkOrderRecord> workOrders, String invoiceId) {
        LocalDateTime now = LocalDateTime.now();
        for (WorkOrderRecord w : workOrders) {
            w.invoice_id = invoiceId;
            w.state = "INVOICED";
            w.version++;
            w.updatedAt = now;
            wg.update(w);
        }
    }
}
