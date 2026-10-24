package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class InvoiceGatewayImpl implements InvoiceGateway {

    private final InvoiceAssembler assembler = new InvoiceAssembler();

    @Override
    public void add(InvoiceRecord i) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_ADD"))) {
            pst.setString(1, i.id);
            pst.setString(2, i.entityState);
            pst.setTimestamp(3, Timestamp.valueOf(i.createdAt));
            pst.setTimestamp(4, Timestamp.valueOf(i.updatedAt));
            pst.setLong(5, i.version);
            pst.setDate(6, Date.valueOf(i.date));
            pst.setLong(7, i.number);
            pst.setString(8, i.state);
            pst.setBoolean(9, i.usedforvoucher);
            pst.setBigDecimal(10, i.subtotal);
            pst.setBigDecimal(11, i.total);
            pst.setBigDecimal(12, i.vatamount);
            pst.setBigDecimal(13, i.vatrate);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(InvoiceRecord i) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_UPDATE"))) {
            pst.setString(1, i.entityState);
            pst.setTimestamp(2, Timestamp.valueOf(i.updatedAt));
            pst.setLong(3, i.version);
            pst.setDate(4, Date.valueOf(i.date));
            pst.setLong(5, i.number);
            pst.setString(6, i.state);
            pst.setBoolean(7, i.usedforvoucher);
            pst.setBigDecimal(8, i.subtotal);
            pst.setBigDecimal(9, i.total);
            pst.setBigDecimal(10, i.vatamount);
            pst.setBigDecimal(11, i.vatrate);
            pst.setString(12, i.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<InvoiceRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FINDBYID"))) {
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
    public List<InvoiceRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<Long> findLastNumber() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FINDLASTNUMBER"));
                ResultSet rs = pst.executeQuery()) {
            if (!rs.next()) {
                return Optional.empty();
            }
            long number = rs.getLong(1);
            return rs.wasNull() ? Optional.empty() : Optional.of(number);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<InvoiceRecord> findByPayerId(String clientId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FINDBYPAYERID"))) {
            pst.setString(1, clientId);
            try (ResultSet rs = pst.executeQuery()) {
                return assembler.toRecordList(rs);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
