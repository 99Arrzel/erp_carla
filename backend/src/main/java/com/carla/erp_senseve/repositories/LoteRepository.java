package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.LotesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoteRepository extends JpaRepository<LotesModel, Long> {
    
    @Query(value = "SELECT COUNT(*) FROM lotes WHERE articulo_id = :articulo_id", nativeQuery = true)
    Integer countByArticuloId(
            @Param("articulo_id") Long articulo_id
    );


}
