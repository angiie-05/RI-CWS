package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic implements Command<MechanicDto> {

    private final MechanicGateway mg = Factories.persistence.forMechanic();
    private final MechanicDto dto;

    public AddMechanic(MechanicDto dto) {
        ArgumentChecks.isNotNull(dto, "The mechanic cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be empty");
        ArgumentChecks.isNotBlank(dto.name, "The name cannot be empty");
        ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be empty");
        this.dto = dto;
    }

    @Override
    public MechanicDto execute() throws BusinessException {
        BusinessChecks.doesNotExist(mg.findByNif(dto.nif),
                "There already exists a mechanic with that nif");

        MechanicRecord r = MechanicAssembler.toRecord(dto);
        mg.add(r);

        dto.id = r.id;
        dto.version = r.version;
        return dto;
    }
}
