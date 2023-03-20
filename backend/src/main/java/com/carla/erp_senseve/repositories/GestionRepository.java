package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.GestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GestionRepository extends JpaRepository<GestionModel, Long>{
    //Find by empresa_id
    List<GestionModel> findByEmpresaId(Long empresa_id);
    //Find between fecha_inicio and fecha_fin and empresa_id
    List<GestionModel> findByFechaInicioBetweenAndFechaFinBetweenAndEmpresaId(Date fechaInicio1, Date fechaInicio2, Date fechaFin1, Date fechaFin2, Long empresaId);
    //Find where empresa_id and estado
    List<GestionModel> findByEmpresaIdAndEstado(Long empresa_id, Boolean estado);
    //Find overlaping dates

    List<GestionModel> findByIdNotAndEmpresaIdAndFechaInicioBetweenOrFechaFinBetween(
            Long id, Long empresaId, Date fechaInicio, Date fechaFin, Date fechaInicio2, Date fechaFin2);





}
