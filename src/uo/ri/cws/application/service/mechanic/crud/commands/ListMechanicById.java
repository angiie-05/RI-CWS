package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class ListMechanicById implements Command<Optional<MechanicDto>> {

    private final MechanicGateway mg = Factories.persistence.forMechanic();
    private final String id;

    public ListMechanicById(String id) {
        ArgumentChecks.isNotBlank(id, "The mechanic id cannot be empty");
        this.id = id;
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return MechanicAssembler.toDto(mg.findById(id));
    }
}
