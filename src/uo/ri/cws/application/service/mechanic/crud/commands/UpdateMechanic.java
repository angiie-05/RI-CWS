package uo.ri.cws.application.service.mechanic.crud.commands;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private final MechanicGateway mg = Factories.persistence.forMechanic();
    private final MechanicDto dto;

    public UpdateMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "The mechanic cannot be null");
        ArgumentChecks.isNotBlank(dto.id, "The mechanic id cannot be empty");
        ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be empty");
        ArgumentChecks.isNotBlank(dto.name, "The name cannot be empty");
        ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be empty");
        this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<MechanicRecord> om = mg.findById(dto.id);
        BusinessChecks.exists(om, "The mechanic does not exist");
        MechanicRecord r = om.get();
        BusinessChecks.hasVersion(r.version, dto.version,
                "The mechanic has been modified by another user");

        r.nif = dto.nif;
        r.name = dto.name;
        r.surname = dto.surname;
        r.version++;
        r.updatedAt = LocalDateTime.now();
        mg.update(r);
        return null;
    }
}
