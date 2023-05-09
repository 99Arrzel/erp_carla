package com.carla.erp_senseve.controllers;
import com.carla.erp_senseve.models.CuentaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.services.CuentaService;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.CuentaValidate;
import com.carla.erp_senseve.validate.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaController {
    @Autowired
    CuentaService cuentaService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    EmpresaService empresaService;

    @PostMapping("/por_empresa")
    public ResponseEntity<?> por_empresa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
            ) {
        if(empresa.getId() == null){
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la empresa"));
        }
        Optional<EmpresaModel> e = empresaService.una_empresa(empresa.getId(), tokenGetUserService.username(authHeader));
        if(e.isEmpty()){
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la empresa"));
        }
        if(e.get().getEstado() == false){
            return ResponseEntity.badRequest().body(new ResponseMessage("La empresa no está activa"));
        }
        return ResponseEntity.ok().body(cuentaService.getCuentas(empresa.getId()));
    }
    @GetMapping("/solo_detalle/{id}")
    public ResponseEntity<?> solo_detalle(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long id
    ){
        try{
            return ResponseEntity.ok().body(cuentaService.solo_detalle(id));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @PostMapping("/upsert")
    public ResponseEntity<?> upsert(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CuentaValidate c
            ) {
        try {
            if (c.getId() != null) { // update
                cuentaService.updateCuenta(c.getId(), c.getNombre());
                return ResponseEntity.ok().body(new ResponseMessage("Cuenta actualizada"));
            } else {
                cuentaService.saveCuenta(c.getNombre(), c.getPadre_id(), c.getEmpresa_id(), tokenGetUserService.username(authHeader));
                return ResponseEntity.ok().body(new ResponseMessage("Cuenta creada"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @PostMapping("/eliminar")
    public ResponseEntity<?> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CuentaModel c
            ) {
        try{
            cuentaService.deleteCuenta(c.getId());
            return ResponseEntity.ok().body(new ResponseMessage("Cuenta eliminada"));
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
}
