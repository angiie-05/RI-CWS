package uo.ri.cws.application.persistence.invoice.impl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class InvoiceAssembler extends BaseRecordAssembler<InvoiceRecord> {

    public InvoiceAssembler() {
        super(InvoiceRecord::new);
    }

    @Override
    public InvoiceRecord toRecord(ResultSet rs) throws SQLException {
        InvoiceRecord record = super.toRecord(rs);
        Date date = rs.getDate("date");
        record.date = date == null ? null : date.toLocalDate();
        record.number = rs.getLong("number");
        record.state = rs.getString("state");
        record.usedforvoucher = rs.getBoolean("usedforvoucher");
        record.subtotal = rs.getBigDecimal("subtotal");
        record.total = rs.getBigDecimal("total");
        record.vatamount = rs.getBigDecimal("vatamount");
        record.vatrate = rs.getBigDecimal("vatrate");
        return record;
    }

}
