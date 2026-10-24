package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.exception.NotYetImplementedException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<PaymentMeanDto> findById(String id) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code) throws BusinessException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String cardNumber) throws BusinessException {
        throw new NotYetImplementedException();
    }

}
