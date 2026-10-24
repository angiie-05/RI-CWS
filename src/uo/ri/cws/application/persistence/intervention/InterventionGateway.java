package uo.ri.cws.application.persistence.intervention;

import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface InterventionGateway extends Gateway<InterventionGateway.InterventionRecord> {

    /**
     * @param mechanicId
     * @return the interventions done by the mechanic, or an empty list if none
     */
    List<InterventionRecord> findByMechanicId(String mechanicId) throws PersistenceException;

    public static class InterventionRecord extends Gateway.BaseRecord {
        public LocalDateTime date;
        public int minutes;
        public String mechanic_id;
        public String workorder_id;
    }
}
