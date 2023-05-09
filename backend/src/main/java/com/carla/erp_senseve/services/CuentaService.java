package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.CuentaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.CuentaRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {
  @Autowired
  CuentaRepository cuentaRepository;

  @Autowired
  EmpresaRepository empresaRepository;
  @Autowired
  UsuarioRepository usuarioRepository;
  public List<CuentaModel> getCuentas(Long empresa_id){
    return cuentaRepository.findByEmpresaId(empresa_id);
  }
  public List<CuentaModel> getChildren(Long id){
    return cuentaRepository.findByPadre_id(id);
  }

  public Optional<CuentaModel> una_cuenta(Long id){
    return cuentaRepository.findById(id);
  }

  public CuentaModel updateCuenta(Long cuenta_id, String nombre){
    if(nombre == null || nombre.isBlank()){
      throw new RuntimeException("El nombre es obligatorio");
    }

    CuentaModel cuenta = cuentaRepository.findById(cuenta_id).orElseThrow(
        () -> new RuntimeException("Cuenta no encontrada")
    );
    List<CuentaModel> cuentasNombre = cuentaRepository.findByEmpresaIdAndNombreAndIdNot(cuenta.getEmpresa().getId(), nombre, cuenta_id);
    if(cuentasNombre.size() > 0){
      throw new RuntimeException("Ya existe una cuenta con ese nombre");
    }
    cuenta.setNombre(nombre);
    return cuentaRepository.save(cuenta);

  }
  public boolean deleteCuenta (Long id){
    CuentaModel cuenta = cuentaRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Cuenta no encontrada")
    );
   cuentaRepository.delete(cuenta);
    return true;
  }
  public CuentaModel saveCuenta(String nombre, Long padre_id, Long empresa_id, String username){
    if (nombre == null || nombre.isBlank()){
      throw new RuntimeException("El nombre es obligatorio");
    }
    if(empresa_id == null || empresa_id == 0){
      throw new RuntimeException("La empresa es obligatoria");
    }
    //Check otras cuentas de la empresa
    List<CuentaModel> cuentasNombre = cuentaRepository.findByEmpresaIdAndNombre(empresa_id, nombre);
    if(cuentasNombre.size() > 0){
      throw new RuntimeException("Ya existe una cuenta con ese nombre");
    }
    Integer hermanos = 0;
    if(padre_id == null || padre_id == 0) {
      hermanos = cuentaRepository.countByEmpresaIdAndPadreIsNull(empresa_id);
    }else{
      hermanos = cuentaRepository.countByEmpresaIdAndPadreId(empresa_id, padre_id);
      if(cuentaRepository.findById(padre_id).get() == null){
        throw new RuntimeException("Padre no encontrado");
      }
    }
    EmpresaModel empresa = empresaRepository.findById(empresa_id).orElseThrow(
        () -> new RuntimeException("Empresa no encontrada")
    );
    UsuarioModel usuario = usuarioRepository.findByUsuario(username).orElseThrow(
        () -> new RuntimeException("Usuario no encontrado")
    );
    CuentaModel padre = cuentaRepository.findById(padre_id).orElse(null);
    CuentaModel cuenta = new CuentaModel();
    cuenta.setNombre(nombre);
    cuenta.setEmpresa(empresa);
    cuenta.setUsuario(usuario);
    cuenta.setPadre(padre); //Padre puede ser null
    cuenta.setCodigo("");
    cuenta.setNivel(1);
    if(padre != null){
      cuenta.setNivel(padre.getNivel() + 1);
      cuenta.setCodigo(padre.getCodigo());
    }
    if(cuenta.getNivel() > empresa.getNiveles()){
      throw new RuntimeException("Nivel maximo alcanzado");
    }
    var max = empresa.getNiveles() ;
    if(cuenta.getCodigo() ==  ""){
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < max; i++){
        sb.append("0");
      }
      cuenta.setCodigo(sb.toString().replace("", "."));
    }
    String[] codigoArr = cuenta.getCodigo().split("\\.");
    List<String> codigoList = new ArrayList<>(Arrays.asList(codigoArr));
    for(int index = 0; index < codigoArr.length; index++){
      if(index == cuenta.getNivel() - 1 ){
        System.out.println(codigoList + " BEFORE");
        codigoList.removeIf(String::isEmpty);
        System.out.println(hermanos + " Hermanos size");
        codigoList.set(index, Integer.toString(hermanos +1));
        System.out.println(codigoList + " AFTER");
      }
    }
    //Trim empty strings
    cuenta.setCodigo(String.join(".", codigoList));
    cuenta.setTipo((cuenta.getNivel() == max) ? "Detalle" : "Global");
    return cuentaRepository.save(cuenta);
  }

  public Object solo_detalle(Long id_empresa) {
    List<CuentaModel> cuentas = cuentaRepository.findByEmpresaIdYEsDetalle(id_empresa);
    return cuentas;
  }

  public CuentaModel findById(long cuentaId) {
    return cuentaRepository.findById(cuentaId).orElseThrow(
        () -> new RuntimeException("Cuenta no encontrada")
    );
  }

    public List<CuentaModel> getCuentasActivoPasivoPatrimonio(Long id_empresa) {
      List<CuentaModel> cuentas = cuentaRepository.findByEmpresaIdAndActivoAndPasivoAndPatrimonio(id_empresa);
        return cuentas;
    }

  public List<CuentaModel> obtenerCuentasPorEmpresa(Long id) {
    return cuentaRepository.findByEmpresaId(id);
  }
}