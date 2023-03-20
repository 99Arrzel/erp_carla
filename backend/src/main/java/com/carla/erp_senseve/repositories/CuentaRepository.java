package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.CuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaModel, Long> {
    List<CuentaModel> findByEmpresaId(Long empresa_id);
    List<CuentaModel> findByPadreId(Long id);
    //Find by empresa_id and padre_id
    List<CuentaModel> findByEmpresaIdAndPadreId(Long empresa_id, Long padre_id);
    //Find by empresa_id and padre_id, count if padre_id is null
    @Query(value = "SELECT COUNT(*) FROM cuentas WHERE empresa_id = ?1 AND padre_id = ?2", nativeQuery = true)
    Integer countByEmpresaIdAndPadreId(Long empresa_id, Long padre_id);
    //Count Where parent is null
    @Query(value = "SELECT COUNT(*) FROM cuentas WHERE empresa_id = ?1 AND padre_id IS NULL", nativeQuery = true)
    Integer countByEmpresaIdAndPadreIsNull(Long empresa_id);


}
