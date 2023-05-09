package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.EmpresaMonedaModel;
import com.carla.erp_senseve.services.EmpresaMonedaService;
import com.carla.erp_senseve.services.MonedaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moneda_empresa")
public class EmpresaMonedaController {
  @Autowired
  MonedaService monedaService;
  @Autowired
  EmpresaMonedaService empresaMonedaService;
  @Autowired
  TokenGetUserService tokenGetUserService;


  @PostMapping("por_empresa")
  public ResponseEntity<?> por_empresa(@RequestHeader("Authorization") String authHeader, @RequestBody EmpresaModel empresa) {
    try {
      List<EmpresaMonedaModel> empresaMonedaModels = empresaMonedaService.por_empresa(empresa.getId());
      System.out.println("Monedas");
      return ResponseEntity.ok().body(empresaMonedaModels);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
    }
  }
  @PostMapping("insertar")
  public ResponseEntity<?> por_empresa(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String,String> body) {
    try {
      Long moneda_principal_id = Long.parseLong(body.get("moneda_principal_id"));
      Long moneda_alternativa_id = Long.parseLong(body.get("moneda_alternativa_id"));
      Float cambio = Float.parseFloat(body.get("cambio"));
      Long empresa_id = Long.parseLong(body.get("empresa_id"));
      String usuario = tokenGetUserService.username(authHeader);
      EmpresaMonedaModel empresaMonedaModel = empresaMonedaService.insertar(moneda_principal_id, moneda_alternativa_id, cambio, empresa_id, usuario);
      return ResponseEntity.ok().body(empresaMonedaModel);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
    }
  }
}
