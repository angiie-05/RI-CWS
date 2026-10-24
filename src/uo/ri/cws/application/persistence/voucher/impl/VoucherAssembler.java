package uo.ri.cws.application.persistence.voucher.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.voucher.VoucherGateway.VoucherRecord;

public class VoucherAssembler extends BaseRecordAssembler<VoucherRecord> {

    public VoucherAssembler() {
        super(VoucherRecord::new);
    }

    @Override
    public VoucherRecord toRecord(ResultSet rs) throws SQLException {
        VoucherRecord record = super.toRecord(rs);
        record.dtype = rs.getString("dtype");
        record.accumulated = rs.getBigDecimal("accumulated");
        record.client_id = rs.getString("client_id");
        record.description = rs.getString("description");
        record.available = rs.getBigDecimal("available");
        record.code = rs.getString("code");
        return record;
    }
}
