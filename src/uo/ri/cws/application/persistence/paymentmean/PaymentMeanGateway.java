package uo.ri.cws.application.persistence.paymentmean;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface PaymentMeanGateway extends Gateway<PaymentMeanGateway.PaymentMeanRecord> {

    /**
     * @param clientId
     * @return all the payment means of the client, whatever their type, or an
     *         empty list if none
     */
    List<PaymentMeanRecord> findByClientId(String clientId) throws PersistenceException;

    public static class PaymentMeanRecord extends Gateway.BaseRecord {
        public String dtype; // Cash, CreditCard, Voucher
        public BigDecimal accumulated;
        public String client_id;
    }
}
