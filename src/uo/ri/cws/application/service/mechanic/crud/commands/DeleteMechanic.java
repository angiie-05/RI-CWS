package uo.ri.cws.application.service.mechanic.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private final MechanicGateway mg = Factories.persistence.forMechanic();
    private final WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    private final InterventionGateway ig = Factories.persistence.forIntervention();
    private final String mechanicId;

    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotBlank(mechanicId, "The mechanic id cannot be empty");
        this.mechanicId = mechanicId;
    }

    @Override
    public Void execute() throws BusinessException {
        BusinessChecks.exists(mg.findById(mechanicId),
                "The mechanic does not exist");
        BusinessChecks.isEmpty(wg.findByMechanicId(mechanicId),
                "The mechanic has work orders assigned");
        BusinessChecks.isEmpty(ig.findByMechanicId(mechanicId),
                "The mechanic has interventions");

        mg.remove(mechanicId);
        return null;
    }
}
