package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.EmpresaMonedaModel;
import com.carla.erp_senseve.models.MonedaModel;
import com.carla.erp_senseve.repositories.EmpresaMonedaRepository;
import com.carla.erp_senseve.repositories.MonedaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonedaService {
  @Autowired
  MonedaRepository monedaRepository;
  @Autowired
  UsuarioRepository usuarioRepository;
  @Autowired
    EmpresaMonedaRepository empresaMonedaRepository;
  public List<MonedaModel> monedasUsuario(String usuario) {
      Long idUsuario = usuarioRepository.findByUsuario(usuario).orElseThrow(
              () -> new RuntimeException("No existe usuario")
      ).getId();
      return monedaRepository.findByUsuarioId(idUsuario);
  }
  //Por id
    public MonedaModel monedaUsuario(Long id) {
        return monedaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No existe moneda")
        );
    }

    public MonedaModel obtenerMoneda(Long id) {
        return monedaRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No existe moneda")
        );
    }

    public MonedaModel obtenerMonedaEmpresa(Long id_empresa) {
      //Devuelve la moneda consultando empresas_monedas, buscar donde estado sea true, de esa tabla el id de moneda_principal_id
        EmpresaMonedaModel empresaMonedaModel = empresaMonedaRepository.findByIdEmpresaYEstadoTrue(id_empresa);
        if(empresaMonedaModel == null) throw new RuntimeException("No existe moneda principal para la empresa");

        return monedaRepository.findById(empresaMonedaModel.getMoneda_principal_id()).orElseThrow(
                () -> new RuntimeException("No existe moneda")
        );
    }
}
