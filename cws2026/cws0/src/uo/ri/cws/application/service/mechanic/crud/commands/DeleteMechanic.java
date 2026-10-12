package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class DeleteMechanic {

    private static final String SQL_CHECK_EXISTS = 
        "select id from TMechanics where id = ?";
    private static final String SQL_CHECK_INTERVENTIONS = 
        "select count(*) from TInterventions where mechanic_id = ?";
    private static final String SQL_CHECK_WORKORDERS = 
        "select count(*) from TWorkOrders where mechanic_id = ?";
    private static final String SQL_DELETE = 
        "delete from TMechanics where id = ?";

    private String id;

    public DeleteMechanic(String id) {
        ArgumentChecks.isNotBlank(id, "Mechanic id cannot be empty");
        this.id = id;
    }

    public Void execute() throws BusinessException {
        try (Connection c = Jdbc.getCurrentConnection()) {
            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_EXISTS)) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    BusinessChecks.isTrue(rs.next(), "Mechanic does not exist");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_INTERVENTIONS)) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    BusinessChecks.isTrue(rs.getInt(1) == 0, "Mechanic has interventions");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_WORKORDERS)) {
                pst.setString(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    rs.next();
                    BusinessChecks.isTrue(rs.getInt(1) == 0, "Mechanic has workorders assigned");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_DELETE)) {
                pst.setString(1, id);
                pst.executeUpdate();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}