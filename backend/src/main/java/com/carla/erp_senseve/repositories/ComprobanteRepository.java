package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.ComprobanteModel;
import com.carla.erp_senseve.models.PeriodoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobanteRepository  extends  JpaRepository<ComprobanteModel, Long>{
    //Crear comprobante
    @Query(value = "INSERT INTO public.comprobantes(estado, fecha, glosa, serie, tc, tipo, usuario_id, moneda_id) VALUES(:estado, :fecha, :glosa, :serie, :tc, :tipo, :usuario_id, :moneda_id)", nativeQuery = true)
    Optional<ComprobanteModel> crear(
            @Param("estado") String estado,
            @Param ("fecha") String fecha,
            @Param("glosa") String glosa,
            @Param("serie") String serie,
            @Param("tc") Double tc,
            @Param("tipo") String tipo,
            @Param("usuario_id") Long usuario_id,
            @Param("moneda_id") Long moneda_id
    );
    //Contar comprobantes
    @Query(value = "SELECT COUNT(*) FROM comprobantes WHERE empresa_id = ?1", nativeQuery = true)
    Long contar(Long empresa_id);
    @Query(value = "SELECT * FROM comprobantes WHERE empresa_id = ?1 AND usuario_id = ?2 ORDER BY serie ASC", nativeQuery = true)
    List <ComprobanteModel> findByEmpresaIdAndUsuarioId(
            @Param("empresa_id") Long empresa_id,
            @Param("usuario_id") Long usuario_id
    );

    //El ultimo serie de comprobante por empresa
    @Query(value = "SELECT serie FROM comprobantes WHERE empresa_id = ?1 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String ultimo_numero(
            @Param("empresa_id") Long empresa_id
    );

    // Buscar comprobante por between fechas y empresa y que sea apertura
    @Query(value = "SELECT * FROM comprobantes WHERE empresa_id = ?1 AND tipo = 'Apertura' AND estado != 'Anulado' AND fecha BETWEEN ?2 AND ?3 ORDER BY id DESC LIMIT 1", nativeQuery = true)
    ComprobanteModel buscarAperturaParaEmpresaEnFechaInicioYFechaFin(Long empresaId, Date fechaInicio, Date fechaFin);
    @Query(value = "SELECT * FROM comprobantes WHERE empresa_id = ?1 AND estado != 'Anulado' AND fecha BETWEEN ?2 AND ?3 ORDER BY id DESC", nativeQuery = true)
    List<ComprobanteModel> buscarComprobantesParaEmpresaEnFechaInicioYFechaFin(
            @Param("empresa_id") Long empresa_id,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );
}
