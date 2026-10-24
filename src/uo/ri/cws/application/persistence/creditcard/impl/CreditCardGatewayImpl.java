package uo.ri.cws.application.persistence.creditcard.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.util.executor.Jdbc;
import uo.ri.util.exception.PersistenceException;
import uo.ri.util.jdbc.Queries;

public class CreditCardGatewayImpl implements CreditCardGateway {

    private final CreditCardAssembler assembler = new CreditCardAssembler();

    @Override
    public void add(CreditCardRecord cc) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCREDITCARDS_ADD"))) {
            pst.setString(1, cc.id);
            pst.setString(2, cc.type);
            pst.setDate(3, Date.valueOf(cc.validthru));
            pst.setString(4, cc.number);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCREDITCARDS_REMOVE"))) {
            pst.setString(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(CreditCardRecord cc) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCREDITCARDS_UPDATE"))) {
            pst.setString(1, cc.type);
            pst.setDate(2, Date.valueOf(cc.validthru));
            pst.setString(3, cc.number);
            pst.setString(4, cc.id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<CreditCardRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCREDITCARDS_FINDBYID"))) {
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
    public List<CreditCardRecord> findAll() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCREDITCARDS_FINDALL"));
                ResultSet rs = pst.executeQuery()) {
            return assembler.toRecordList(rs);
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
