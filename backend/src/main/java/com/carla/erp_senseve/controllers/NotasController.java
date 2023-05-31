package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaCompraRequest;
import com.carla.erp_senseve.services.NotasService;
import com.carla.erp_senseve.services.TokenGetUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping("/api/notas")
public class NotasController {
    @Autowired

    NotasService notasService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    //listar

    @PostMapping(value = "/listar")
    public ResponseEntity<?> lista_notas(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(notasService.listar(
                    data.get("empresa_id"),
                    data.get("tipo")
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

    @PostMapping(value = "/crear_compra")
    public ResponseEntity<?> crear_compra(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CrearNotaCompraRequest data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.crear_compra(data)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
    //crear_compra
    //crear_venta
    //anular_compra
    //anular_venta
    //ultimo_numero
    //una_nota_compra
    //una_nota_venta

}
