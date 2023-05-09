package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.services.MonedaService;
import com.carla.erp_senseve.services.TokenGetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/moneda")
public class MonedaController {
  @Autowired
  MonedaService monedaService;
  @Autowired
  TokenGetUserService tokenGetUserService;
  @GetMapping("/usuario")
  public ResponseEntity<?> porUsuario(
      @RequestHeader("Authorization") String authHeader
  ) {
    return ResponseEntity.ok().body(monedaService.monedasUsuario(tokenGetUserService.username(authHeader)));
  }
}
