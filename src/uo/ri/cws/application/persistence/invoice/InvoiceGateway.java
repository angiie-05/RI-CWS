package uo.ri.cws.application.persistence.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface InvoiceGateway extends Gateway<InvoiceGateway.InvoiceRecord> {

    /**
     * @return the highest invoice number in the table, or empty if there is
     *         no invoice yet
     */
    Optional<Long> findLastNumber() throws PersistenceException;

    /**
     * @param clientId
     * @return the invoices with charges to any of the payment means of the
     *         client, that is, the invoices paid by the client; or an empty
     *         list if none
     */
    List<InvoiceRecord> findByPayerId(String clientId) throws PersistenceException;

    public static class InvoiceRecord extends Gateway.BaseRecord {
        public LocalDate date;
        public long number;
        public String state;
        public boolean usedforvoucher;
        public BigDecimal subtotal;
        public BigDecimal total;
        public BigDecimal vatamount;
        public BigDecimal vatrate;
    }
}
