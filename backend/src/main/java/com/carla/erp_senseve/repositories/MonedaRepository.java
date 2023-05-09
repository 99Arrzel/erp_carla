package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.MonedaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonedaRepository extends JpaRepository<MonedaModel, Long> {

  @Query(value = "select * from \"monedas\" where \"usuario_id\" = :usuario_id", nativeQuery = true)
  List<MonedaModel> findByUsuarioId(
      @Param( "usuario_id" ) Long usuario_id
  );


}
