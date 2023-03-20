package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.UsuarioModel;

import com.carla.erp_senseve.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;
    @CrossOrigin
    @GetMapping("/usuario/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }



}
