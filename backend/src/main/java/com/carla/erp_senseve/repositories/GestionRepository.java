package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.GestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface GestionRepository extends JpaRepository<GestionModel, Long>{
    List<GestionModel> findByEmpresaId(Long empresa_id);


    @Query(value = "select * from \"gestiones\" where \"empresa_id\" = :empresaId and \"nombre\" = :nombre and id != :id", nativeQuery = true)
    Optional<GestionModel> findByNombreAndEmpresaId(
        @Param("nombre") String nombre,
        @Param("empresaId") Long empresa_id,
        @Param("id") Long id
    );

    @Query(value = "select * from \"gestiones\" where \"empresa_id\" = :empresaId and \"nombre\" = :nombre", nativeQuery = true)
    Optional<GestionModel> findByNombreAndEmpresaId(
        @Param("nombre") String nombre,
        @Param("empresaId") Long empresa_id
    );

    @Query(value = "select * from \"gestiones\" where \"empresa_id\" = :empresaId  and (:fechaInicio between fecha_inicio AND fecha_fin or :fechaFin between fecha_inicio AND fecha_fin or (\"fecha_inicio\" >= :fechaInicio and \"fecha_fin\" <= :fechaFin))", nativeQuery = true)
    List<GestionModel> isOverlapping(
            @Param("empresaId") Long empresaId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
    @Query(value = "select * from \"gestiones\" where \"empresa_id\" = :empresaId and id != :id and (:fechaInicio between fecha_inicio AND fecha_fin or :fechaFin between fecha_inicio AND fecha_fin or (\"fecha_inicio\" >= :fechaInicio and \"fecha_fin\" <= :fechaFin))", nativeQuery = true)
    List<GestionModel> isOverlappingAndIsNot(
            @Param("id") Long id,
            @Param("empresaId") Long empresaId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin);
    List<GestionModel> findByEmpresaIdAndEstado(Long empresa_id, Boolean estado);

    @Query(value = "select * from \"gestiones\" where \"empresa_id\" = :empresaId and \"fecha_inicio\" <= :fecha and \"fecha_fin\" >= :fecha limit 1", nativeQuery = true)
    GestionModel gestionEnFechaConIdEmpresa(java.sql.Date fecha, Long empresaId);
}
