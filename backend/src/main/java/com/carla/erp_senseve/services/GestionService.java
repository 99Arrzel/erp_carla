package com.carla.erp_senseve.services;
import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.GestionRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import com.carla.erp_senseve.validate.GestionValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
@Service
public class GestionService {
  @Autowired
  GestionRepository gestionRepository;
  @Autowired
  EmpresaService empresaService;
  @Autowired
  UsuarioRepository usuarioRepository;
  public Optional<GestionModel> cerrar(Long id, String username) {
    GestionModel g = gestionRepository.findById(id).get();
    if (g != null) {
      g.setEstado(false);
      return Optional.of(gestionRepository.save(g));
    }
    return Optional.empty();
  }
  public Optional<GestionModel> una_gestion(Long id, String username) {
    return gestionRepository.findById(id);
  }
  public GestionModel obtenerGestion(Long id){
    return gestionRepository.findById(id).orElseThrow(
      () -> new RuntimeException("No existe gestion")
  );
  }
  public GestionModel upsert(GestionValidate gestion, String username) {
    UsuarioModel e = usuarioRepository.findByUsuario(username).orElseThrow(
        () -> new RuntimeException("No existe usuario")
    );
    List<GestionModel> abiertas = gestionRepository.findByEmpresaIdAndEstado(gestion.getEmpresa_id(), true);

    if(abiertas.size() > 1 && gestion.getId() == null ){
      throw new RuntimeException("No pueden existir más de 2 gestiones abiertas");
    }
    if(gestion.getNombre() == null){
      throw new RuntimeException("El nombre es obligatorio");
    }
    if(gestion.getFecha_inicio() == null){
      throw new RuntimeException("La fecha de inicio es obligatoria");
    }
    if(gestion.getFecha_fin() == null){
      throw new RuntimeException("La fecha de fin es obligatoria");
    }
    if(gestion.getId() != null){
      GestionModel ges = gestionRepository.findById(gestion.getId()).orElseThrow(
          () -> new RuntimeException("No existe gestion")
      );
      if(ges.getEstado() == false){
        throw new RuntimeException("No se puede editar una gestion cerrada");
      }
      GestionModel checkNombre = gestionRepository.findByNombreAndEmpresaId(gestion.getNombre(), gestion.getEmpresa_id(), gestion.getId()).orElse(null);
      if(checkNombre != null){
        throw new RuntimeException("Ya existe una gestion con el mismo nombre");
      }
      List<GestionModel> checkOverlap = gestionRepository.isOverlappingAndIsNot(gestion.getId(), gestion.getEmpresa_id(), gestion.getFecha_inicio(), gestion.getFecha_fin());
      if(checkOverlap.size() > 0){
        throw new RuntimeException("La fecha de inicio o fin de la gestión se solapa con otra gestión");
      }
      ges.setNombre(gestion.getNombre());
      ges.setFechaInicio(gestion.getFecha_inicio());
      ges.setFechaFin(gestion.getFecha_fin());
      ges.setEmpresa(empresaService.una_empresa(gestion.getEmpresa_id(), username).get());
      ges.setUsuario(e);
      ges.setEstado(true);
      return gestionRepository.save(ges);
    }
    GestionModel checkNombre = gestionRepository.findByNombreAndEmpresaId(gestion.getNombre(), gestion.getEmpresa_id()).orElse(null);
    if(checkNombre != null){
      throw new RuntimeException("Ya existe una gestion con el mismo nombre");
    }
    List<GestionModel> checkOverlap = gestionRepository.isOverlapping(gestion.getEmpresa_id(), gestion.getFecha_inicio(), gestion.getFecha_fin());
    if(checkOverlap.size() > 0){
      throw new RuntimeException("La fecha de inicio o fin de la gestión se solapa con otra gestión");
    }
    GestionModel g = new GestionModel();
    g.setNombre(gestion.getNombre());
    g.setFechaInicio(gestion.getFecha_inicio());
    g.setFechaFin(gestion.getFecha_fin());
    g.setEmpresa(empresaService.una_empresa(gestion.getEmpresa_id(), username).get());
    g.setUsuario(e);
    g.setEstado(true);
    return gestionRepository.save(g);
  }
  public boolean eliminar(Long id, String username) {
    Optional<GestionModel> g = gestionRepository.findById(id);
    if (g.isEmpty()) {
      return false;
    }
    if(g.get().getEstado() == false){
      throw new RuntimeException("No se puede eliminar una gestion cerrada");
    }
    gestionRepository.delete(g.get());
    return true;
    //}

  }

  public GestionModel gestionEnFechaConIdEmpresa(Date fecha, Long empresaId) {
    GestionModel ges = gestionRepository.gestionEnFechaConIdEmpresa(fecha, empresaId);
    return ges;
  }
}
