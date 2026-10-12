package uo.ri.cws.application.service.invoice.create;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.commands.InvoiceWorkorder;
import uo.ri.cws.application.service.invoice.create.commands.FindNotInvoicedWorkOrdersByClient;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class InvoicingServiceImpl implements InvoicingService {

    @Override
    public InvoiceDto create(List<String> workOrderIds) throws BusinessException {
        return new InvoiceWorkorder(workOrderIds).execute();
    }

    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif) throws BusinessException {
        return new FindNotInvoicedWorkOrdersByClient(nif).execute();
    }

    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<InvoiceDto> findInvoiceByNumber(Long number) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<PaymentMeanDto> findPayMeansByClientNif(String nif) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public void settleInvoice(String invoiceId, Map<String, BigDecimal> charges) throws BusinessException {
        throw new NotYetImplementedException();
    }

}