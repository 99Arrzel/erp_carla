package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.GestionService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.GestionValidate;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestion")
public class GestionController {
    @Autowired
    EmpresaService empresaService;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    GestionService gestionService;
    @CrossOrigin
    @PostMapping("por_empresa")
    public ResponseEntity<?> por_empresa(
           @RequestHeader("Authorization") String authHeader,
           @RequestBody EmpresaModel empresa
    ){
        if(empresa.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la empresa");
        }
        EmpresaModel e = empresaService.una_empresa(empresa.getId(), tokenGetUserService.username(authHeader));
        if(e == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la empresa");
        }
        //Add gestiones to Empresa

        e.getGestiones(); // Esto es para que se carguen las gestiones de la empresa
        return ResponseEntity.ok().body(e);
    }
    @CrossOrigin
    @PostMapping("upsert")
    public ResponseEntity<?> upsert(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody GestionValidate gestion, BindingResult bindingResult
            ) {
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        //If it invalid
        if(gestion.getFecha_fin().before(gestion.getFecha_inicio())){
            return ResponseEntity.badRequest().body("La fecha de fin no puede ser menor a la fecha de inicio");
        }
        if(gestion.getId() == null){
            //check range
            if(gestionService.isOverLapping(gestion)){
                return ResponseEntity.badRequest().body("La fecha de inicio o fin se superpone con otra gestión");
            }
        }else{
            if(gestionService.isOverLappingAndIsNot(gestion)){
                return ResponseEntity.badRequest().body("La fecha de inicio o fin se superpone con otra gestión");
            }
        }

        GestionModel g =gestionService.upsert(gestion, tokenGetUserService.username(authHeader));
        if(g == null){
            return ResponseEntity.badRequest().body("No se ha podido guardar la gestión");
        }
        return ResponseEntity.ok().body(g);
    }
    @CrossOrigin
    @PostMapping("cerrar")
    public ResponseEntity<?> cerrar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GestionModel gestion
    ){
        if(gestion.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la gestión");
        }
        GestionModel g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));
        if(g == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la gestión");
        }
        if(g.getEstado().equals(false)){
            return ResponseEntity.badRequest().body("La gestión ya está cerrada");
        }
        GestionModel g2 = gestionService.cerrar(g.getId(), tokenGetUserService.username(authHeader));
        if(g2 == null){
            return ResponseEntity.badRequest().body("No se ha podido cerrar la gestión");
        }
        return ResponseEntity.ok().body("Gestión cerrada");
    }
    @CrossOrigin
    @PostMapping("eliminar")
    public ResponseEntity<?> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GestionModel gestion
    ){
        if(gestion.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la gestión");
        }
        GestionModel g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));

        if(g == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la gestión");
        }
        if(g.getEstado() == false){
            return ResponseEntity.badRequest().body("La gestión cerrada no se puede eliminar");
        }
        boolean g2 = gestionService.eliminar(g.getId(), tokenGetUserService.username(authHeader));
        if(!g2){
            return ResponseEntity.badRequest().body("No se ha podido eliminar la gestión");
        }
        return ResponseEntity.ok().body("Gestión eliminada");
    }
    @CrossOrigin
    @PostMapping("con_periodos")
    public ResponseEntity<?> con_periodos(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GestionModel gestion
    ){
        if(gestion.getId() == null){
            return ResponseEntity.badRequest().body("No se ha enviado el id de la gestión");
        }
        GestionModel g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));

        if(g == null){
            return ResponseEntity.badRequest().body("No se ha encontrado la gestión");
        }
        g.getPeriodos();
        System.out.println(g.getPeriodos().size());
        System.out.println(g.getId());
        return ResponseEntity.ok(g);
    }

}
