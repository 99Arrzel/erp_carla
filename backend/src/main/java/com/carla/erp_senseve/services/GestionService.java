package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.repositories.GestionRepository;
import com.carla.erp_senseve.validate.GestionValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class GestionService {
    @Autowired
    GestionRepository gestionRepository;
    @Autowired
    EmpresaService empresaService;

    public GestionModel saveGestion(GestionModel gestion) {
        return gestionRepository.save(gestion);
    }

    public GestionModel updateGestion(GestionModel gestion) {
        //Fetch id
        GestionModel g = gestionRepository.findById(gestion.getId()).get();
        g.setFechaFin(gestion.getFechaFin());
        g.setFechaInicio(gestion.getFechaInicio());
        g.setNombre(gestion.getNombre());
        return gestionRepository.save(g);
    }
    public GestionModel cerrar(Long id, String username) {
        GestionModel g = gestionRepository.findById(id).get();
        if (g != null) {
            g.setEstado(false);
            return gestionRepository.save(g);
        }
        return null;
    }
    public List<GestionModel> getGestiones(Long empresaId) {
        return gestionRepository.findByEmpresaId(empresaId);
    }

    public List<GestionModel> getBetweenDates(Date fecha_inicio, Date fecha_fin, Long empresaId) {
        return gestionRepository.findByFechaInicioBetweenAndFechaFinBetweenAndEmpresaId(fecha_inicio, fecha_fin, fecha_inicio, fecha_fin, empresaId);
    }
    public GestionModel una_gestion(Long id, String username) {

        return gestionRepository.findById(id).get();
    }

    public GestionModel upsert(GestionValidate gestion, String username) {
        //Search empresa
        EmpresaModel empresa = empresaService.una_empresa(gestion.getEmpresa_id(), username);
        if (empresa == null) {
            return null;
        }
        GestionModel g = new GestionModel();

        if (gestion.getId() == null) { //Entonces se crea
            GestionModel ge = new GestionModel();
            ge.setFechaFin(gestion.getFecha_fin());
            ge.setFechaInicio(gestion.getFecha_inicio());
            ge.setNombre(gestion.getNombre());
            ge.setEmpresa(empresa);
            ge.setUsuario(empresa.getUsuario());
            ge.setEstado(true);
            return gestionRepository.save(ge);
        }
        g.setFechaFin(gestion.getFecha_fin());
        g.setFechaInicio(gestion.getFecha_inicio());
        g.setNombre(gestion.getNombre());
        g.setEstado(true);
        return gestionRepository.save(g);
    }
    public Boolean isOverLappingAndIsNot(GestionValidate gestion) {
        List<GestionModel> gestiones = gestionRepository.findByIdNotAndEmpresaIdAndFechaInicioBetweenOrFechaFinBetween(
                gestion.getId(),
                gestion.getEmpresa_id(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin());
        return gestiones.size() > 0; //Si hay mas de 0 gestiones, entonces hay una que se solapa
    }

    public Boolean isOverLapping(GestionValidate gestion) {
        List<GestionModel> gestiones = gestionRepository.findByFechaInicioBetweenAndFechaFinBetweenAndEmpresaId(
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getFecha_inicio(),
                gestion.getFecha_fin(),
                gestion.getEmpresa_id());
        return gestiones.size() > 0; //Si hay mas de 0 gestiones, entonces hay una que se solapa
    }

    public boolean eliminar(Long id, String username) {
        GestionModel g = gestionRepository.findById(id).get();
        if (g.getEmpresa().getUsuario().getUsername().equals(username)) {
            gestionRepository.delete(g);
            return true;
        }
        return false;
    }
}
