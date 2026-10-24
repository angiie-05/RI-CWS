package uo.ri.cws.application.service.invoice.create;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkOrderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;

/**
 * Conversions between the invoicing service dtos and the persistence records
 */
public class InvoiceAssembler {

    private InvoiceAssembler() {
    }

    public static InvoiceDto toDto(InvoiceRecord r) {
        InvoiceDto dto = new InvoiceDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.number = r.number;
        dto.date = r.date;
        dto.state = r.state;
        dto.subtotal = r.subtotal;
        dto.vatAmount = r.vatamount;
        dto.vatPercentage = r.vatrate;
        dto.total = r.total;
        return dto;
    }

    public static InvoicingWorkOrderDto toInvoicingWorkOrderDto(WorkOrderRecord r) {
        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
        dto.id = r.id;
        dto.description = r.description;
        dto.date = r.date;
        dto.state = r.state;
        dto.amount = r.total_amount;
        return dto;
    }

    public static List<InvoicingWorkOrderDto> toInvoicingWorkOrderDtoList(
            List<WorkOrderRecord> records) {
        List<InvoicingWorkOrderDto> res = new ArrayList<>();
        for (WorkOrderRecord r : records) {
            res.add(toInvoicingWorkOrderDto(r));
        }
        return res;
    }
}
