package uo.ri.cws.application.service.mechanic.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

/**
 * Conversions between the service dto and the persistence record
 */
public class MechanicAssembler {

    private MechanicAssembler() {
    }

    public static MechanicDto toDto(MechanicRecord r) {
        MechanicDto dto = new MechanicDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.nif = r.nif;
        dto.name = r.name;
        dto.surname = r.surname;
        return dto;
    }

    public static Optional<MechanicDto> toDto(Optional<MechanicRecord> r) {
        if (r.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDto(r.get()));
    }

    public static List<MechanicDto> toDtoList(List<MechanicRecord> records) {
        List<MechanicDto> res = new ArrayList<>();
        for (MechanicRecord r : records) {
            res.add(toDto(r));
        }
        return res;
    }

    public static MechanicRecord toRecord(MechanicDto dto) {
        MechanicRecord r = new MechanicRecord();
        r.nif = dto.nif;
        r.name = dto.name;
        r.surname = dto.surname;
        return r;
    }
}
