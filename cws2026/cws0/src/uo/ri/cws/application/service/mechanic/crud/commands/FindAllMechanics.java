package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;

public class FindAllMechanics {

    private static final String SQL = 
        "select id, dni, name, surname, version from TMechanics";

    public List<MechanicDto> execute() throws BusinessException {
        List<MechanicDto> res = new ArrayList<>();
        try (Connection c = Jdbc.getCurrentConnection();
             PreparedStatement pst = c.prepareStatement(SQL);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                MechanicDto m = new MechanicDto();
                m.id = rs.getString("id");
                m.nif = rs.getString("dni");
                m.name = rs.getString("name");
                m.surname = rs.getString("surname");
                m.version = rs.getLong("version");
                res.add(m);
            }
            return res;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}