package uo.ri.cws.application.persistence.voucher;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.util.exception.PersistenceException;

/**
 * Gateway for TVouchers. A voucher is stored in two tables: the common data of
 * every payment mean in TPaymentMeans and the voucher data, with the same id,
 * in TVouchers. This gateway reads the whole voucher but only writes in
 * TVouchers.
 */
public interface VoucherGateway extends Gateway<VoucherGateway.VoucherRecord> {

    /**
     * @param clientId
     * @return the vouchers owned by the client, or an empty list if none
     */
    List<VoucherRecord> findByClientId(String clientId) throws PersistenceException;

    public static class VoucherRecord extends PaymentMeanRecord {
        public String description;
        public BigDecimal available;
        public String code;
    }
}
