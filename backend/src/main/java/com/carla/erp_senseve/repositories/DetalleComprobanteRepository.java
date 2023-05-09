package com.carla.erp_senseve.repositories;

import com.carla.erp_senseve.models.DetalleComprobanteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobanteModel, Long> {
    //Crear detalle
    /*INSERT INTO public.detalle_comprobantes
(glosa, monto_debe, monto_debe_alt, monto_haber, monto_haber_alt, numero, usuario_id, comprobante_id, cuenta_id)
VALUES('', 0, 0, 0, 0, '', 0, 0, 0);
*/
    @Query(value = "INSERT INTO public.detalle_comprobantes(glosa, monto_debe, monto_debe_alt, monto_haber, monto_haber_alt, numero, usuario_id, comprobante_id, cuenta_id) VALUES(:glosa, :monto_debe, :monto_debe_alt, :monto_haber, :monto_haber_alt, :numero, :usuario_id, :comprobante_id, :cuenta_id)", nativeQuery = true)
    DetalleComprobanteModel crear(
            String glosa,
            Double monto_debe,
            Double monto_debe_alt,
            Double monto_haber,
            Double monto_haber_alt,
            String numero,
            Long usuario_id,
            Long comprobante_id,
            Long cuenta_id
    );

    //Por comprobante_id
    @Query(value = "SELECT * FROM detalle_comprobantes WHERE comprobante_id = :idComprobante", nativeQuery = true)
    List<DetalleComprobanteModel> findByIdComprobante(
            @Param("idComprobante") Long idComprobante
    );

    //Por comprobante_id y cuenta_id
    @Query(value = "SELECT * FROM detalle_comprobantes WHERE comprobante_id = :idComprobante AND cuenta_id = :idCuenta LIMIT 1", nativeQuery = true)
    DetalleComprobanteModel findByIdComprobanteAndIdCuenta(
            @Param("idComprobante") Long idComprobante,
            @Param("idCuenta") Long idCuenta
    );

}

