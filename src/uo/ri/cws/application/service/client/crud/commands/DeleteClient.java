package uo.ri.cws.application.service.client.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.cash.CashGateway;
import uo.ri.cws.application.persistence.client.ClientGateway;
import uo.ri.cws.application.persistence.client.ClientGateway.ClientRecord;
import uo.ri.cws.application.persistence.creditcard.CreditCardGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway;
import uo.ri.cws.application.persistence.paymentmean.PaymentMeanGateway.PaymentMeanRecord;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway;
import uo.ri.cws.application.persistence.recommendation.RecommendationGateway.RecommendationRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.cws.application.persistence.voucher.VoucherGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteClient implements Command<Void> {

    private final ClientGateway cg = Factories.persistence.forClient();
    private final VehicleGateway vg = Factories.persistence.forVehicle();
    private final InvoiceGateway ig = Factories.persistence.forInvoice();
    private final RecommendationGateway rg = Factories.persistence.forRecommendation();
    private final PaymentMeanGateway pmg = Factories.persistence.forPaymentMean();
    private final CashGateway cashg = Factories.persistence.forCash();
    private final CreditCardGateway ccg = Factories.persistence.forCreditCard();
    private final VoucherGateway voucherg = Factories.persistence.forVoucher();
    private final String nif;

    public DeleteClient(String nif) {
        ArgumentChecks.isNotBlank(nif, "The nif cannot be empty");
        this.nif = nif;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ClientRecord> oc = cg.findByNif(nif);
        BusinessChecks.exists(oc, "The client does not exist");
        String clientId = oc.get().id;
        BusinessChecks.isEmpty(vg.findByClientId(clientId),
                "The client has vehicles registered");
        BusinessChecks.isEmpty(ig.findByPayerId(clientId),
                "The client has paid invoices");

        removeRecommendations(clientId);
        removePaymentMeans(clientId);
        cg.remove(clientId);
        return null;
    }

    /*
     * Both the recommendations made and the one received by the client
     */
    private void removeRecommendations(String clientId) {
        for (RecommendationRecord r : rg.findByRecommenderId(clientId)) {
            rg.remove(r.id);
        }
        for (RecommendationRecord r : rg.findByRecommendedId(clientId)) {
            rg.remove(r.id);
        }
    }

    /*
     * Every payment mean has a row in its specific table and another one in
     * TPaymentMeans
     */
    private void removePaymentMeans(String clientId) {
        for (PaymentMeanRecord pm : pmg.findByClientId(clientId)) {
            if ("Cash".equals(pm.dtype)) {
                cashg.remove(pm.id);
            } else if ("CreditCard".equals(pm.dtype)) {
                ccg.remove(pm.id);
            } else if ("Voucher".equals(pm.dtype)) {
                voucherg.remove(pm.id);
            }
            pmg.remove(pm.id);
        }
    }
}
