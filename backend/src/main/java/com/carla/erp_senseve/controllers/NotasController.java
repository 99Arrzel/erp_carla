package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaCompraRequest;
import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaVentaRequest;
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

    //anular_compra
    @PostMapping(value = "/anular_compra")
    public ResponseEntity<?> anular_compra(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.anular_compra(
                            data.get("nota_id")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

    //crear_venta
    @PostMapping(value = "/crear_venta")
    public ResponseEntity<?> crear_venta(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CrearNotaVentaRequest data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.crear_venta(data)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

    @PostMapping(value = "/anular_venta")
    public ResponseEntity<?> anular_venta(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.anular_venta(
                            data.get("nota_id")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }


    @PostMapping(value = "/ultimo_numero")
    public ResponseEntity<?> ultimo_numero(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.ultimo_numero(
                            data.get("empresa_id"),
                            data.get("tipo")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }


    @PostMapping(value = "/una_nota_compra")
    public ResponseEntity<?> una_nota_compra(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.una_nota_compra(
                            data.get("nota_id")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

    @PostMapping(value = "/una_nota_venta")
    public ResponseEntity<?> una_nota_venta(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ) {
        try {
            return ResponseEntity.ok().body(
                    notasService.una_nota_venta(
                            data.get("nota_id")
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

}
