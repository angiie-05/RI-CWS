package uo.ri.cws.application.persistence.creditcard;

import java.time.LocalDate;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;

/**
 * Gateway for TCreditCards. A credit card is stored in two tables: the common
 * data of every payment mean in TPaymentMeans and the card data, with the same
 * id, in TCreditCards. This gateway reads the whole card but only writes in
 * TCreditCards.
 */
public interface CreditCardGateway extends Gateway<CreditCardGateway.CreditCardRecord> {

    public static class CreditCardRecord extends PaymentMeanRecord {
        public String type;
        public LocalDate validthru;
        public String number;
    }
}
