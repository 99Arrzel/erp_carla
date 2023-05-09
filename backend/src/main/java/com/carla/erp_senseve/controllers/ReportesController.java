package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.ReporteBalanceInicialModel;
import com.carla.erp_senseve.models.ReporteComprobanteLibroDiarioModel;
import com.carla.erp_senseve.models.ReporteComprobanteLibroMayorModel;
import com.carla.erp_senseve.models.ReporteComprobanteModel;
import com.carla.erp_senseve.services.ReporteComprobanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reportes", consumes = MediaType.ALL_VALUE)
public class ReportesController {
    @Autowired
    ReporteComprobanteService reporteComprobanteService;
    @PostMapping(value = "/reporte_comprobante", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> reporte_comprobante(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Map<String, String> data
            ) {
        try{
            System.out.println(data.get("id"));
            ReporteComprobanteModel reporteComprobanteModel = reporteComprobanteService.reporte_comprobante(data.get("id"));
            return ResponseEntity.ok().body(reporteComprobanteModel);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
    @PostMapping(value = "/reporte-libro-diario", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> reporte_libro_diario(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Map<String, String> data
    ){
        try{
            System.out.println(data.get("id_moneda"));
            System.out.println(data.get("id_periodo"));
            System.out.println(data.get("todos"));
            ReporteComprobanteLibroDiarioModel reporte = reporteComprobanteService.reporte_libro_diario(data.get("id_moneda"), data.get("id_periodo"), data.get("todos"));
            return ResponseEntity.ok().body(reporte);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
    @PostMapping(value = "/reporte-libro-mayor", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> reporte_libro_mayor(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Map<String, String> data
    ){
        try{
            System.out.println(data.get("id_moneda"));
            System.out.println(data.get("id_periodo"));
            System.out.println(data.get("todos"));
            ReporteComprobanteLibroMayorModel reporte = reporteComprobanteService.reporte_libro_mayor(data.get("id_moneda"), data.get("id_periodo"), data.get("todos"));
            return ResponseEntity.ok().body(reporte);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
    @PostMapping(value = "/reporte-balance-inicial", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> reporte_balance_inicial(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam Map<String, String> data
    ){
        try{
            System.out.println(data.get("id_moneda"));
            System.out.println(data.get("id_gestion"));

            ReporteBalanceInicialModel reporteComprobanteModel = reporteComprobanteService.reporte_balance_inicial(data.get("id_moneda"), data.get("id_gestion"));
            return ResponseEntity.ok().body(reporteComprobanteModel);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage().toString());
        }
    }
}