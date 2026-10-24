package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class MechanicGatewayImpl implements MechanicGateway {

    private final MechanicAssembler assembler = new MechanicAssembler();

    @Override
    public void add(MechanicRecord m) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_ADD"))) {
            pst.setString(1, m.id);
            pst.setString(2, m.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(m.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(m.updatedAt));
            pst.setLong(5, m.version);
            pst.setString(6, m.nif);
            pst.setString(7, m.name);
            pst.setString(8, m.surname);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(MechanicRecord m) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_UPDATE"))) {
            pst.setString(1, m.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(m.updatedAt));
            pst.setLong(3, m.version);
            pst.setString(4, m.nif);
            pst.setString(5, m.name);
            pst.setString(6, m.surname);
            pst.setString(7, m.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<MechanicRecord> findById(String id) throws PersistenceException {
        return findOneBy("TMECHANICS_FINDBYID", id);
    }

    @Override
    public Optional<MechanicRecord> findByNif(String nif) throws PersistenceException {
        return findOneBy("TMECHANICS_FINDBYNIF", nif);
    }

    @Override
    public List<MechanicRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private Optional<MechanicRecord> findOneBy(String queryKey, String param) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(queryKey))) {
            pst.setString(1, param);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next()
                        ? Optional.of(assembler.toRecord(rs))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
