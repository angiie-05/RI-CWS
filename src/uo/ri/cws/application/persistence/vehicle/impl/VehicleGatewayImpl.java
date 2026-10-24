package uo.ri.cws.application.persistence.vehicle.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class VehicleGatewayImpl implements VehicleGateway {

    private final VehicleAssembler assembler = new VehicleAssembler();

    @Override
    public void add(VehicleRecord v) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_ADD"))) {
            pst.setString(1, v.id);
            pst.setString(2, v.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(v.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(v.updatedAt));
            pst.setLong(5, v.version);
            pst.setString(6, v.make);
            pst.setString(7, v.model);
            pst.setString(8, v.licenseplate);
            pst.setString(9, v.client_id);
            pst.setString(10, v.vehicletype_id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(VehicleRecord v) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_UPDATE"))) {
            pst.setString(1, v.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(v.updatedAt));
            pst.setLong(3, v.version);
            pst.setString(4, v.make);
            pst.setString(5, v.model);
            pst.setString(6, v.licenseplate);
            pst.setString(7, v.client_id);
            pst.setString(8, v.vehicletype_id);
            pst.setString(9, v.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<VehicleRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_FINDBYID"))) {
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
    public List<VehicleRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<VehicleRecord> findByClientId(String clientId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TVEHICLES_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
