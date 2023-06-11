package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.EmpresaMonedaModel;
import com.carla.erp_senseve.models.MonedaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.EmpresaMonedaRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.MonedaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaMonedaService {
    @Autowired
    EmpresaMonedaRepository empresaMonedaRepository;
    @Autowired
    MonedaRepository monedaRepository;
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    public List<EmpresaMonedaModel> por_empresa(Long empresa_id) {
        List<EmpresaMonedaModel> em = empresaMonedaRepository.findByEmpresaId(empresa_id);
        //WorkAround, el serializer por alguna razón no funciona con los objetos anidados
        em.forEach(
                (e) -> {
                    System.out.println(e.getMoneda_principal().getNombre());
                    if (e.getMoneda_principal() != null) {
                        e.setNombre_moneda_principal(e.getMoneda_principal().getNombre());
                    }
                    if (e.getMoneda_alternativa() != null) {
                        e.setNombre_moneda_alternativa(e.getMoneda_alternativa().getNombre());
                    }
                }
        );
        return em;
    }

    public EmpresaMonedaModel insertar(Long moneda_principal, Long moneda_alternativa, Float cambio, Long empresa_id, String usuario) {
        MonedaModel mp = monedaRepository.findById(moneda_principal).orElseThrow(
                () -> new RuntimeException("No existe moneda principal")
        );
        MonedaModel ma = monedaRepository.findById(moneda_alternativa).orElseThrow(
                () -> new RuntimeException("No existe moneda alternativa")
        );
        EmpresaModel em = empresaRepository.findById(empresa_id).orElseThrow(
                () -> new RuntimeException("No existe empresa")
        );
        UsuarioModel u = usuarioRepository.findByUsuario(usuario).orElseThrow(
                () -> new RuntimeException("No existe usuario")
        );
        EmpresaMonedaModel ultimo = empresaMonedaRepository.findFirstByEmpresaIdAndEstadoOrderByIdDesc(empresa_id, true);
        if (ultimo.getCambio() == cambio
                && ultimo.getMoneda_principal().getId() == moneda_principal
                && ultimo.getMoneda_alternativa().getId() == moneda_alternativa) {
            throw new RuntimeException("El cambio es igual al anterior");
        }
        //Todas las demás monedas a estado falso
        empresaMonedaRepository.setAllToFalse(empresa_id);
        EmpresaMonedaModel historial_add = new EmpresaMonedaModel();
        historial_add.setMoneda_principal(mp);
        historial_add.setMoneda_alternativa(ma);
        historial_add.setCambio(cambio);
        historial_add.setEmpresa(em);
        historial_add.setEstado(true);
        historial_add.setUsuario(u);
        return empresaMonedaRepository.save(historial_add);
    }

    public EmpresaMonedaModel ultimo_cambio(long empresaId) {
        return empresaMonedaRepository.findFirstByEmpresaIdAndEstadoOrderByIdDesc(empresaId, true);
    }
}
