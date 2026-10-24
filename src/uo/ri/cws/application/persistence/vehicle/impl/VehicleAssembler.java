package uo.ri.cws.application.persistence.vehicle.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway.VehicleRecord;

public class VehicleAssembler extends BaseRecordAssembler<VehicleRecord> {

    public VehicleAssembler() {
        super(VehicleRecord::new);
    }

    @Override
    public VehicleRecord toRecord(ResultSet rs) throws SQLException {
        VehicleRecord record = super.toRecord(rs);
        record.make = rs.getString("make");
        record.model = rs.getString("model");
        record.licenseplate = rs.getString("licenseplate");
        record.client_id = rs.getString("client_id");
        record.vehicletype_id = rs.getString("vehicletype_id");
        return record;
    }
}
