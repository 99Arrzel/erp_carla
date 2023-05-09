package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.models.ComprobanteRequest;
import com.carla.erp_senseve.services.ComprobanteService;
import com.carla.erp_senseve.validate.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comprobante")
public class ComprobanteController {
    @Autowired
    ComprobanteService comprobanteService;
    @PostMapping("crear")
    public ResponseEntity<?> crear(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ComprobanteRequest data
    ) {
        System.out.println(data);
        try {
            comprobanteService.crear(data, authHeader);
            return ResponseEntity.ok().body(new ResponseMessage("Comprobante creado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @GetMapping("listar/{empresa_id}")
    public ResponseEntity<?> listar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("empresa_id") String empresa_id
    ) {
        try {
            return ResponseEntity.ok().body(comprobanteService.listar(empresa_id, authHeader));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @GetMapping("/ultimo_numero/{empresa_id}")
    public ResponseEntity<?> ultimo_numero(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("empresa_id") Long empresa_id
    ) {
        try {
            return ResponseEntity.ok().body(comprobanteService.ultimo_numero(empresa_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @GetMapping("/cerrar/{id}")
    public ResponseEntity<?> cerrar(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long id
    ) {
        try {
            return ResponseEntity.ok().body(comprobanteService.cerrar(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @GetMapping("/anular/{id}")
    public ResponseEntity<?> anular(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") Long id
    ) {
        try {
            return ResponseEntity.ok().body(comprobanteService.anular(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }

}
