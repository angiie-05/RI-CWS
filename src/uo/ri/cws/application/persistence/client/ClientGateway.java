package uo.ri.cws.application.persistence.client;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface ClientGateway extends Gateway<ClientGateway.ClientRecord> {

    /**
     * @param nif
     * @return the client record with the nif or empty if none
     */
    Optional<ClientRecord> findByNif(String nif) throws PersistenceException;

    /**
     * @param recommenderId the id of the client who made the recommendations
     * @return the clients recommended by the recommender, or an empty list if
     *         none
     */
    List<ClientRecord> findRecommendedBy(String recommenderId) throws PersistenceException;

    public static class ClientRecord extends Gateway.BaseRecord {
        public String nif;
        public String name;
        public String surname;
        public String street;
        public String city;
        public String country;
        public String postalcode;
        public String phone;
        public String email;
    }
}
