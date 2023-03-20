package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.models.CuentaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.services.CuentaService;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.CuentaValidate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {
    @Autowired
    CuentaService cuentaService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    EmpresaService empresaService;
    @CrossOrigin
    @PostMapping("por_empresa")
    public ResponseEntity<?> por_empresa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
            ) {
        if(empresa.getId() == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la empresa");
        }
        return ResponseEntity.ok().body(cuentaService.getCuentas(empresa.getId()));
    }
    @CrossOrigin
    @PostMapping("upsert")
    public ResponseEntity<?> upsert(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CuentaValidate c
            ) {
        EmpresaModel empresa = empresaService.una_empresa(c.getEmpresa_id(), tokenGetUserService.username(authHeader));

        if(empresa == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la empresa");
        }
        if(c.getId() != null) { // update
            if(c.getNombre() == null || c.getNombre().isEmpty()){
                return ResponseEntity.badRequest().body("El nombre de la cuenta no puede estar vacío");
            }

            return ResponseEntity.ok().body(cuentaService.updateCuenta(c.getId(), c.getNombre()));
        }
        var x = cuentaService.saveCuenta(c.getNombre(), c.getPadre_id() , empresa.getId(), tokenGetUserService.username(authHeader));
        return ResponseEntity.ok().body(x);


    }
}
