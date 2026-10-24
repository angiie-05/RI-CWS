package uo.ri.cws.application.service.client.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.ClientAssembler;
import uo.ri.util.exception.BusinessException;

public class ListClients implements Command<List<ClientDto>> {

    private final ClientGateway cg = Factories.persistence.forClient();

    @Override
    public List<ClientDto> execute() throws BusinessException {
        return ClientAssembler.toDtoList(cg.findAll());
    }
}
