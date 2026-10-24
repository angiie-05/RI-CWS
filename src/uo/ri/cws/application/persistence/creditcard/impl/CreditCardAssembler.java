package uo.ri.cws.application.persistence.creditcard.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.creditcard.CreditCardGateway.CreditCardRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class CreditCardAssembler extends BaseRecordAssembler<CreditCardRecord> {

    public CreditCardAssembler() {
        super(CreditCardRecord::new);
    }

    @Override
    public CreditCardRecord toRecord(ResultSet rs) throws SQLException {
        CreditCardRecord record = super.toRecord(rs);
        record.dtype = rs.getString("dtype");
        record.accumulated = rs.getBigDecimal("accumulated");
        record.client_id = rs.getString("client_id");
        record.type = rs.getString("type");
        record.validthru = rs.getDate("validthru").toLocalDate();
        record.number = rs.getString("number");
        return record;
    }
}
