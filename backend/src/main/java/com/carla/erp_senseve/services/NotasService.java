package com.carla.erp_senseve.services;


import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaCompraRequest;
import com.carla.erp_senseve.controllers.DTOControllers.CrearNotaVentaRequest;
import com.carla.erp_senseve.models.*;
import com.carla.erp_senseve.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    LoteRepository lotesRepository;
    @Autowired
    Detallesrepository detallesRepository;

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
        ComprobanteModel comprobante = null;

        Float total = data.getLotes().stream().map(l -> l.getPrecio() * l.getCantidad()).reduce(0f, Float::sum);

        if (empresa.getCuentaIntegracion().size() > 0 && empresa.getCuentaIntegracion().get(0).getEstado() == "Activo") {
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

            comprobanteRepository.save(comprobanteModel);
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
            compras.setComprobante(comprobanteModel);
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
            credito.setComprobante(comprobanteModel);
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
            caja.setComprobante(comprobanteModel);
            detalles.add(caja);
            //guardar

            comprobanteModel.setDetalles(detalles);
            comprobanteRepository.save(comprobanteModel);
            comprobante = comprobanteModel;


        }
        NotaModel nota = new NotaModel();
        nota.setDescripcion(data.getDescripcion());
        nota.setEmpresa(empresa);
        nota.setFecha(data.getFecha());
        String nro = String.valueOf(notasRepository.countByEmpresaIdAndTipo(data.getEmpresa_id(), "COMPRA") + 1);
        nota.setNro_nota(nro);
        nota.setEstado("ACTIVO");
        nota.setTipo("COMPRA");
        nota.setTotal(
                (float) data.getLotes().stream().mapToDouble(lote -> lote.getPrecio() * lote.getCantidad()).sum()
        );
        nota.setUsuario(empresa.getUsuario());
        if (comprobante != null) {
            nota.setComprobante(comprobante);
        }
        notasRepository.save(nota);
        //
        List<LotesModel> lotes = new ArrayList<>();
        data.lotes.forEach(lote -> {
            LotesModel loteModel = new LotesModel();
            ArticuloModel art = articuloRepository.findById(lote.getArticulo_id()).orElseThrow(
                    () -> new RuntimeException("Articulo no encontrado")
            );
            loteModel.setArticulo(art);
            art.setStock((int) (art.getStock() + lote.getCantidad()));

            loteModel.setCantidad(lote.getCantidad());
            loteModel.setPrecio_compra(lote.getPrecio());
            loteModel.setEstado("ACTIVO");
            loteModel.setFecha_vencimiento(lote.getFecha_vencimiento());
            loteModel.setFecha_ingreso(data.getFecha());
            loteModel.setStock(lote.getCantidad());
            loteModel.setNro_lote(String.valueOf(loteRepository.countByArticuloId(lote.getArticulo_id()) + 1));
            loteModel.setNota(nota);
            lotesRepository.save(loteModel);

        });
        nota.setLote(lotes);
        notasRepository.save(nota);
        return nota;
    }

    public Integer ultimo_numero(String empresaId, String tipo) {
        return notasRepository.countByEmpresaIdAndTipo(Long.parseLong(empresaId), tipo) + 1;
    }

    public NotaModel anular_compra(String notaId) {
        //Necesita una transaccion
        //Remove stock from articulo, remove stock from lote
        NotaModel nota = notasRepository.findById(Long.parseLong(notaId)).orElseThrow(
                () -> new RuntimeException("Nota no encontrada")
        );
        if (nota.getEstado().equals("ANULADO")) {
            throw new RuntimeException("Nota ya anulada");
        }

        nota.setEstado("ANULADO");
        //From each detail (Lote) remove stock
        nota.getLote().forEach(lotesModel -> {
                    //chequear que stock sea diferente de cantidad
                    if (!lotesModel.getStock().equals(lotesModel.getCantidad())) {
                        throw new RuntimeException("No se puede anular la nota, ya se vendió parte del stock");
                    }
                    lotesModel.getArticulo().setStock((int) (lotesModel.getArticulo().getStock() - lotesModel.getCantidad()));
                    lotesModel.setEstado("ANULADO");
                    lotesRepository.save(lotesModel);
                }
        );
        if (nota.getComprobante() != null) {
            ComprobanteModel comp = nota.getComprobante();
            comp.setEstado("ANULADO");
            comprobanteRepository.save(comp);
        }
        return notasRepository.save(nota);
    }

    public NotaCompraDTO una_nota_compra(String notaId) {
        NotaModel nota = notasRepository.findById(Long.parseLong(notaId)).orElseThrow(
                () -> new RuntimeException("Nota no encontrada")
        );
        NotaCompraDTO dto = new NotaCompraDTO();
        dto.setId(nota.getId());
        dto.setFecha(nota.getFecha());
        dto.setNumero(nota.getNro_nota());
        dto.setEstado(nota.getEstado());
        dto.setDescripcion(nota.getDescripcion());
        dto.setTotal(nota.getTotal());
        List<NotaCompraProductosDTO> productos = new ArrayList<>();

        nota.getLote().forEach(lotesModel -> {
                    NotaCompraProductosDTO producto = new NotaCompraProductosDTO();
                    producto.setArticulo(lotesModel.getArticulo().getNombre());
                    producto.setCantidad(lotesModel.getCantidad());
                    producto.setPrecio(lotesModel.getPrecio_compra());
                    producto.setFecha_vencimiento(lotesModel.getFecha_vencimiento());
                    producto.setSubtotal(lotesModel.getCantidad() * lotesModel.getPrecio_compra());
                    productos.add(producto);
                }
        );
        dto.setDetalles(productos);
        return dto;
    }

    public NotaModel crear_venta(CrearNotaVentaRequest data) {
        EmpresaModel empresa = empresaRepository.findById(data.getEmpresa_id()).orElseThrow(
                () -> new RuntimeException("Empresa no encontrada")
        );
        ComprobanteModel comprobante = null;

        Float total = data.getLotes().stream().map(l -> l.getPrecio() * l.getCantidad()).reduce(0f, Float::sum);
        if (empresa.getCuentaIntegracion().size() > 0 && empresa.getCuentaIntegracion().get(0).getEstado() == "Activo") {
            //Crear comprobante
        }
        NotaModel nota = new NotaModel();
        nota.setDescripcion(data.getDescripcion());
        nota.setEmpresa(empresa);
        nota.setFecha(data.getFecha());
        String nro = String.valueOf(notasRepository.countByEmpresaIdAndTipo(data.getEmpresa_id(), "VENTA") + 1);
        nota.setNro_nota(nro);
        nota.setEstado("ACTIVO");
        nota.setTipo("VENTA");
        nota.setTotal(total);
        nota.setUsuario(empresa.getUsuario());
        if (comprobante != null) {
            nota.setComprobante(comprobante);
        }
        notasRepository.save(nota);

        List<DetallesModel> detalles = new ArrayList<>();
        data.getLotes().forEach(lote -> {
            DetallesModel detalle = new DetallesModel();
            LotesModel loteModel = lotesRepository.findById(lote.getLote_id()).orElseThrow(
                    () -> new RuntimeException("Lote no encontrado")
            );
            if (loteModel.getStock() < lote.getCantidad()) {
                notasRepository.delete(nota);
                if (comprobante != null) {
                    comprobanteRepository.delete(comprobante);
                }
                throw new RuntimeException("No hay stock suficiente");
            }
            detalle.setLote(loteModel);
            detalle.setCantidad(lote.getCantidad());
            detalle.setPrecio_venta(lote.getPrecio());
            detalle.setNota(nota);
            detallesRepository.save(detalle);
            loteModel.setStock((float) (loteModel.getStock() - lote.getCantidad()));
            ArticuloModel art = articuloRepository.findById(loteModel.getArticulo().getId()).orElseThrow(
                    () -> new RuntimeException("Articulo no encontrado")
            );
            art.setStock((int) (art.getStock() - lote.getCantidad()));
            lotesRepository.save(loteModel);
        });
        return nota;
    }

    public NotaVentaDTO una_nota_venta(String notaId) {

        NotaModel nota = notasRepository.findById(Long.parseLong(notaId)).orElseThrow(
                () -> new RuntimeException("Nota no encontrada")
        );
        NotaVentaDTO dto = new NotaVentaDTO();
        dto.setId(nota.getId());
        dto.setFecha(nota.getFecha());
        dto.setNro_nota(nota.getNro_nota());
        dto.setEstado(nota.getEstado());
        dto.setDescripcion(nota.getDescripcion());
        dto.setTotal(nota.getTotal());
        List<NotaVentaDetallesDTO> productos = new ArrayList<>();

        nota.getDetalle().forEach(detalle -> {
            NotaVentaDetallesDTO producto = new NotaVentaDetallesDTO();
            producto.setNombre_articulo(detalle.getLote().getArticulo().getNombre());
            producto.setCantidad(detalle.getCantidad());
            producto.setLote(detalle.getLote().getNro_lote());
            producto.setPrecio(detalle.getPrecio_venta());
            producto.setSubtotal(detalle.getCantidad() * detalle.getPrecio_venta());
            productos.add(producto);
        });
        dto.setDetalles(productos);
        return dto;
    }

    public NotaModel anular_venta(String notaId) {
        NotaModel nota = notasRepository.findById(Long.parseLong(notaId)).orElseThrow(
                () -> new RuntimeException("Nota no encontrada")
        );
        if (nota.getEstado().equals("ANULADO")) {
            throw new RuntimeException("Nota ya anulada");
        }
        nota.setEstado("ANULADO");
        //From each detail (Lote) remove stock
        //los detalles no, en los lotes
        nota.getDetalle().forEach(detalle -> {
            LotesModel lote = detalle.getLote();
            lote.setStock(lote.getStock() + detalle.getCantidad());
            lotesRepository.save(lote);
            //Restablecer lote de articulo
            ArticuloModel art = articuloRepository.findById(lote.getArticulo().getId()).orElseThrow(
                    () -> new RuntimeException("Articulo no encontrado")
            );
            art.setStock((int) (art.getStock() + detalle.getCantidad()));
        });
        if (nota.getComprobante() != null) {
            ComprobanteModel comp = nota.getComprobante();
            comp.setEstado("Anulado");
            comprobanteRepository.save(comp);
        }

        return notasRepository.save(nota);
    }
}
