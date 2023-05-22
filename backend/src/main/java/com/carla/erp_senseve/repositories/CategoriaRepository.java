package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {

    @Query (value = "SELECT * FROM categoria WHERE empresa_id = ?1", nativeQuery = true)
    List<CategoriaModel> findByEmpresaId(
            @Param("empresa_id") Long empresa_id
    );


    @Query (value = "SELECT * FROM categoria WHERE nombre = ?1 AND empresa_id = ?2 LIMIT 1", nativeQuery = true)
    Optional<CategoriaModel> findByNombreEnEmpresa(String nombre, Long empresa_id);
}
