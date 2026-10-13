package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class AddMechanic {

    private static final String SQL_CHECK_NIF = 
        "select id from TMechanics where nif = ?";
    private static final String SQL_INSERT = 
        "insert into TMechanics(id, nif, name, surname, version, createdAt, updatedAt, entityState) values (?, ?, ?, ?, ?, ?, ?, ?)";

    private MechanicDto dto;

    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Mechanic cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "Nif cannot be empty");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be empty");
        ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be empty");
        this.dto = dto;
    }

    public MechanicDto execute() throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_NIF)) {
                pst.setString(1, dto.nif);
                try (ResultSet rs = pst.executeQuery()) {
                    BusinessChecks.isFalse(rs.next(), "A mechanic with this nif already exists");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_INSERT)) {
                String id = UUID.randomUUID().toString();
                java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());
                pst.setString(1, id);
                pst.setString(2, dto.nif);
                pst.setString(3, dto.name);
                pst.setString(4, dto.surname);
                pst.setLong(5, 1L);
                pst.setTimestamp(6, now);
                pst.setTimestamp(7, now);
                pst.setString(8, "ENABLED");
                pst.executeUpdate();

                dto.id = id;
                dto.version = 1L;
                return dto;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}