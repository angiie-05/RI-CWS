package uo.ri.cws.application.persistence.vehicle;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface VehicleGateway extends Gateway<VehicleGateway.VehicleRecord> {

    /**
     * @param clientId
     * @return the vehicles owned by the client, or an empty list if none
     */
    List<VehicleRecord> findByClientId(String clientId) throws PersistenceException;

    public static class VehicleRecord extends Gateway.BaseRecord {
        public String make;
        public String model;
        public String licenseplate;
        public String client_id;
        public String vehicletype_id;
    }
}
