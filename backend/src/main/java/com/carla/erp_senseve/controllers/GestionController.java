package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.GestionModel;
import com.carla.erp_senseve.services.EmpresaService;
import com.carla.erp_senseve.services.GestionService;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.validate.GestionValidate;
import com.carla.erp_senseve.validate.ResponseMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
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

    @PostMapping("por_empresa")
    public ResponseEntity<?> por_empresa(@RequestHeader("Authorization") String authHeader, @RequestBody EmpresaModel empresa) {
        if (empresa.getId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la empresa"));
        }
        Optional<EmpresaModel> e = empresaService.una_empresa(empresa.getId(), tokenGetUserService.username(authHeader));
        if (e.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la empresa"));
        }
        e.get();
        return ResponseEntity.ok().body(e.get());
    }


    @PostMapping("upsert")
    public ResponseEntity<?> upsert(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody GestionValidate gestion, BindingResult bindingResult) throws HttpMessageNotReadableException {
        //if throws HttpMessageNotReadableException, it means that the request body is empty
        try {
            GestionModel g = gestionService.upsert(gestion, tokenGetUserService.username(authHeader));
            return ResponseEntity.ok().body(g);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }

    }

    @PostMapping("cerrar")
    public ResponseEntity<?> cerrar(@RequestHeader("Authorization") String authHeader, @RequestBody GestionModel gestion) {
        if (gestion.getId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la gestión"));
        }
        Optional<GestionModel> g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));
        if (g.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la gestión"));
        }
        if (g.get().getEstado().equals(false)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("La gestión ya está cerrada"));
        }
        Optional<GestionModel> g2 = gestionService.cerrar(g.get().getId(), tokenGetUserService.username(authHeader));
        if (g2.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha podido cerrar la gestión"));
        }
        return ResponseEntity.ok().body(new ResponseMessage("Gestión cerrada"));
    }

    @PostMapping("eliminar")
    public ResponseEntity<?> eliminar(@RequestHeader("Authorization") String authHeader, @RequestBody GestionModel gestion) {
        if (gestion.getId() == null) {
            return ResponseEntity.badRequest().body("No se ha enviado el id de la gestión");
        }
        Optional<GestionModel> g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));
        if (g.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la gestión"));
        }
        if (g.get().getEstado() == false) {
            return ResponseEntity.badRequest().body(new ResponseMessage("La gestión cerrada no se puede eliminar"));
        }
        boolean g2 = gestionService.eliminar(g.get().getId(), tokenGetUserService.username(authHeader));
        if (!g2) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha podido eliminar la gestión"));
        }
        return ResponseEntity.ok().body(new ResponseMessage("Gestión eliminada"));
    }

    @PostMapping("con_periodos")
    public ResponseEntity<?> con_periodos(@RequestHeader("Authorization") String authHeader, @RequestBody GestionModel gestion) {
        if (gestion.getId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha enviado el id de la gestión"));
        }
        Optional<GestionModel> g = gestionService.una_gestion(gestion.getId(), tokenGetUserService.username(authHeader));
        if (g.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No se ha encontrado la gestión"));
        }
        //g.get().getPeriodos();

        //System.out.println(g.get().getPeriodos());
        //System.out.println(g.get().getId());
        return ResponseEntity.ok(g.get());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof DateTimeParseException) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Valor invalido: " + cause.getMessage()));
        }
        return ResponseEntity.badRequest().body(new ResponseMessage("Entrada invalida: " + ex.getMessage()));
    }


}
