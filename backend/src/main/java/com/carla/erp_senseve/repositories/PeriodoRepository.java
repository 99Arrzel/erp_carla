package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.PeriodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoModel, Long> {
  @Query(value = "select * from \"periodos\" where \"gestion_id\" = :gestionId and id != :id and (:fechaInicio between fecha_inicio AND fecha_fin or :fechaFin between fecha_inicio AND fecha_fin or (\"fecha_inicio\" >= :fechaInicio and \"fecha_fin\" <= :fechaFin))", nativeQuery = true)
  List<PeriodoModel> isOverlappingAndIsNot(
      @Param("id") Long id,
      @Param("gestionId") Long gestionId,
      @Param("fechaInicio") Date fechaInicio,
      @Param("fechaFin") Date fechaFin
  );
  @Query(value = "select * from \"periodos\" where \"gestion_id\" = :gestionId  and (:fechaInicio between fecha_inicio AND fecha_fin or :fechaFin between fecha_inicio AND fecha_fin or (\"fecha_inicio\" >= :fechaInicio and \"fecha_fin\" <= :fechaFin))", nativeQuery = true)
  List<PeriodoModel> isOverlapping(
      @Param("gestionId") Long gestionId,
      @Param("fechaInicio") Date fechaInicio,
      @Param("fechaFin") Date fechaFin
  );
  @Query (value = "select * from \"periodos\" where \"gestion_id\" = :gestionId and \"nombre\" = :nombre", nativeQuery = true)
  List<PeriodoModel> findByGestionIdAndNombre(
      @Param("gestionId") Long gestionId,
      @Param("nombre") String nombre
  );
  @Query (value = "select * from \"periodos\" where \"gestion_id\" = :gestionId and \"nombre\" = :nombre and id != :id", nativeQuery = true)
  List<PeriodoModel> findByGestionIdAndNombreAndIdNot(
    @Param("gestionId") Long gestionId,
    @Param("nombre") String nombre,
    @Param("id") Long id
  );


    /*@Query(value = "select * from \"periodos\" where \"gestion_id\" = :gestionId and \"fecha_inicio\" <= :fecha and \"fecha_fin\" >= :fecha limit 1", nativeQuery = true)
    PeriodoModel hayPeriodoAbiertoEnEmpresaYFecha(
        @Param("empresa_id") Long empresa_id,
        @Param("fecha") Date fecha
    );
    BAD
    */
    @Query(value = "select p.* from \"periodos\" p JOIN \"gestiones\" g ON p.\"gestion_id\" = g.id where g.\"empresa_id\" = :empresa_id and p.\"fecha_inicio\" <= :fecha and p.\"fecha_fin\" >= :fecha limit 1", nativeQuery = true)
    PeriodoModel hayPeriodoAbiertoEnEmpresaYFecha(
            @Param("empresa_id") Long empresa_id,
            @Param("fecha") Date fecha
    );



    //Select por id simple
    @Query(value = "select * from \"periodos\" where id = :id", nativeQuery = true)
    PeriodoModel findByElId(
        @Param("id") Long id
    );
}
