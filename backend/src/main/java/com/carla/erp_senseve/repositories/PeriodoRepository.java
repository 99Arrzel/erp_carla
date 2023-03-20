package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.PeriodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PeriodoRepository extends JpaRepository<PeriodoModel, Long> {
    //This method retrieves a user from the database by their username
    List<PeriodoModel> findByIdNotAndGestionIdAndFechaInicioBetweenOrFechaFinBetween(
            Long id, Long gestionId, Date fechaInicio, Date fechaFin, Date fechaInicio2, Date fechaFin2);
    List<PeriodoModel> findByFechaInicioBetweenAndFechaFinBetweenAndGestionId(
            Date fechaInicio1, Date fechaInicio2, Date fechaFin1, Date fechaFin2, Long gestionId);

}
