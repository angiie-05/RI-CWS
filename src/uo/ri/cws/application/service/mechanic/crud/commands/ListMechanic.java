package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.mechanic.crud.MechanicAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class ListMechanic implements Command<Optional<MechanicDto>> {

    private final MechanicGateway mg = Factories.persistence.forMechanic();
    private final String nif;

    public ListMechanic(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be empty");
        this.nif = nif;
    }

    @Override
    public Optional<MechanicDto> execute() throws BusinessException {
        return MechanicAssembler.toDto(mg.findByNif(nif));
    }
}
