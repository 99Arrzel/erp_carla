package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.*;
import com.carla.erp_senseve.repositories.EmpresaMonedaRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.MonedaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {
  @Autowired
  EmpresaRepository empresaRepository;
  @Autowired
  UsuarioRepository usuarioRepository;
  @Autowired
  MonedaRepository monedaRepository;
  @Autowired
  EmpresaMonedaRepository empresaMonedaRepository;
  @Autowired
  CuentaService cuentaService;
  public Optional<EmpresaModel> delete(Long id, String usuario) {
    Optional<UsuarioModel> u = usuarioRepository.findByUsuario(usuario);
    if (u.isPresent()) {
      EmpresaModel e = empresaRepository.findById(id).orElse(null);
      if (e != null) {
        e.setEstado(false);
        return Optional.of(empresaRepository.save(e));
      }
    }
    return Optional.empty();
  }
  public List<EmpresaModel> empresasUsuario(String usuario) {
    Optional<UsuarioModel> u = usuarioRepository.findByUsuario(usuario);
    if (u.isPresent()) {
      return empresaRepository.findByUsuarioIdAndEstado(u.get().getId(), true);
    }
    return null;
  }
  public EmpresaModel upsert(EmpresaModel empresa, String usuario, Long coin_id) {
    if(coin_id == null){
      throw new RuntimeException("No se ha enviado el id de la moneda");
    }
    Long usuario_id = usuarioRepository.findByUsuario(usuario).orElseThrow(
            () -> new RuntimeException("No existe usuario")
    ).getId();
    MonedaModel moneda = monedaRepository.findById(coin_id).orElseThrow(
            () -> new RuntimeException("No existe moneda")
    );
    if(empresa.getNit() == null || empresa.getNit().isBlank()){
      throw new RuntimeException("El nit es obligatorio");
    }
    if(empresa.getNombre() == null || empresa.getNombre().isBlank()){
      throw new RuntimeException("El nombre es obligatorio");
    }
    if(empresa.getSigla() == null || empresa.getSigla().isBlank()){
      throw new RuntimeException("La sigla es obligatoria");
    }
    UsuarioModel us = usuarioRepository.findByUsuario(usuario).orElseThrow(
            () -> new RuntimeException("No existe usuario")
    );

    if(empresa.getId() != null){
      Optional<EmpresaModel> emp = empresaRepository.findById(empresa.getId());
      if(emp.isEmpty()) {
        throw new RuntimeException("No existe empresa");
      }
      List<EmpresaModel> checkNitOSigla = empresaRepository.findByNitOrSiglaOrNombreAndEstado(empresa.getNit(), empresa.getSigla(), empresa.getNombre(), empresa.getId());
      if(checkNitOSigla.size() > 1 && checkNitOSigla.get(0).getId() != empresa.getId()){
        throw new RuntimeException("Ya existe una empresa con el mismo nit, sigla o nombre " + checkNitOSigla.get(0).getId() + " " + checkNitOSigla.get(0).getNombre());
      }
      List<EmpresaMonedaModel> monedas = empresaMonedaRepository.findByEmpresaId(empresa.getId());
      if(monedas.size() > 1 && monedas.get(0).getId() != coin_id){
        throw new RuntimeException("Ya existe registros de monedas");
      }
      empresaMonedaRepository.deleteAllByEmpresaId(empresa.getId());
      EmpresaModel e = emp.get();
      e.setUsuario(us);
      e.setNombre(empresa.getNombre());
      e.setNit(empresa.getNit());
      e.setSigla(empresa.getSigla());
      e.setCorreo(empresa.getCorreo());
      e.setDireccion(empresa.getDireccion());
      e.setTelefono(empresa.getTelefono());
      e.setEstado(empresa.getEstado() == null || empresa.getEstado()); //Default true
      EmpresaModel eSaved = empresaRepository.save(e);

      EmpresaMonedaModel defaultMoneda = new EmpresaMonedaModel();
      defaultMoneda.setMoneda_principal(moneda);
      defaultMoneda.setEmpresa(eSaved);
      defaultMoneda.setUsuario(us);
      defaultMoneda.setEstado(true);
      defaultMoneda.setCambio(null);
      defaultMoneda.setMoneda_alternativa(null);
      empresaMonedaRepository.save(defaultMoneda);

      return eSaved;
    }else{
      List<EmpresaModel> checkNitOSigla = empresaRepository.findByNitOrSiglaOrNombreAndEstado(empresa.getNit(), empresa.getSigla(), empresa.getNombre());
      if(checkNitOSigla.size() > 0){
        throw new RuntimeException("Ya existe una empresa con el mismo nit o sigla o nombre " + checkNitOSigla.get(0).getId() + " " + checkNitOSigla.get(0).getNombre());
      }
      empresa.setUsuario(us);
      empresa.setEstado(true);
      EmpresaModel eSaved = empresaRepository.save(empresa);

      EmpresaMonedaModel defaultMoneda = new EmpresaMonedaModel();
      defaultMoneda.setMoneda_principal(moneda);
      defaultMoneda.setEmpresa(empresa);
      defaultMoneda.setUsuario(us);
      defaultMoneda.setEstado(true);
      defaultMoneda.setCambio(null);
      defaultMoneda.setMoneda_alternativa(null);
      empresaMonedaRepository.save(defaultMoneda);
      //Crear las primeras cuentas de la empresa
      cuentaService.saveCuenta("Activo", 0L, empresa.getId(), usuario);
      cuentaService.saveCuenta("Pasivo", 0L, empresa.getId(), usuario);
      cuentaService.saveCuenta("Patrimonio", 0L, empresa.getId(), usuario);
      cuentaService.saveCuenta("Ingresos", 0L, empresa.getId(), usuario);
      CuentaModel egresos = cuentaService.saveCuenta("Egresos", 0L, empresa.getId(), usuario);
      cuentaService.saveCuenta("Costos", egresos.getId(), empresa.getId(), usuario);
      cuentaService.saveCuenta("Gastos", egresos.getId(), empresa.getId(), usuario);
      return eSaved;
    }
  }
  public Optional<EmpresaModel> una_empresa(Long id, String username) {
    Optional<UsuarioModel> u = usuarioRepository.findByUsuario(username);
    if (u.isPresent()) {
      Optional<EmpresaModel> e = empresaRepository.findById(id);
      if (e.isPresent()) {
        e.get().getUsuario();
        return e;
      }
      return Optional.empty();
    }
    return Optional.empty();
  }

  public EmpresaModel findById(long empresaId) {
    return empresaRepository.findById(empresaId).orElseThrow(
            () -> new RuntimeException("No existe empresa")
    );
  }
}
