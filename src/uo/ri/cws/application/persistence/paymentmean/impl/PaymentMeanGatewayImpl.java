package uo.ri.cws.application.persistence.paymentmean.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class PaymentMeanGatewayImpl implements PaymentMeanGateway {

    private final PaymentMeanAssembler assembler = new PaymentMeanAssembler();

    @Override
    public void add(PaymentMeanRecord p) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_ADD"))) {
            pst.setString(1, p.id);
            pst.setString(2, p.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(p.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(p.updatedAt));
            pst.setLong(5, p.version);
            pst.setString(6, p.dtype);
            pst.setBigDecimal(7, p.accumulated);
            pst.setString(8, p.client_id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(PaymentMeanRecord p) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_UPDATE"))) {
            pst.setString(1, p.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(p.updatedAt));
            pst.setLong(3, p.version);
            pst.setString(4, p.dtype);
            pst.setBigDecimal(5, p.accumulated);
            pst.setString(6, p.client_id);
            pst.setString(7, p.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<PaymentMeanRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_FINDBYID"))) {
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
    public List<PaymentMeanRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<PaymentMeanRecord> findByClientId(String clientId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYMENTMEANS_FINDBYCLIENTID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
