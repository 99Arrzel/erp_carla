package com.carla.erp_senseve.repositories;


import com.carla.erp_senseve.models.ArticuloModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloRepository extends JpaRepository<ArticuloModel, Long> {


    @Query (value = "SELECT * FROM articulos WHERE empresa_id = ?1 AND usuario_id = ?2", nativeQuery = true)
    List<ArticuloModel> findByIdAndUsuario(
            @Param("empresa_id") Long empresa_id,
            @Param("usuario_id") Long usuario_id);
}
