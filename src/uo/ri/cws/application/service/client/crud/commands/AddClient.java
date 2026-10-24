package uo.ri.cws.application.service.client.crud.commands;

import java.math.BigDecimal;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.cash.CashGateway;
import uo.ri.cws.application.persistence.cash.CashGateway.CashRecord;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.client.crud.ClientAssembler;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddClient implements Command<ClientDto> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    private final CashGateway cashg = Factories.persistence.forCash();
    private final RecommendationGateway rg = Factories.persistence.forRecommendation();
    private final ClientDto dto;
    private final String recommenderNif;

    public AddClient(ClientDto dto, String recommenderNif) {
        ArgumentChecks.isNotNull(dto, "The client cannot be null");
        ArgumentChecks.isNotBlank(dto.nif, "The nif cannot be empty");
        ArgumentChecks.isNotBlank(dto.name, "The name cannot be empty");
        ArgumentChecks.isNotBlank(dto.surname, "The surname cannot be empty");
        ArgumentChecks.isNotBlank(dto.addressStreet, "The street cannot be empty");
        ArgumentChecks.isNotBlank(dto.addressCity, "The city cannot be empty");
        ArgumentChecks.isNotBlank(dto.addressCountry, "The country cannot be empty");
        ArgumentChecks.isNotBlank(dto.addressZipcode, "The zip code cannot be empty");
        ArgumentChecks.isNotBlank(dto.phone, "The phone cannot be empty");
        ArgumentChecks.isNotBlank(dto.email, "The email cannot be empty");
        ArgumentChecks.isTrue(recommenderNif == null || !recommenderNif.isBlank(),
                "The recommender nif cannot be empty");
        this.dto = dto;
        this.recommenderNif = recommenderNif;
    }

    @Override
    public ClientDto execute() throws BusinessException {
        BusinessChecks.doesNotExist(cg.findByNif(dto.nif),
                "There already exists a client with that nif");
        String recommenderId = checkRecommender();

        ClientRecord client = ClientAssembler.toRecord(dto);
        cg.add(client);
        addCashFor(client.id);
        if (recommenderId != null) {
            addRecommendation(recommenderId, client.id);
        }

        dto.id = client.id;
        dto.version = client.version;
        return dto;
    }

    /*
     * Returns the id of the recommender, or null if the client has not been
     * recommended by anyone
     */
    private String checkRecommender() throws BusinessException {
        if (recommenderNif == null) {
            return null;
        }
        BusinessChecks.isFalse(recommenderNif.equals(dto.nif),
                "A client cannot recommend itself");
        Optional<ClientRecord> recommender = cg.findByNif(recommenderNif);
        BusinessChecks.exists(recommender, "The recommender client does not exist");
        return recommender.get().id;
    }

    /*
     * Every client has a cash payment mean, created along with the client.
     * The row in TPaymentMeans goes first, as TCashes references it.
     */
    private void addCashFor(String clientId) {
        CashRecord cash = new CashRecord();
        cash.dtype = "Cash";
        cash.accumulated = BigDecimal.ZERO;
        cash.client_id = clientId;
        pmg.add(cash);
        cashg.add(cash);
    }

    private void addRecommendation(String recommenderId, String recommendedId) {
        RecommendationRecord r = new RecommendationRecord();
        r.usedforvoucher = false;
        r.recommender_id = recommenderId;
        r.recommended_id = recommendedId;
        rg.add(r);
    }
}
