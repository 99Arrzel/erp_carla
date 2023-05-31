package com.carla.erp_senseve.repositories;


import com.carla.erp_senseve.models.CuentasIntegracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CuentasIntegracionRepository extends JpaRepository<CuentasIntegracion, Long> {

    @Query(value = "SELECT * FROM cuentas_integracion WHERE empresa_id = ?1 LIMIT 1", nativeQuery = true)
    CuentasIntegracion findByEmpresaId(Long empresaId);
}
