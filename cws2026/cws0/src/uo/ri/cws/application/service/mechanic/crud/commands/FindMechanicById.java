package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class FindMechanicById {

    private static final String SQL = 
        "select id, nif, name, surname, version from TMechanics where id = ?";

    private String id;

    public FindMechanicById(String id) {
        ArgumentChecks.isNotBlank(id, "Mechanic id cannot be empty");
        this.id = id;
    }

    public Optional<MechanicDto> execute() throws BusinessException {
        try (Connection c = Jdbc.createThreadConnection();
             PreparedStatement pst = c.prepareStatement(SQL)) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    MechanicDto m = new MechanicDto();
                    m.id = rs.getString("id");
                    m.nif = rs.getString("nif");
                    m.name = rs.getString("name");
                    m.surname = rs.getString("surname");
                    m.version = rs.getLong("version");
                    return Optional.of(m);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}