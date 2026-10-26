package uo.ri.cws.application.persistence.recommendation.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.NotYetImplementedException;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class RecommendationGatewayImpl implements RecommendationGateway {

    private final RecommendationAssembler assembler = new RecommendationAssembler();

    @Override
    public void add(RecommendationRecord r) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TRECOMMENDATIONS_ADD"))) {
            pst.setString(1, r.id);
            pst.setString(2, r.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(r.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(r.updatedAt));
            pst.setLong(5, r.version);
            pst.setBoolean(6, r.usedforvoucher);
            pst.setString(7, r.recommender_id);
            pst.setString(8, r.recommended_id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TRECOMMENDATIONS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(RecommendationRecord r) throws PersistenceException {
        throw new NotYetImplementedException();
    }

    @Override
    public Optional<RecommendationRecord> findById(String id) throws PersistenceException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<RecommendationRecord> findAll() throws PersistenceException {
        throw new NotYetImplementedException();
    }

    @Override
    public List<RecommendationRecord> findByRecommenderId(String recommenderId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TRECOMMENDATIONS_FINDBYRECOMMENDERID"))) {
            pst.setString(1, recommenderId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<RecommendationRecord> findByRecommendedId(String recommendedId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TRECOMMENDATIONS_FINDBYRECOMMENDEDID"))) {
            pst.setString(1, recommendedId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
