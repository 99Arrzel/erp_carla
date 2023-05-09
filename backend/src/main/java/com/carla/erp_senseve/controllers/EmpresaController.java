package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {
    @Autowired
    EmpresaService empresaService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @GetMapping("/mis_empresas")
    public ResponseEntity<List<EmpresaModel>> empresas(
            @RequestHeader("Authorization") String authHeader
    ) {
        List<EmpresaModel> empresas = empresaService.empresasUsuario(tokenGetUserService.username(authHeader));

        return ResponseEntity.ok().body(empresas);
    }
    @PostMapping("/upsert")
    public ResponseEntity<?> upsert(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody  Map<String,String> body
    ) {
        //Cast string to long
        try {
            EmpresaModel empresa = new EmpresaModel();
            if(body.get("id") != null){
                empresa.setId(Long.parseLong(body.get("id")));
            }
            empresa.setNombre(body.get("nombre"));
            empresa.setNiveles(Integer.parseInt(body.get("niveles")));
            empresa.setSigla(body.get("sigla"));
            empresa.setCorreo(body.get("correo"));
            empresa.setDireccion(body.get("direccion"));
            empresa.setTelefono(body.get("telefono"));
            empresa.setNit(body.get("nit"));
            //
            if(body.get("moneda_id") == null){
                throw new Exception("No se ha enviado el id de la moneda");
            }
            return ResponseEntity.ok().body(empresaService.upsert(empresa, tokenGetUserService.username(authHeader), Long.parseLong(body.get("moneda_id"))));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));

        }
    }
    @PostMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
    ) {
        if (empresa.getId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la empresa"));
        }
        Optional<EmpresaModel> e = empresaService.delete(empresa.getId(), tokenGetUserService.username(authHeader));
        if (e.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la empresa"));
        }
        return ResponseEntity.ok().body(e.get());
    }
    @PostMapping("/una_empresa")
    public ResponseEntity<?> una_empresa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
    ) {
        if (empresa.getId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la empresa"));
        }
        Optional<EmpresaModel> ep = empresaService.una_empresa(empresa.getId(), tokenGetUserService.username(authHeader));

        if (ep.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la empresa"));
        }
        return ResponseEntity.ok().body(ep.get());
    }
}
