package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.*;
import com.carla.erp_senseve.repositories.ComprobanteRepository;
import com.carla.erp_senseve.repositories.DetalleComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ComprobanteService {
    @Autowired
    ComprobanteRepository comprobanteRepository;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    PeriodoService periodoService;
    @Autowired
    DetalleComprobanteRepository detalleComprobanteRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    MonedaService monedaService;
    @Autowired
    EmpresaService empresaService;
    @Autowired
    CuentaService cuentaService;
    @Autowired
    GestionService gestionService;
    public List<ComprobanteModel> listar(String empresa_id, String authHeader) throws Exception {
        String usuario = tokenGetUserService.username(authHeader);
        Long idUsuario = usuarioService.obtenerPorUsuario(usuario).orElseThrow(
                () -> new RuntimeException("No existe usuario")
        ).getId();
        List <ComprobanteModel> comprobantes = comprobanteRepository.findByEmpresaIdAndUsuarioId(Long.valueOf(empresa_id), idUsuario);
        comprobantes.forEach(comprobante -> {
            comprobante.setMoneda_nombre(comprobante.getMoneda().getNombre());
        });
        return comprobantes;
    }
    public ComprobanteModel crear(ComprobanteRequest comprobante, String authHeader) throws Exception {

        /*
        Validar que el comprobante este en el RANGO de un periodo abierto
        * */
        boolean continuar = periodoService.hayPeriodoAbiertoEnEmpresaYFecha(comprobante.getEmpresa_id(), comprobante.getFecha());
        if(!continuar){
            throw new RuntimeException("No hay periodo abierto en la fecha del comprobante");
        }
        //Solo uno de tipo "Apertura" por gestión, así que vamos a gestión con esta fecha
        GestionModel gestion_b = gestionService.gestionEnFechaConIdEmpresa(comprobante.getFecha(), comprobante.getEmpresa_id());
        if (gestion_b == null){
            throw new RuntimeException("No existe gestión en esa fecha");
        }
        //Ahora vamos a buscar un comprobante en ese rango de fechaa_inicio y fecha_fin de la gestion, que sea de tipo apertura y esa empresa
        if(comprobante.getTipo().equals("Apertura")){
            ComprobanteModel comprobante_apertura = comprobanteRepository.buscarAperturaParaEmpresaEnFechaInicioYFechaFin(comprobante.getEmpresa_id(), gestion_b.getFechaInicio(), gestion_b.getFechaFin());
            if(comprobante_apertura != null){
                System.out.println("Ya existe un comprobante de apertura en esa gestion");
                System.out.println(comprobante_apertura);
                throw new RuntimeException("Ya existe un comprobante de apertura en esa gestion");
            }
        }
        ComprobanteModel comprobanteModel = new ComprobanteModel();
        comprobanteModel.setEstado("Abierto");
        comprobanteModel.setFecha(comprobante.getFecha());
        comprobanteModel.setGlosa(comprobante.getGlosa());
        //La serie es una contada de los que hay en db + 1
        comprobanteModel.setSerie(String.valueOf(comprobanteRepository.contar(comprobante.getEmpresa_id())));
        comprobanteModel.setTc(comprobante.getTc());
        comprobanteModel.setTipo(comprobante.getTipo());
        //empresa
        EmpresaModel empresa = empresaService.findById(comprobante.getEmpresa_id());
        comprobanteModel.setEmpresa(empresa);

        //usuario
        String usuario = tokenGetUserService.username(authHeader);
        //De ese usuario, buscar el id
        Long idUsuario = usuarioService.obtenerPorUsuario(usuario).orElseThrow(
                () -> new RuntimeException("No existe usuario")
        ).getId();
        //Set usuario
        comprobanteModel.setUsuario(usuarioService.findById(idUsuario));
        comprobanteModel.setUsuario_id(idUsuario);
        MonedaModel moneda = monedaService.monedaUsuario(comprobante.getMoneda_id());
        comprobanteModel.setMoneda(moneda);
        ComprobanteModel comprobanteCreado = comprobanteRepository.save(comprobanteModel);
        //Ahora los detalles
        AtomicReference<Integer> numero = new AtomicReference<>(1);
        comprobante.getDetalles().forEach(detalle -> {
            DetalleComprobanteModel detalleComprobanteModel = new DetalleComprobanteModel();
            detalleComprobanteModel.setGlosa(detalle.getGlosa());
            detalleComprobanteModel.setComprobante(comprobanteCreado);
            detalleComprobanteModel.setMonto_debe(detalle.getDebe());
            detalleComprobanteModel.setMonto_haber(detalle.getHaber());
            detalleComprobanteModel.setMonto_debe_alt(detalle.getDebe() * comprobante.getTc());
            detalleComprobanteModel.setMonto_haber_alt(detalle.getHaber() * comprobante.getTc());
            detalleComprobanteModel.setNumero(String.valueOf(numero));
            detalleComprobanteModel.setUsuario_id(idUsuario);
            detalleComprobanteModel.setUsuario(usuarioService.findById(idUsuario));


            detalleComprobanteModel.setCuenta(cuentaService.findById(detalle.getCuenta_id()));
            numero.getAndSet(numero.get() + 1);
            detalleComprobanteRepository.save(detalleComprobanteModel);
        });
        return comprobanteCreado;
    }

    public String ultimo_numero(Long empresaId) {
        return comprobanteRepository.ultimo_numero(empresaId);
    }

    public ComprobanteModel cerrar(Long id) {
        ComprobanteModel comprobante = comprobanteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No existe comprobante")
        );
        comprobante.setEstado("Cerrado");
        return comprobanteRepository.save(comprobante);
    }

    public ComprobanteModel anular(Long id) {
        ComprobanteModel comprobante = comprobanteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No existe comprobante")
        );
        comprobante.setEstado("Anulado");
        return comprobanteRepository.save(comprobante);
    }

    public ComprobanteModel obtener(String idComprobante) {
        return comprobanteRepository.findById(Long.valueOf(idComprobante)).orElseThrow(
                () -> new RuntimeException("No existe comprobante")
        );
    }

    public ComprobanteModel obtenerComprobanteApertura(Date fechaInicio, Date fechaFin, Long id_empresa) {
        var comprobante = comprobanteRepository.buscarAperturaParaEmpresaEnFechaInicioYFechaFin(id_empresa, fechaInicio, fechaFin);
        if(comprobante == null){
            throw new RuntimeException("No existe comprobante de apertura en ese rango de fechas, para esa empresa");
        }
        return comprobante;
    }
    public List<ComprobanteModel> obtenerComprobantesInicioFinEmpresa(Date fechaInicio, Date fechaFin, Long id_empresa) {
        return comprobanteRepository.buscarComprobantesParaEmpresaEnFechaInicioYFechaFin(id_empresa, fechaInicio, fechaFin);
    }
}
