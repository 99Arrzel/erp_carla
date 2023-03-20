package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.models.PeriodoModel;
import com.carla.erp_senseve.repositories.PeriodoRepository;
import com.carla.erp_senseve.validate.GestionValidate;
import com.carla.erp_senseve.validate.PeriodoValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
public class PeriodoService {
    @Autowired
    private PeriodoRepository periodoRepository;
    @Autowired
    private GestionService gestionService;

    public PeriodoModel savePeriodo(PeriodoModel periodo) {
        return periodoRepository.save(periodo);
    }

    public PeriodoModel upsert(PeriodoValidate periodo, String username) {
        //Search empresa
        GestionModel gestion = gestionService.una_gestion(periodo.getGestion_id(), username);
        if (gestion == null) {
            return null;
        }
        PeriodoModel g = new PeriodoModel();

        if (periodo.getId() == null) { //Entonces se crea
            PeriodoModel pe = new PeriodoModel();
            pe.setFechaFin(periodo.getFecha_fin());
            pe.setFechaInicio(periodo.getFecha_inicio());
            pe.setNombre(periodo.getNombre());
            pe.setGestion(gestion);
            pe.setUsuario(gestion.getUsuario());
            pe.setEstado(true);
            return periodoRepository.save(pe);
        }
        g.setFechaFin(periodo.getFecha_fin());
        g.setFechaInicio(periodo.getFecha_inicio());
        g.setNombre(periodo.getNombre());
        g.setEstado(true);
        return periodoRepository.save(g);
    }



    public Boolean isOverLappingAndIsNot(PeriodoValidate gestion) {
        List<PeriodoModel> gestiones = periodoRepository.findByIdNotAndGestionIdAndFechaInicioBetweenOrFechaFinBetween(
                gestion.getId(),
                gestion.getGestion_id(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin());
        return gestiones.size() > 0; //Si hay mas de 0 gestiones, entonces hay una que se solapa
    }
    public Boolean isOverLapping(PeriodoValidate gestion) {
        List<PeriodoModel> gestiones = periodoRepository.findByFechaInicioBetweenAndFechaFinBetweenAndGestionId(
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getGestion_id());
        return gestiones.size() > 0; //Si hay mas de 0 gestiones, entonces hay una que se solapa
    }
    public boolean eliminar(Long id, String username) {
        PeriodoModel g = periodoRepository.findById(id).get();
        if (g.getGestion().getUsuario().getUsername().equals(username)) {
            periodoRepository.delete(g);
            return true;
        }
        return false;
    }
    public PeriodoModel cerrar(Long id, String username) {
        PeriodoModel g = periodoRepository.findById(id).get();
        if (g.getGestion().getUsuario().getUsername().equals(username)) {
            g.setEstado(false);
            return periodoRepository.save(g);
        }
        return null;
    }
    public PeriodoModel un_periodo (Long id, String username) {
        PeriodoModel g = periodoRepository.findById(id).get();
        if (g.getGestion().getUsuario().getUsername().equals(username)) {
            return g;
        }
        return null;
    }

}
