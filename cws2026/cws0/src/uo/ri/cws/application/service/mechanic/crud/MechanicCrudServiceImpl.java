package uo.ri.cws.application.service.mechanic.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.commands.AddMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.DeleteMechanic;
import uo.ri.cws.application.service.mechanic.crud.commands.FindAllMechanics;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicById;
import uo.ri.cws.application.service.mechanic.crud.commands.FindMechanicByNif;
import uo.ri.cws.application.service.mechanic.crud.commands.UpdateMechanic;
import uo.ri.util.exception.BusinessException;

public class MechanicCrudServiceImpl implements MechanicCrudService {

    @Override
    public MechanicDto create(MechanicDto dto) throws BusinessException {
        return new AddMechanic(dto).execute();
    }

    @Override
    public void delete(String idMechanic) throws BusinessException {
        new DeleteMechanic(idMechanic).execute();
    }

    @Override
    public void update(MechanicDto dto) throws BusinessException {
        new UpdateMechanic(dto).execute();
    }

    @Override
    public List<MechanicDto> findAll() throws BusinessException {
        return new FindAllMechanics().execute();
    }

    @Override
    public Optional<MechanicDto> findById(String idMechanic) throws BusinessException {
        return new FindMechanicById(idMechanic).execute();
    }

    @Override
    public Optional<MechanicDto> findByNif(String nifMechanic) throws BusinessException {
        return new FindMechanicByNif(nifMechanic).execute();
    }
}