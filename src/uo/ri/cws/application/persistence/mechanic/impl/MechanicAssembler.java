package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class MechanicAssembler extends BaseRecordAssembler<MechanicRecord> {

    public MechanicAssembler() {
        super(MechanicRecord::new);
    }

    @Override
    public MechanicRecord toRecord(ResultSet rs) throws SQLException {
        MechanicRecord record = super.toRecord(rs);
        record.nif = rs.getString("nif");
        record.name = rs.getString("name");
        record.surname = rs.getString("surname");
        return record;
    }
}
