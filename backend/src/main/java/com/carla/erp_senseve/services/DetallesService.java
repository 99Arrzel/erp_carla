package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.DetalleComprobanteModel;
import com.carla.erp_senseve.repositories.DetalleComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallesService {
    @Autowired
    DetalleComprobanteRepository detalleComprobanteRepository;

    public List<DetalleComprobanteModel> por_id_comprobante(Long id_comprobante ) {
        return detalleComprobanteRepository.findByIdComprobante(id_comprobante);
    }

    public DetalleComprobanteModel obtenerDetalleComprobantePorComprobanteYCuenta(Long id_comprobnate, Long id_cuenta) {
        return detalleComprobanteRepository.findByIdComprobanteAndIdCuenta(id_comprobnate, id_cuenta);
    }
}
