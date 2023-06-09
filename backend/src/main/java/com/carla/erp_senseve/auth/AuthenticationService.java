package com.carla.erp_senseve.auth;
import com.carla.erp_senseve.config.JwtService;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.authentication.AuthenticationManager;
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UsuarioRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;


  public AuthenticationResponse register(RegisterRequest request) {


    var usuario = new UsuarioModel();
    usuario.setNombre(request.getNombre());
    if(request.getNombre() == null || request.getNombre().isBlank()){
      throw new RuntimeException("Nombre no puede estar vacio");
    }
    if(request.getUsuario() == null || request.getUsuario().isBlank()){
      throw new RuntimeException("Usuario no puede estar vacio");
    }
    if(request.getPassword() == null || request.getPassword().isBlank()){
      throw new RuntimeException("Password no puede estar vacio");
    }
    if(request.getTipo() == null || request.getTipo().isBlank()){
      throw new RuntimeException("Tipo no puede estar vacio");
    }
    usuario.setUsuario(request.getUsuario());
    usuario.setPassword(passwordEncoder.encode(request.getPassword()));
    usuario.setTipo(request.getTipo());
    repository.save(usuario);
    var jwtToken = jwtService.generateToken(usuario);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    //Hard code this shit
    var u = repository.findByUsuario(request.getUsuario()).orElseThrow(
        () -> new RuntimeException("Usuario no encontrado")
    );
    //uncrypt password
    if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
      throw new RuntimeException("Password incorrecto");
    }
    var usuario = repository.findByUsuario(request.getUsuario()).orElseThrow(
        () -> new RuntimeException("Usuario no encontrado")
    );
    var jwtToken = jwtService.generateToken(usuario);


    //Add user id too
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

}
