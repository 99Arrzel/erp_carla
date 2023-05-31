package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.CuentasIntegracion;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.repositories.CuentaRepository;
import com.carla.erp_senseve.repositories.CuentasIntegracionRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentasIntegracionService {
    @Autowired
    CuentasIntegracionRepository cuentasIntegracionRepository;
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    CuentaRepository cuentaRepository;

    public CuentasIntegracion listar(String empresaId) {
        return cuentasIntegracionRepository.findByEmpresaId(Long.parseLong(empresaId));
    }

    public CuentasIntegracion integrar(String empresaId, String cuentaCajaId, String cuentaComprasId, String cuentaCreditoFiscalId, String cuentaDebitoFiscalId, String cuentaItId, String cuentaItPorPagarId, String cuentaVentasId, String estado) {
        //buscar primeor para ver si existe de esa empresa
        CuentasIntegracion cuentasIntegracion = cuentasIntegracionRepository.findByEmpresaId(Long.parseLong(empresaId));
        if (cuentasIntegracion == null) {
            cuentasIntegracion = new CuentasIntegracion();
        }
        EmpresaModel empresa = empresaRepository.findById(Long.parseLong(empresaId)).orElseThrow(
                () -> new RuntimeException("Empresa no encontrada")
        );
        cuentasIntegracion.setEstado(estado);
        cuentasIntegracion.setEmpresa(empresa);
        cuentasIntegracion.setCuenta_caja(cuentaRepository.findById(Long.parseLong(cuentaCajaId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_compras(cuentaRepository.findById(Long.parseLong(cuentaComprasId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_credito_fiscal(cuentaRepository.findById(Long.parseLong(cuentaCreditoFiscalId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_debito_fiscal(cuentaRepository.findById(Long.parseLong(cuentaDebitoFiscalId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_it(cuentaRepository.findById(Long.parseLong(cuentaItId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_it_por_pagar(cuentaRepository.findById(Long.parseLong(cuentaItPorPagarId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        cuentasIntegracion.setCuenta_ventas(cuentaRepository.findById(Long.parseLong(cuentaVentasId)).orElseThrow(
                () -> new RuntimeException("Cuenta no encontrada")
        ));
        return cuentasIntegracionRepository.save(cuentasIntegracion);
    }
}
