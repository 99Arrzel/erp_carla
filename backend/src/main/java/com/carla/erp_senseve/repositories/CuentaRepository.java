package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.CuentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaModel, Long> {


    @Query(value = "SELECT * FROM cuentas WHERE empresa_id = :empresa_id AND nombre = :nombre AND id != :id", nativeQuery = true)
    List<CuentaModel> findByEmpresaIdAndNombreAndIdNot(
        @Param("empresa_id") Long empresa_id,
        @Param("nombre") String nombre,
        @Param("id") Long id
        );

    List<CuentaModel> findByEmpresaIdAndNombre(Long empresa_id, String nombre);

    @Query(value = "SELECT * FROM cuentas WHERE empresa_id = ?1 ORDER BY inet_truchon(codigo)", nativeQuery = true)
    List<CuentaModel> findByEmpresaId(Long empresa_id);
    List<CuentaModel> findByPadre_id(Long id);
    //Find by empresa_id and padre_id
    List<CuentaModel> findByEmpresaIdAndPadre_id(Long empresa_id, Long padre_id);
    //Find by empresa_id and padre_id, count if padre_id is null
    @Query(value = "SELECT COUNT(*) FROM cuentas WHERE empresa_id = ?1 AND padre_id = ?2", nativeQuery = true)
    Integer countByEmpresaIdAndPadreId(Long empresa_id, Long padre_id);
    //Count Where parent is null
    @Query(value = "SELECT COUNT(*) FROM cuentas WHERE empresa_id = ?1 AND padre_id IS NULL", nativeQuery = true)
    Integer countByEmpresaIdAndPadreIsNull(Long empresa_id);

    //@Query(value = "SELECT * FROM cuentas WHERE empresa_id = ?1 AND tipo = 'Detalle'", nativeQuery = true)
    @Query(value = "SELECT * FROM cuentas WHERE empresa_id = ?1 AND tipo = 'Detalle' AND padre_id IS NOT NULL", nativeQuery = true)
    List<CuentaModel> findByEmpresaIdYEsDetalle(Long id);

    //Es fácil de detectar, tenemos que traer las cuentas que empiezan con 1. o 2. o 3.
    @Query(value = "SELECT * FROM cuentas WHERE empresa_id = ?1 AND codigo LIKE '1.%' OR codigo LIKE '2.%' OR codigo LIKE '3.%' ORDER BY inet_truchon(codigo)", nativeQuery = true)
    List<CuentaModel> findByEmpresaIdAndActivoAndPasivoAndPatrimonio(Long idEmpresa);
}
