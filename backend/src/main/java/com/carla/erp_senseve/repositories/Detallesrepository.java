package com.carla.erp_senseve.repositories;


import com.carla.erp_senseve.models.DetallesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Detallesrepository extends JpaRepository<DetallesModel, Long> {

}
