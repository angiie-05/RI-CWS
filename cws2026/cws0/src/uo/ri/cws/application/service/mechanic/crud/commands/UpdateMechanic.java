package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class UpdateMechanic {

    private static final String SQL_CHECK_EXISTS = 
        "select version from TMechanics where id = ?";
    private static final String SQL_UPDATE = 
        "update TMechanics set name = ?, surname = ?, nif = ?, version = version + 1, updatedAt = CURRENT_TIMESTAMP where id = ?";

    private MechanicDto dto;

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "Mechanic cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "Mechanic id cannot be empty");
        ArgumentChecks.isNotBlank(dto.nif, "Mechanic nif cannot be empty");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be empty");
        ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be empty");
        this.dto = dto;
    }

    public Void execute() throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection()) {
            try (PreparedStatement pst = c.prepareStatement(SQL_CHECK_EXISTS)) {
                pst.setString(1, dto.id);
                try (ResultSet rs = pst.executeQuery()) {
                        BusinessChecks.isTrue(rs.next(), "Mechanic does not exist");
                        BusinessChecks.isTrue(rs.getLong("version") == dto.version,
                            "Mechanic has been updated by another user");
                }
            }

            try (PreparedStatement pst = c.prepareStatement(SQL_UPDATE)) {
                pst.setString(1, dto.name);
                pst.setString(2, dto.surname);
                pst.setString(3, dto.nif);
                pst.setString(4, dto.id);
                pst.executeUpdate();
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}