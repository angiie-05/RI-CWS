package uo.ri.cws.application.persistence.workorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface WorkOrderGateway extends Gateway<WorkOrderGateway.WorkOrderRecord> {

    /**
     * @param mechanicId
     * @return the work orders assigned to the mechanic, or an empty list if
     *         none
     */
    List<WorkOrderRecord> findByMechanicId(String mechanicId) throws PersistenceException;

    /**
     * @param nif of the client
     * @param state of the work orders
     * @return the work orders in that state of every vehicle owned by the
     *         client, or an empty list if none
     */
    List<WorkOrderRecord> findByClientNifAndState(String nif, String state) throws PersistenceException;

    /**
     * @param clientId
     * @param invoiceState state of the invoice the work orders belong to
     * @return the work orders of every vehicle owned by the client whose
     *         invoice is in that state, ordered by date, or an empty list if
     *         none
     */
    List<WorkOrderRecord> findByClientIdAndInvoiceState(String clientId, String invoiceState)
            throws PersistenceException;

    public static class WorkOrderRecord extends Gateway.BaseRecord {
        public LocalDateTime date;
        public String description;
        public String state;
        public boolean usedforvoucher;
        public BigDecimal total_amount;
        public String invoice_id;
        public String mechanic_id;
        public String vehicle_id;
    }
}
