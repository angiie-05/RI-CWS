package uo.ri.cws.application.persistence.client.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.assembler.BaseRecordAssembler;

public class ClientAssembler extends BaseRecordAssembler<ClientRecord> {

    public ClientAssembler() {
        super(ClientRecord::new);
    }

    @Override
    public ClientRecord toRecord(ResultSet rs) throws SQLException {
        ClientRecord record = super.toRecord(rs);
        record.nif = rs.getString("nif");
        record.name = rs.getString("name");
        record.surname = rs.getString("surname");
        record.street = rs.getString("street");
        record.city = rs.getString("city");
        record.country = rs.getString("country");
        record.postalcode = rs.getString("postalcode");
        record.phone = rs.getString("phone");
        record.email = rs.getString("email");
        return record;
    }
}
