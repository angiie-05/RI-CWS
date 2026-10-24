package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.ClientAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindClientByNif implements Command<Optional<ClientDto>> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final String nif;

    public FindClientByNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be empty");
        this.nif = nif;
    }

    @Override
    public Optional<ClientDto> execute() throws BusinessException {
        return ClientAssembler.toDto(cg.findByNif(nif));
    }
}
