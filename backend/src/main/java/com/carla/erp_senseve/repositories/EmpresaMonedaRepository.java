package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.CuentaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.EmpresaMonedaModel;
import com.carla.erp_senseve.models.MonedaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaMonedaRepository extends JpaRepository<EmpresaMonedaModel, Long> {


    List<EmpresaMonedaModel> findByEmpresaId(
        @Param("empresa_id") Long empresa_id
    );

    @Query("SELECT em FROM EmpresaMonedaModel em LEFT JOIN FETCH em.moneda_principal LEFT JOIN FETCH em.moneda_alternativa WHERE em.empresa.id = :empresa_id")
    List<EmpresaMonedaModel> findAllWithRelationshipsByEmpresaId(
        @Param("empresa_id") Long empresa_id
    );
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM empresas_monedas WHERE empresa_id = ?1", nativeQuery = true)
    void deleteAllByEmpresaId(
        @Param("empresa_id") Long empresa_id
    );
    @Transactional
    @Modifying
    @Query(value = "UPDATE empresas_monedas SET estado = false WHERE empresa_id = ?1", nativeQuery = true)
    void setAllToFalse(
        @Param("empresa_id") Long empresa_id
    );

    EmpresaMonedaModel findFirstByEmpresaIdAndEstadoOrderByIdDesc(
        @Param("empresa_id") Long empresa_id,
        @Param("estado") Boolean estado
    );

    //Buscar por id empresa y estado true
    @Query(value = "SELECT * FROM empresas_monedas WHERE empresa_id = :idEmpresa AND estado = true LIMIT 1", nativeQuery = true)
    EmpresaMonedaModel findByIdEmpresaYEstadoTrue(
            @Param("idEmpresa") Long idEmpresa
    );
}
