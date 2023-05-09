package com.carla.erp_senseve.auth;
import com.carla.erp_senseve.validate.ResponseMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){

        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        //Handle runtime exception
        try{
            return ResponseEntity.ok(service.authenticate(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage("Usuario o contraseña incorrectos"));
        }
    }
}
