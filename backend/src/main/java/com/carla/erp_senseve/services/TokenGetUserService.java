package com.carla.erp_senseve.services;


import com.carla.erp_senseve.config.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class TokenGetUserService {
    public String username(String authHeader) {
        JwtService j = new JwtService();
        String jwtToken = authHeader.substring(7);
        Claims claims = j.allClaims(jwtToken);
        String username = claims.getSubject();
        return username;
    }
}