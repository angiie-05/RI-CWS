package uo.ri.cws.application.persistence.cash;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;

/**
 * Gateway for TCashes. A cash is stored in two tables: the common data of
 * every payment mean in TPaymentMeans and the cash row, with the same id, in
 * TCashes. This gateway reads the whole cash but only writes in TCashes.
 */
public interface CashGateway extends Gateway<CashGateway.CashRecord> {

    public static class CashRecord extends PaymentMeanRecord {
    }
}
