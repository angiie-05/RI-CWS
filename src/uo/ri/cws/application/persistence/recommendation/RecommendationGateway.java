package uo.ri.cws.application.persistence.recommendation;

import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.PersistenceException;

public interface RecommendationGateway
        extends Gateway<RecommendationGateway.RecommendationRecord> {

    /**
     * @param recommenderId
     * @return the recommendations made by the client, or an empty list if
     *         none
     */
    List<RecommendationRecord> findByRecommenderId(String recommenderId) throws PersistenceException;

    /**
     * @param recommendedId
     * @return the recommendations received by the client, or an empty list if
     *         none
     */
    List<RecommendationRecord> findByRecommendedId(String recommendedId) throws PersistenceException;

    public static class RecommendationRecord extends Gateway.BaseRecord {
        public boolean usedforvoucher;
        public String recommender_id;
        public String recommended_id;
    }
}
