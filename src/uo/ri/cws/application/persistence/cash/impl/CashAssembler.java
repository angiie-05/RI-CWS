package uo.ri.cws.application.persistence.cash.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.cash.CashGateway.CashRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class CashAssembler extends BaseRecordAssembler<CashRecord> {

    public CashAssembler() {
        super(CashRecord::new);
    }

    @Override
    public CashRecord toRecord(ResultSet rs) throws SQLException {
        CashRecord record = super.toRecord(rs);
        record.dtype = rs.getString("dtype");
        record.accumulated = rs.getBigDecimal("accumulated");
        record.client_id = rs.getString("client_id");
        return record;
    }
}
