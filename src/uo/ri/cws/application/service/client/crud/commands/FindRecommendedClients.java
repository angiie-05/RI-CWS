package uo.ri.cws.application.service.client.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.ClientAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindRecommendedClients implements Command<List<ClientDto>> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final String recommenderNif;

    public FindRecommendedClients(String recommenderNif) {
        ArgumentChecks.isNotBlank(recommenderNif, "The recommender nif cannot be empty");
        this.recommenderNif = recommenderNif;
    }

    @Override
    public List<ClientDto> execute() throws BusinessException {
        Optional<ClientRecord> recommender = cg.findByNif(recommenderNif);
        if (recommender.isEmpty()) {
            return new ArrayList<>();
        }
        return ClientAssembler.toDtoList(cg.findRecommendedBy(recommender.get().id));
    }
}
