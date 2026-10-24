package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class InterventionGatewayImpl implements InterventionGateway {

    private final InterventionAssembler assembler = new InterventionAssembler();

    @Override
    public void add(InterventionRecord i) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_ADD"))) {
            pst.setString(1, i.id);
            pst.setString(2, i.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(i.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(i.updatedAt));
            pst.setLong(5, i.version);
            pst.setTimestamp(6, Timestamp.valueOf(i.date));
            pst.setInt(7, i.minutes);
            pst.setString(8, i.mechanic_id);
            pst.setString(9, i.workorder_id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(InterventionRecord i) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_UPDATE"))) {
            pst.setString(1, i.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(i.updatedAt));
            pst.setLong(3, i.version);
            pst.setTimestamp(4, Timestamp.valueOf(i.date));
            pst.setInt(5, i.minutes);
            pst.setString(6, i.mechanic_id);
            pst.setString(7, i.workorder_id);
            pst.setString(8, i.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<InterventionRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_FINDBYID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next()
                        ? Optional.of(assembler.toRecord(rs))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<InterventionRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<InterventionRecord> findByMechanicId(String mechanicId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINTERVENTIONS_FINDBYMECHANICID"))) {
            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
