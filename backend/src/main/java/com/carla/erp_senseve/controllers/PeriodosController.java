package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.models.PeriodoModel;
import com.carla.erp_senseve.services.GestionService;
import com.carla.erp_senseve.services.PeriodoService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.PeriodoValidate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/periodo")
public class PeriodosController {

    @Autowired
    PeriodoService periodoService;
    @Autowired
    GestionService gestionService;

    @Autowired
    TokenGetUserService tokenGetUserService;
    @CrossOrigin
    @PostMapping("upsert")
    public ResponseEntity<?>upsert(
            @RequestHeader("Authorization") String authHeader,
            @Valid@RequestBody PeriodoValidate periodo, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        if(periodo.getFecha_fin().before(periodo.getFecha_inicio())){
            return ResponseEntity.badRequest().body("La fecha de fin no puede ser menor a la fecha de inicio");
        }
        if(periodo.getId() == null){
            //check range
            if(periodoService.isOverLapping(periodo)){
                return ResponseEntity.badRequest().body("La fecha de inicio o fin se superpone con otro periodo");
            }
        }else{
            if(periodoService.isOverLappingAndIsNot(periodo)){
                return ResponseEntity.badRequest().body("La fecha de inicio o fin se superpone con otro periodo");
            }
        }
        PeriodoModel p = periodoService.upsert(periodo, tokenGetUserService.username(authHeader));
        if(p == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la gestión");
        }
        return ResponseEntity.ok().body(p);

    }
    @CrossOrigin
    @PostMapping("eliminar")
    public ResponseEntity<?>eliminar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PeriodoValidate periodo, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        PeriodoModel p = periodoService.un_periodo(periodo.getId(), tokenGetUserService.username(authHeader));
        if(p == null){
            return ResponseEntity.badRequest().body("No se ha encontrado el periodo");
        }
        if(p.getEstado() == false){
            return ResponseEntity.badRequest().body("El periodo ya se encuentra cerrado");
        }
        boolean x = periodoService.eliminar(p.getId(), tokenGetUserService.username(authHeader));
        if(x){
            return ResponseEntity.ok().body("Periodo eliminado con éxito");
        }

        return ResponseEntity.badRequest().body("No se ha podido eliminar el periodo");
    }
    @CrossOrigin
    @PostMapping("cerrar")
    public ResponseEntity<?> cerrar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PeriodoValidate periodo, BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        PeriodoModel p = periodoService.un_periodo(periodo.getId(), tokenGetUserService.username(authHeader));
        if(p == null){
            return ResponseEntity.badRequest().body("No se ha encontrado el periodo");
        }
        if(p.getEstado() == false){
            return ResponseEntity.badRequest().body("El periodo ya se encuentra cerrado");
        }
        p.setEstado(false);
        periodoService.savePeriodo(p);
        return ResponseEntity.ok().body(p);

    }
}
