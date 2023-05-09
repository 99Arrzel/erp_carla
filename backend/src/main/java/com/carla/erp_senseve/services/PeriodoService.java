package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.models.PeriodoModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.PeriodoRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import com.carla.erp_senseve.validate.PeriodoValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoService {
  @Autowired
  private PeriodoRepository periodoRepository;
  @Autowired
  private GestionService gestionService;
  @Autowired
  private UsuarioRepository usuarioRepository;



  public PeriodoModel upsert(PeriodoValidate periodo, String username) {
    //Search empresa
    GestionModel gestion = gestionService.una_gestion(periodo.getGestion_id(), username).orElseThrow(
        () -> new RuntimeException("No existe esa gestion")
    );
    if (!gestion.getEstado()) {
      throw new RuntimeException("No se puede editar periodos de una gestion cerrada");
    }

    if (periodo.getFecha_inicio().after(periodo.getFecha_fin())) {
      throw new RuntimeException("La fecha de inicio no puede ser mayor a la fecha de fin");
    }
    if (periodo.getFecha_inicio().before(gestion.getFechaInicio()) || periodo.getFecha_fin().after(gestion.getFechaFin())) {
      throw new RuntimeException("El periodo no puede estar fuera de la gestion " +
          "Inicio: " + gestion.getFechaInicio() + " Fin: " + gestion.getFechaFin());
    }
    if (periodo.getId() != null) {
      List<PeriodoModel> periodosNombre = periodoRepository.findByGestionIdAndNombreAndIdNot(periodo.getGestion_id(), periodo.getNombre(), periodo.getId());
      if (periodosNombre.size() > 0) {
        throw new RuntimeException("Ya existe un periodo con ese nombre");
      }
      PeriodoModel p = periodoRepository.findById(periodo.getId()).orElseThrow(
          () -> new RuntimeException("No existe ese periodo")
      );
      if (!p.getUsuario().getUsuario().equals(username)) {
        throw new RuntimeException("No tienes permiso para editar este periodo");
      }
      List<PeriodoModel> periodos = periodoRepository.isOverlappingAndIsNot(periodo.getId(), periodo.getGestion_id(), periodo.getFecha_inicio(), periodo.getFecha_fin());
      if (periodos.size() > 0) {
        throw new RuntimeException("El periodo se solapa con otro periodo");
      }
      p = periodoRepository.findById(periodo.getId()).get();
      p.setFechaInicio(periodo.getFecha_inicio());
      p.setFechaFin(periodo.getFecha_fin());
      p.setNombre(periodo.getNombre());
      return periodoRepository.save(p);
    }
    List<PeriodoModel> periodosNombre = periodoRepository.findByGestionIdAndNombre(periodo.getGestion_id(), periodo.getNombre());
    if (periodosNombre.size() > 0) {
      throw new RuntimeException("Ya existe un periodo con ese nombre");
    }

    List<PeriodoModel> periodos = periodoRepository.isOverlapping(periodo.getGestion_id(), periodo.getFecha_inicio(), periodo.getFecha_fin());
    if (periodos.size() > 0) {
      throw new RuntimeException("El periodo se solapa con otro periodo");
    }
    PeriodoModel p = new PeriodoModel();
    p.setFechaInicio(periodo.getFecha_inicio());
    p.setFechaFin(periodo.getFecha_fin());
    p.setNombre(periodo.getNombre());
    p.setGestion(gestion);
    p.setEstado(true);
    p.setUsuario(gestion.getUsuario());
    return periodoRepository.save(p);
  }
  public Boolean eliminar(Long id, String username) {
    PeriodoModel g = periodoRepository.findById(id).orElseThrow(
        () -> new RuntimeException("No existe ese periodo")
    );
    if(g.getGestion().getEstado() == false) throw new RuntimeException("No se puede eliminar un periodo de una gestion cerrada");
    if(g.getEstado() == false) throw new RuntimeException("No se puede eliminar un periodo cerrado");
    if (g.getGestion().getUsuario().getUsername().equals(username)) {
      periodoRepository.delete(g);
      return true;
    }
    throw new RuntimeException("No tienes permiso para eliminar este periodo");
  }
  public PeriodoModel cerrar(Long id, String username) {
    PeriodoModel g = periodoRepository.findById(id).orElseThrow(
        () -> new RuntimeException("No existe ese periodo")
    );
    if (g.getGestion().getUsuario().getUsername().equals(username)) {
      g.setEstado(false);
      return periodoRepository.save(g);
    }
    throw new RuntimeException("No tienes permiso para cerrar este periodo");
  }
  public Optional<PeriodoModel> un_periodo(Long id, String username) {
    Optional<PeriodoModel> g = periodoRepository.findById(id);
    Optional<UsuarioModel> u = usuarioRepository.findByUsuario(username);
    if (g.isEmpty()) return Optional.empty();
    if (g.get().getUsuario().getId() == u.get().getId()) {
      return g;
    }
    return Optional.empty();
  }

    public boolean hayPeriodoAbiertoEnEmpresaYFecha(Long empresaId, Date fecha) {
      PeriodoModel existe = periodoRepository.hayPeriodoAbiertoEnEmpresaYFecha(empresaId, fecha);
        if(existe != null) return true;
        return false;
    }

    public PeriodoModel obtenerPeriodo(Long id) {
    var periodo = periodoRepository.findByElId(id);
    if(periodo == null){
      throw new RuntimeException("No existe el periodo");
    }
        return periodo;
    }
}
