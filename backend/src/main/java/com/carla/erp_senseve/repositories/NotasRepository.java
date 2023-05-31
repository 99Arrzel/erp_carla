package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.NotaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotasRepository extends JpaRepository<NotaModel, Long> {

    @Query(value = "SELECT * FROM notas WHERE empresa_id = :empresa_id AND tipo = :tipo", nativeQuery = true)
    List<NotaModel> findByEmpresaIdAndTipo(
            @Param("empresa_id") Long empresa_id,
            @Param("tipo") String tipo);

    //Nro nota en base a conteo de empresa_id y tipo de nota
    @Query(value = "SELECT COUNT(*) FROM notas WHERE empresa_id = :empresa_id AND tipo = :tipo", nativeQuery = true)
    Integer countByEmpresaIdAndTipo(
            @Param("empresa_id") Long empresa_id,
            @Param("tipo") String tipo);
}
