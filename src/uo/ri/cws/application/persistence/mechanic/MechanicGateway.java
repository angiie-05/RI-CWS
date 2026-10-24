package uo.ri.cws.application.persistence.mechanic;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface MechanicGateway extends Gateway<MechanicGateway.MechanicRecord> {

    /**
     * @param nif
     * @return the mechanic record with the nif or empty if none
     */
    Optional<MechanicRecord> findByNif(String nif) throws PersistenceException;

    public static class MechanicRecord extends Gateway.BaseRecord {
        public String nif;
        public String name;
        public String surname;
    }
}
