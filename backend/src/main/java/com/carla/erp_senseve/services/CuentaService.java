package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.CuentaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.CuentaRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return cuentaRepository.findByPadreId(id);
    }
    public CuentaModel una_cuenta(Long id){
        return cuentaRepository.findById(id).get();
    }

    public CuentaModel updateCuenta(Long cuenta_id, String nombre){
        CuentaModel cuenta = cuentaRepository.findById(cuenta_id).get();
        if(cuenta != null){
            cuenta.setNombre(nombre);
            return cuentaRepository.save(cuenta);
        }
        return null;
    }
    public CuentaModel saveCuenta(String nombre, Long padre_id, Long empresa_id, String username){
        Integer hermanos = 0;
        if(padre_id == null || padre_id == 0) {
            hermanos = cuentaRepository.countByEmpresaIdAndPadreIsNull(empresa_id);
        }else{
            hermanos = cuentaRepository.countByEmpresaIdAndPadreId(empresa_id, padre_id);
            if(cuentaRepository.findById(padre_id).get() == null){
                return null;
            }
        }
        EmpresaModel empresa = empresaRepository.findById(empresa_id).get();
        UsuarioModel usuario = usuarioRepository.findByUsuario(username).get();
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
            return null;
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

        System.out.println(cuenta.getCodigo());

        return cuentaRepository.save(cuenta);
    }
}
