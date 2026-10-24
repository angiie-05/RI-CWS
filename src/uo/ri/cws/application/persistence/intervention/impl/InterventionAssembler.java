package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.intervention.InterventionGateway.InterventionRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class InterventionAssembler extends BaseRecordAssembler<InterventionRecord> {

    public InterventionAssembler() {
        super(InterventionRecord::new);
    }

    @Override
    public InterventionRecord toRecord(ResultSet rs) throws SQLException {
        InterventionRecord record = super.toRecord(rs);
        record.date = rs.getTimestamp("date").toLocalDateTime();
        record.minutes = rs.getInt("minutes");
        record.mechanic_id = rs.getString("mechanic_id");
        record.workorder_id = rs.getString("workorder_id");
        return record;
    }
}
