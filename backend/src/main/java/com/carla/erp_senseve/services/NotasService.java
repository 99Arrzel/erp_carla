package com.carla.erp_senseve.services;


import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaCompraRequest;
import com.carla.erp_senseve.models.*;
import com.carla.erp_senseve.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotasService {
    @Autowired
    NotasRepository notasRepository;
    @Autowired
    ArticuloService articuloService;

    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    LoteRepository loteRepository;

    @Autowired
    ComprobanteRepository comprobanteRepository;
    @Autowired
    ComprobanteService comprobanteService;

    public List<NotaModel> listar(String empresaId, String tipo) {
        if (empresaId == null || empresaId.isBlank()) {
            throw new RuntimeException("El id de la empresa es obligatorio");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new RuntimeException("El tipo es obligatorio");
        }
        List<NotaModel> notas = notasRepository.findByEmpresaIdAndTipo(Long.parseLong(empresaId), tipo);
        //Fetch lotes y de los lotes, fetch productos
        for (NotaModel nota : notas) {
            nota.getLote().size();
            for (int i = 0; i < nota.getLote().size(); i++) {
                nota.getLote().get(i).getArticulo();
            }
        }
        return notas;
    }

    public NotaModel crear_compra(CrearNotaCompraRequest data) {
        EmpresaModel empresa = empresaRepository.findById(data.getEmpresa_id()).orElseThrow(
                () -> new RuntimeException("Empresa no encontrada")
        );
        ComprobanteModel comprobante = new ComprobanteModel();

        Float total = data.getLotes().stream().map(l -> l.getPrecio() * l.getCantidad()).reduce(0f, Float::sum);

        if (empresa.getCuentaIntegracion().size() > 0) {
            //TODO: Crear la integracion
            //Siempre habrá solo 1, así que, agarramos ese 1
            CuentasIntegracion integracion = empresa.getCuentaIntegracion().get(0);
            EmpresaMonedaModel moneda_empresa = empresa.getMonedas().stream().filter(m -> m.getEstado()).findFirst().orElseThrow(
                    () -> new RuntimeException("No se encontró la moneda")
            );
            //buscamos periodo abierto en data.getFecha()
            List<GestionModel> gestiones = empresa.getGestiones().stream().filter(
                    g -> g.getPeriodos().stream().filter(
                            p -> p.getEstado() && p.getFechaInicio().compareTo(data.getFecha()) <= 0 && p.getFechaFin().compareTo(data.getFecha()) >= 0
                    ).count() > 0
            ).toList();
            if (gestiones.size() == 0) {
                throw new RuntimeException("No se encontró el periodo abierto");
            }
            ComprobanteModel comprobanteModel = new ComprobanteModel();
            comprobanteModel.setEmpresa(empresa);
            comprobanteModel.setFecha(data.getFecha());
            //Ultima serie de comprobante
            String serie = String.valueOf(comprobanteRepository.contar(empresa.getId()) + 1);
            comprobanteModel.setSerie(serie);
            comprobanteModel.setGlosa("Compra de mercaderías");
            comprobanteModel.setTipo("Egreso");
            comprobanteModel.setEstado("Abierto");
            comprobanteModel.setTc(moneda_empresa.getCambio());
            comprobanteModel.setUsuario(empresa.getUsuario());
            //Ahora los detalles de comprobante
            List<DetalleComprobanteModel> detalles = new ArrayList<>();
            //Lógica de compras
            DetalleComprobanteModel compras = new DetalleComprobanteModel();
            compras.setCuenta(integracion.getCuenta_compras());
            compras.setMonto_debe((float) (total * 0.87));
            compras.setMonto_debe_alt((float) (total * 0.87) * moneda_empresa.getCambio());
            compras.setMonto_haber(0f);
            compras.setMonto_haber_alt(0f);
            compras.setGlosa("Compra de mercaderías");
            compras.setNumero("1");
            compras.setUsuario(empresa.getUsuario());
            detalles.add(compras);
            //Credito fiscal
            DetalleComprobanteModel credito = new DetalleComprobanteModel();
            credito.setCuenta(integracion.getCuenta_credito_fiscal());
            credito.setMonto_debe((float) (total * 0.13));
            credito.setMonto_debe_alt((float) (total * 0.13) * moneda_empresa.getCambio());
            credito.setMonto_haber(0f);
            credito.setMonto_haber_alt(0f);
            credito.setGlosa("Compra de mercaderías");
            credito.setNumero("2");
            credito.setUsuario(empresa.getUsuario());
            detalles.add(credito);
            //Caja
            DetalleComprobanteModel caja = new DetalleComprobanteModel();
            caja.setCuenta(integracion.getCuenta_caja());
            caja.setMonto_debe((float) (total));
            caja.setMonto_debe_alt((float) (total) * moneda_empresa.getCambio());
            caja.setMonto_haber(0f);
            caja.setMonto_haber_alt(0f);
            caja.setGlosa("Compra de mercaderías");
            caja.setNumero("3");
            caja.setUsuario(empresa.getUsuario());
            detalles.add(caja);
            //guardar
            comprobanteModel.setDetalles(detalles);
            comprobanteRepository.save(comprobanteModel);
        }
        NotaModel nota = new NotaModel();
        nota.setDescripcion(data.getDescripcion());
        nota.setEmpresa(empresa);
        nota.setFecha(data.getFecha());
        String nro = String.valueOf(notasRepository.countByEmpresaIdAndTipo(data.getEmpresa_id(), "COMPRA") + 1);
        nota.setNro_nota(nro);
        nota.setTipo("COMPRA");
        nota.setTotal(
                (float) data.getLotes().stream().mapToDouble(lote -> lote.getPrecio() * lote.getCantidad()).sum()
        );
        nota.setUsuario(empresa.getUsuario());
        if (comprobante != null) {
            nota.setComprobante(comprobante);
        }
        //
        List<LotesModel> lotes = new ArrayList<>();
        data.lotes.forEach(lote -> {
            LotesModel loteModel = new LotesModel();
            loteModel.setArticulo(articuloRepository.findById(lote.getArticulo_id()).orElseThrow(
                    () -> new RuntimeException("Articulo no encontrado")
            ));
            loteModel.setCantidad(lote.getCantidad());
            loteModel.setPrecio_compra(lote.getPrecio());
            loteModel.setEstado("ACTIVO");
            loteModel.setFecha_vencimiento((Date) lote.getFecha_vencimiento());
            loteModel.setFecha_ingreso(data.getFecha());
            loteModel.setStock(lote.getCantidad());
            loteModel.setNro_lote(String.valueOf(loteRepository.countByArticuloId(lote.getArticulo_id()) + 1));
            lotes.add(loteModel);
        });
        nota.setLote(lotes);
        return notasRepository.save(nota);
    }
    
}
