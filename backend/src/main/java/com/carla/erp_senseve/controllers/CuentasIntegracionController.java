package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.services.CuentasIntegracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cuentas_integracion")
public class CuentasIntegracionController {

    @Autowired
    CuentasIntegracionService cuentasIntegracionService;

    @PostMapping(value = "/listar")
    public ResponseEntity<?> listar(
            @RequestBody Map<String, String> data,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            return ResponseEntity.ok().body(cuentasIntegracionService.listar(data.get("empresa_id")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }

    @PostMapping(value = "/integrar")
    public ResponseEntity<?> integrar(
            @RequestBody Map<String, String> data,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            return ResponseEntity.ok().body(cuentasIntegracionService.integrar(
                    data.get("empresa_id"),
                    data.get("cuenta_caja_id"),
                    data.get("cuenta_compras_id"),
                    data.get("cuenta_credito_fiscal_id"),
                    data.get("cuenta_debito_fiscal_id"),
                    data.get("cuenta_it_id"),
                    data.get("cuenta_it_por_pagar_id"),
                    data.get("cuenta_ventas_id"),
                    data.get("estado")
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
}
