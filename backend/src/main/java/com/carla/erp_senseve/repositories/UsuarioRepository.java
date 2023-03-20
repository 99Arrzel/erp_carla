package com.carla.erp_senseve.repositories;



import com.carla.erp_senseve.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    //This method retrieves a user from the database by their username
    Optional<UsuarioModel> findByUsuario(String usuario);



}
