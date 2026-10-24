package uo.ri.cws.application.persistence.client.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class ClientGatewayImpl implements ClientGateway {

    private final ClientAssembler assembler = new ClientAssembler();

    @Override
    public void add(ClientRecord cl) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_ADD"))) {
            pst.setString(1, cl.id);
            pst.setString(2, cl.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(cl.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(cl.updatedAt));
            pst.setLong(5, cl.version);
            pst.setString(6, cl.nif);
            pst.setString(7, cl.name);
            pst.setString(8, cl.surname);
            pst.setString(9, cl.street);
            pst.setString(10, cl.city);
            pst.setString(11, cl.country);
            pst.setString(12, cl.postalcode);
            pst.setString(13, cl.phone);
            pst.setString(14, cl.email);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ClientRecord cl) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_UPDATE"))) {
            pst.setString(1, cl.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(cl.updatedAt));
            pst.setLong(3, cl.version);
            pst.setString(4, cl.nif);
            pst.setString(5, cl.name);
            pst.setString(6, cl.surname);
            pst.setString(7, cl.street);
            pst.setString(8, cl.city);
            pst.setString(9, cl.country);
            pst.setString(10, cl.postalcode);
            pst.setString(11, cl.phone);
            pst.setString(12, cl.email);
            pst.setString(13, cl.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ClientRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_FINDBYID"))) {
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
    public List<ClientRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ClientRecord> findByNif(String nif) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_FINDBYNIF"))) {
            pst.setString(1, nif);
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
    public List<ClientRecord> findRecommendedBy(String recommenderId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCLIENTS_FINDRECOMMENDEDBY"))) {
            pst.setString(1, recommenderId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
