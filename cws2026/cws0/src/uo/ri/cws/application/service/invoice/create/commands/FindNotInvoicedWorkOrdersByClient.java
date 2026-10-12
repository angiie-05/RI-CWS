package uo.ri.cws.application.service.invoice.create.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class FindNotInvoicedWorkOrdersByClient {

    private static final String SQL_CHECK_CLIENT = 
        "select id from TClients where dni = ?";
    private static final String SQL_FIND_WORKORDERS = 
        "select wo.id, wo.description, wo.date, wo.state, wo.amount "
        + "from TWorkOrders wo "
        + "join TVehicles v on wo.vehicle_id = v.id "
        + "join TClients c on v.client_id = c.id "
        + "where c.dni = ? and wo.state = 'FINISHED'";

    private String dni;

    public FindNotInvoicedWorkOrdersByClient(String dni) {
        ArgumentChecks.isNotBlank(dni, "Dni cannot be empty");
        this.dni = dni;
    }

    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
        List<InvoicingWorkOrderDto> workOrders = new ArrayList<>();
        try (Connection c = Jdbc.getCurrentConnection()) {
            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_CLIENT)) {
                pst.setString(1, dni);
                try (ResultSet rs = pst.executeQuery()) {
                    BusinessChecks.isTrue(rs.next(), "Client does not exist");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_FIND_WORKORDERS)) {
                pst.setString(1, dni);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
                        dto.id = rs.getString("id");
                        dto.description = rs.getString("description");
                        dto.date = rs.getTimestamp("date").toLocalDateTime();
                        dto.state = rs.getString("state");
                        dto.amount = rs.getBigDecimal("amount");
                        workOrders.add(dto);
                    }
                }
            }
            return workOrders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}