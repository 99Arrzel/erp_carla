package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.config.JwtService;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    EmpresaService empresaService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @CrossOrigin
    @GetMapping("/mis_empresas")
    public ResponseEntity<List<EmpresaModel>> empresas(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.ok().body(empresaService.empresasUsuario(tokenGetUserService.username(authHeader)));
    }
    @CrossOrigin
    @PostMapping("/upsert")
    public ResponseEntity<?> upsert(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
    ) {
        Optional<EmpresaModel> e = empresaService.existeEmpresaNitOSigla(empresa.getNit(), empresa.getSigla());
        if (e.isPresent() && empresa.getId() == null) {
            return ResponseEntity.badRequest().body("Esa empresa ya existe con el mismo NIT o Sigla");
        }
            // Si el id de la empresa que se quiere guardar es igual al id de la empresa que se obtuvo de la base de datos, se edita
            return ResponseEntity.ok().body(empresaService.upsert(empresa, tokenGetUserService.username(authHeader)));
    }
    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<?> delete(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa

    ) {
        if(empresa.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la empresa");
        }
        return ResponseEntity.ok().body(empresaService.delete(empresa.getId(), tokenGetUserService.username(authHeader)));
    }
    @CrossOrigin
    @PostMapping("/una_empresa")
    public ResponseEntity<?> una_empresa(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EmpresaModel empresa
    ) {
        if(empresa.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la empresa");
        }
        return ResponseEntity.ok().body(empresaService.una_empresa(empresa.getId(), tokenGetUserService.username(authHeader)));
    }
}
