package com.carla.erp_senseve.controllers;

import com.carla.erp_senseve.models.UsuarioModel;

import com.carla.erp_senseve.repositories.UsuarioRepository;
import com.carla.erp_senseve.services.TokenGetUserService;
import com.carla.erp_senseve.services.UsuarioService;
import com.carla.erp_senseve.validate.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    TokenGetUserService tokenGetUserService;

    @Autowired
    UsuarioRepository usuarioRepository;
    @GetMapping("/usuario/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }
    @GetMapping("/getMyData")
    public ResponseEntity<?> getMyId(
            @RequestHeader("Authorization") String authHeader
    ){
        var usuario = tokenGetUserService.username(authHeader);
        var data = usuarioRepository.findByUsuario(usuario);
        return ResponseEntity.ok().body(data);
    }
}
