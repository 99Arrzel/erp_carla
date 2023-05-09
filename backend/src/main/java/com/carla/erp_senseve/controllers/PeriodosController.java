package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.models.PeriodoModel;
import com.carla.erp_senseve.services.GestionService;
import com.carla.erp_senseve.services.PeriodoService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.PeriodoValidate;
import com.carla.erp_senseve.validate.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/periodo")
public class PeriodosController {

  @Autowired
  PeriodoService periodoService;
  @Autowired
  GestionService gestionService;

  @Autowired
  TokenGetUserService tokenGetUserService;

  @PostMapping("upsert")
  public ResponseEntity<?>upsert(
      @RequestHeader("Authorization") String authHeader,
      @Valid@RequestBody PeriodoValidate periodo, BindingResult bindingResult
  ) {
    try{
      PeriodoModel p = periodoService.upsert(periodo, tokenGetUserService.username(authHeader));
      return ResponseEntity.ok().body(p);
    }
    catch (Exception e){
      return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
    }
  }
  @PostMapping("eliminar")
  public ResponseEntity<?>eliminar(
      @RequestHeader("Authorization") String authHeader,
      @RequestBody PeriodoValidate periodo, BindingResult bindingResult
  ) {
    try {
      periodoService.eliminar(periodo.getId(), tokenGetUserService.username(authHeader));
      return ResponseEntity.ok().body(new ResponseMessage("Periodo eliminado con éxito"));
    }
    catch (Exception e){
      return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
    }
  }
  @PostMapping("cerrar")
  public ResponseEntity<?> cerrar(
      @RequestHeader("Authorization") String authHeader,
      @RequestBody PeriodoValidate periodo, BindingResult bindingResult
  ) {
    try {
      PeriodoModel p = periodoService.cerrar(periodo.getId(), tokenGetUserService.username(authHeader));
      return ResponseEntity.ok().body(p);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
    }
  }
}
