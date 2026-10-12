package uo.ri.cws.application.service.invoice.create.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class InvoiceWorkorder {

    private static final String SQL_CHECK_WORKORDER = 
        "select id, state, total_amount from TWorkOrders where id = ?";
    private static final String SQL_GET_NEXT_INVOICE_NUMBER = 
        "select max(number) + 1 from TInvoices";
    private static final String SQL_INSERT_INVOICE = 
        "insert into TInvoices(id, number, date, vatrate, vatamount, subtotal, total, state, version) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_LINK_WORKORDER = 
        "update TWorkOrders set invoice_id = ?, state = 'INVOICED' where id = ?";

    private List<String> workOrderIds;

    public InvoiceWorkorder(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds, "Workorder ids list cannot be null");
        ArgumentChecks.isFalse(workOrderIds.isEmpty(), "Workorder ids list cannot be empty");
        for (String id : workOrderIds) {
            ArgumentChecks.isNotBlank(id, "Workorder id cannot be empty");
        }
        this.workOrderIds = workOrderIds;
    }

    public InvoiceDto execute() throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection()) {
            BigDecimal subtotal = BigDecimal.ZERO;

            for (String id : workOrderIds) {
                try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_WORKORDER)) {
                    pst.setString(1, id);
                    try (ResultSet rs = pst.executeQuery()) {
                        BusinessChecks.isTrue(rs.next(), "Workorder does not exist");
                        BusinessChecks.isTrue("APPROVED".equalsIgnoreCase(rs.getString("state")), "Workorder is not approved");
                        subtotal = subtotal.add(rs.getBigDecimal("total_amount"));
                    }
                }
            }

            long numberInvoice = 1L;
            try (PreparedStatement pst = c.prepareStatement(SQL_GET_NEXT_INVOICE_NUMBER);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next() && rs.getLong(1) > 0) {
                    numberInvoice = rs.getLong(1);
                }
            }

            LocalDate dateInvoice = LocalDate.now();
                BigDecimal vatPercentage = new BigDecimal("21");
                BigDecimal vatAmount = subtotal.multiply(vatPercentage)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
                BigDecimal total = subtotal.add(vatAmount);

            String idInvoice = UUID.randomUUID().toString();
            try (PreparedStatement pst = c.prepareStatement(SQL_INSERT_INVOICE)) {
                pst.setString(1, idInvoice);
                pst.setLong(2, numberInvoice);
                pst.setDate(3, Date.valueOf(dateInvoice));
                pst.setBigDecimal(4, vatPercentage);
                pst.setBigDecimal(5, vatAmount);
                pst.setBigDecimal(6, subtotal);
                pst.setBigDecimal(7, total);
                pst.setString(8, "ISSUED");
                pst.setLong(9, 1L);
                pst.executeUpdate();
            }

            for (String id : workOrderIds) {
                try (PreparedStatement pst = c.prepareStatement(SQL_LINK_WORKORDER)) {
                    pst.setString(1, idInvoice);
                    pst.setString(2, id);
                    pst.executeUpdate();
                }
            }

            InvoiceDto invoice = new InvoiceDto();
            invoice.id = idInvoice;
            invoice.number = numberInvoice;
            invoice.date = dateInvoice;
            invoice.total = total;
            invoice.subtotal = subtotal;
            invoice.vatAmount = vatAmount;
            invoice.vatPercentage = vatPercentage;
            invoice.state = "ISSUED";
            invoice.version = 1L;
            return invoice;
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}