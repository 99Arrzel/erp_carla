package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public ArrayList<UsuarioModel> obtenerUsuarios() {
        return (ArrayList<UsuarioModel>) usuarioRepository.findAll();
    }
    public UsuarioModel login(UsuarioModel usuario){
        ArrayList<UsuarioModel> usuarios = obtenerUsuarios();
        for(UsuarioModel usuarioModel: usuarios){
            if(usuarioModel.getNombre().equals(usuario.getNombre())){
                if(BCrypt.checkpw(usuario.getPassword(), usuarioModel.getPassword())){
                    return usuarioModel;
                }
            }
        }
        return null;
    }
    public Optional<UsuarioModel> crearUsuario(UsuarioModel usuario){
        usuario.setPassword(BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()));
        return Optional.of(usuarioRepository.save(usuario));
    }
    public Optional<UsuarioModel> obtenerPorUsuario(String usuario) throws Exception{
            return usuarioRepository.findByUsuario(usuario);
    }

    public UsuarioModel findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new RuntimeException("No existe usuario")
        );
    }
}
