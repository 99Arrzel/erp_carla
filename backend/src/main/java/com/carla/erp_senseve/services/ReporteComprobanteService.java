package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReporteComprobanteService {
    @Autowired
    ComprobanteService comprobanteService;
    @Autowired
    DetallesService detallesService;
    @Autowired
    GestionService gestionService;
    @Autowired
    MonedaService monedaService;
    @Autowired
    CuentaService cuentaService;
    @Autowired
    PeriodoService periodoService;
    public ReporteComprobanteModel reporte_comprobante(String id_comprobante) {
        ReporteComprobanteModel reporteComprobanteModel = new ReporteComprobanteModel();
        //Fetch data from database using id_comprobante
        ComprobanteModel comprobanteModel = comprobanteService.obtener(id_comprobante);
        reporteComprobanteModel.setSerie(comprobanteModel.getSerie());
        reporteComprobanteModel.setEstado(comprobanteModel.getEstado());
        reporteComprobanteModel.setTipo(comprobanteModel.getTipo());
        reporteComprobanteModel.setFecha(comprobanteModel.getFecha());
        reporteComprobanteModel.setGlosa(comprobanteModel.getGlosa());
        reporteComprobanteModel.setMoneda(comprobanteModel.getMoneda().getNombre());
        reporteComprobanteModel.setUsuario(comprobanteModel.getUsuario().getNombre());
        reporteComprobanteModel.setEmpresa(comprobanteModel.getEmpresa().getNombre());
        reporteComprobanteModel.setCambio(comprobanteModel.getTc());
        List<comprobante_detalles> comprobante_detalles_list = new ArrayList<>();
        comprobanteModel.getDetalles().forEach( detalle -> {
            System.out.println(detalle.getNumero());
            System.out.println(detalle.getGlosa());
            comprobante_detalles nuevoDetalle = new comprobante_detalles();
            nuevoDetalle.setNumero(detalle.getNumero());
            nuevoDetalle.setGlosa(detalle.getGlosa());
            nuevoDetalle.setMonto_debe(detalle.getMonto_debe());
            nuevoDetalle.setMonto_haber(detalle.getMonto_haber());
            nuevoDetalle.setNombreCuenta(detalle.getCuenta().getNombre());
            nuevoDetalle.setCodigoCuenta(detalle.getCuenta().getCodigo());
            comprobante_detalles_list.add(nuevoDetalle);
          }
        );
        reporteComprobanteModel.setComprobante_detalles(comprobante_detalles_list);
        return reporteComprobanteModel;
    }

    public ReporteBalanceInicialModel reporte_balance_inicial(String idMoneda, String idGestion) {
        ReporteBalanceInicialModel reporteComprobanteModel = new ReporteBalanceInicialModel();
        //Fetch data from database using id_comprobante

        //Primero la gestion
        GestionModel gestion = gestionService.obtenerGestion(Long.parseLong(idGestion));
        MonedaModel moneda = monedaService.obtenerMoneda(Long.parseLong(idMoneda));

        //Buscamos el primer comprobante de apertura en gestion.fecha_inicio y gestion_fecha_fin con estado Abierto
        ComprobanteModel comprobanteModel = comprobanteService.obtenerComprobanteApertura( gestion.getFechaInicio(), gestion.getFechaFin(), gestion.getEmpresa().getId());

        //La diferencia de este reporte es que es por cuentas, las cuentas deben ser ordenadas por codigo y traer desde la base con padre null hasta el hijo que es el último, que es el detalle que tiene el comprobante

        List<CuentaModel> cuentas = cuentaService.getCuentasActivoPasivoPatrimonio(gestion.getEmpresa().getId());

        //Iteramos sobre las cuentas y removemos de ellas, los detalles que no le pertenecen al comprobante, es decir, comprobante_id != comprobanteModel.getId()
        cuentas.forEach(cuenta -> {
            cuenta.getDetalle_comprobantes().removeIf(detalle -> {
                        return detalle.getComprobante().getId() != comprobanteModel.getId();
                    }
            );
        });
        //Ahora para cada cuenta, si es que tiene detalle_comprobantes mayor a 1, que solamente es 1, lo que haremos es ponerlo en "unicodetalle"
        cuentas.forEach(cuenta -> {
            if (cuenta.getDetalle_comprobantes().size() > 0) {
                cuenta.setUnicoDetalle(cuenta.getDetalle_comprobantes().get(0));
            }
        });
        //Removemos lo que es detalle_comprobantes, ya que no lo necesitamos
        cuentas.forEach(cuenta -> {
            cuenta.setDetalle_comprobantes(null);
            //Ademas, sumamos el debe y el haber de unicodetalle y lo ponemos en total_balance_inicial
            if (cuenta.getUnicodetalle() != null) {
                cuenta.setTotal_balance_inicial(Math.abs(cuenta.getUnicodetalle().getMonto_debe() - cuenta.getUnicodetalle().getMonto_haber()));
            }
        });
        //Finalmente, las cuentas son de padre a hijo y lo que debemos hacer es iterar sobre cuentas en reversa y sumar el total_balance_inicial de los hijos al padre
        for (int i = cuentas.size() - 1; i >= 0; i--) {
            CuentaModel cuenta = cuentas.get(i);
            if (cuenta.getPadre() != null) {
                //cuenta.getPadre().setTotal_balance_inicial(cuenta.getPadre().getTotal_balance_inicial() + cuenta.getTotal_balance_inicial()); hay que verificar que getTotal no sea null
                if (cuenta.getPadre().getTotal_balance_inicial() != null) {
                    cuenta.getPadre().setTotal_balance_inicial(cuenta.getPadre().getTotal_balance_inicial() + cuenta.getTotal_balance_inicial());
                } else {
                    cuenta.getPadre().setTotal_balance_inicial(cuenta.getTotal_balance_inicial());
                }
            }
        }
        //Finalmente, multiplicamos total_balance_inical por tc solo si es que la moneda es distinta a la moneda de la empresa
        //Primero obtenemos la moneda de la empresa
        MonedaModel monedaEmpresa = monedaService.obtenerMonedaEmpresa(gestion.getEmpresa().getId());
        if (monedaEmpresa.getId() != moneda.getId()) {
            cuentas.forEach(cuenta -> {
                if (cuenta.getTotal_balance_inicial() != null) {
                    cuenta.setTotal_balance_inicial(cuenta.getTotal_balance_inicial() * comprobanteModel.getTc());
                }
            });
        }

        reporteComprobanteModel.setCuentas(cuentas);
        reporteComprobanteModel.setGestion(gestion);
        reporteComprobanteModel.setUsuario(comprobanteModel.getUsuario().getNombre());
        reporteComprobanteModel.setSerie(comprobanteModel.getSerie());
        reporteComprobanteModel.setEstado(comprobanteModel.getEstado());
        reporteComprobanteModel.setTipo(comprobanteModel.getTipo());
        reporteComprobanteModel.setFecha(comprobanteModel.getFecha());
        reporteComprobanteModel.setGlosa(comprobanteModel.getGlosa());
        reporteComprobanteModel.setMoneda(comprobanteModel.getMoneda().getNombre());
        reporteComprobanteModel.setUsuario(comprobanteModel.getUsuario().getNombre());
        reporteComprobanteModel.setEmpresa(comprobanteModel.getEmpresa().getNombre());
        reporteComprobanteModel.setCambio(comprobanteModel.getTc());
        return reporteComprobanteModel;

    }

    public ReporteComprobanteLibroDiarioModel reporte_libro_diario(String idMoneda, String idPeriodo, String todos) {
        //Este es un reporte de comprobantes en un periodo de tiempo, es decir, desde fecha_inicio hasta fecha_fin, que puede  ser de cierto periodo o  gestion
        //La gestión es opcional, si es que se quiere ver todos los comprobantes desde el inicio de la gestion hasta la fecha_fin
        ReporteComprobanteLibroDiarioModel reporteComprobanteLibroDiarioModel = new ReporteComprobanteLibroDiarioModel();
        Date fecha_inicio ;
        Date fecha_fin;
        PeriodoModel periodo = null;

        MonedaModel moneda = monedaService.obtenerMoneda(Long.parseLong(idMoneda));
        periodo = periodoService.obtenerPeriodo(Long.parseLong(idPeriodo));
        if(periodo == null){
            throw new RuntimeException("No existe el periodo");
        }
        //if(todos.equals() == "SI"){ Esto no está funcionando
        if(todos != null && todos.equals("SI")){
            fecha_inicio = periodo.getGestion().getFechaInicio();
            fecha_fin = periodo.getGestion().getFechaFin();
            reporteComprobanteLibroDiarioModel.setTodos_periodos("SI");
        }else{
            reporteComprobanteLibroDiarioModel.setTodos_periodos("NO");
            fecha_inicio = periodo.getFechaInicio();
            fecha_fin = periodo.getFechaFin();
        }
        System.out.println("Esta basura imprime todos:" + todos);
        System.out.println(reporteComprobanteLibroDiarioModel.getTodos_periodos());

        List<ComprobanteModel> comprobantes = comprobanteService.obtenerComprobantesInicioFinEmpresa(fecha_inicio, fecha_fin, periodo.getGestion().getEmpresa().getId());
        //Ahora con los comprobantes armamos los detalles
        List<DetallesLibroDiario> detallesLibroDiario = new ArrayList<>();
        AtomicReference<Float> totalDebe = new AtomicReference<>(0f);
        AtomicReference<Float> totalHaber = new AtomicReference<>(0f);
        comprobantes.forEach(comprobante -> {
            DetallesLibroDiario nuevoDetalle = new DetallesLibroDiario();
            nuevoDetalle.setFecha(comprobante.getFecha());
            nuevoDetalle.setCodigo("");
            nuevoDetalle.setCuenta(comprobante.getGlosa());
            nuevoDetalle.setDebe(0f);
            nuevoDetalle.setHaber(0f);
            detallesLibroDiario.add(nuevoDetalle);
            comprobante.getDetalles().forEach(detalle -> {
                DetallesLibroDiario nuevoDetalle2 = new DetallesLibroDiario();
                if(detalle.getComprobante().getMoneda().getId() != moneda.getId()) {
                    nuevoDetalle2.setDebe(detalle.getMonto_debe() * detalle.getComprobante().getTc());
                    nuevoDetalle2.setHaber(detalle.getMonto_haber() * detalle.getComprobante().getTc());
                }else{
                    nuevoDetalle2.setDebe(detalle.getMonto_debe());
                    nuevoDetalle2.setHaber(detalle.getMonto_haber());
                }
                totalDebe.updateAndGet(v -> v + nuevoDetalle2.getDebe());
                totalHaber.updateAndGet(v -> v + nuevoDetalle2.getHaber());
                nuevoDetalle2.setFecha(comprobante.getFecha());
                nuevoDetalle2.setCodigo(detalle.getCuenta().getCodigo());
                nuevoDetalle2.setCuenta(detalle.getCuenta().getNombre());
                detallesLibroDiario.add(nuevoDetalle2);

            });
        });

        reporteComprobanteLibroDiarioModel.setDetalles(detallesLibroDiario);
        reporteComprobanteLibroDiarioModel.setTotalDebe(totalDebe.get());
        reporteComprobanteLibroDiarioModel.setTotalHaber(totalHaber.get());
        reporteComprobanteLibroDiarioModel.setPeriodo(periodo);
        GestionModel gestion = periodo.getGestion();

        reporteComprobanteLibroDiarioModel.setEmpresa(gestion.getEmpresa().getNombre());
        reporteComprobanteLibroDiarioModel.setUsuario(periodo.getUsuario().getNombre());
        reporteComprobanteLibroDiarioModel.setMoneda(moneda.getNombre());

        reporteComprobanteLibroDiarioModel.setGestion(gestion.getNombre());
        return reporteComprobanteLibroDiarioModel;
    }

    public ReporteComprobanteLibroMayorModel reporte_libro_mayor(String idMoneda, String idPeriodo, String todos) {
        //Esto es conseguir las cuentas, ordenarlas por numeros, de estas sacar los detalles y tener debe haber con un saldo que es siempre la resta de estos en un math.abs
        ReporteComprobanteLibroMayorModel reporteComprobanteLibroMayorModel = new ReporteComprobanteLibroMayorModel();
        Date fecha_inicio ;
        Date fecha_fin;
        PeriodoModel periodo = periodoService.obtenerPeriodo(Long.parseLong(idPeriodo));
        if(periodo == null){
            throw new RuntimeException("No existe el periodo");
        }
        if(todos != null && todos.equals("SI")) {
            reporteComprobanteLibroMayorModel.setTodos_periodos("SI");
            fecha_inicio = periodo.getGestion().getFechaInicio();
            fecha_fin = periodo.getGestion().getFechaFin();
        }
        else{
            reporteComprobanteLibroMayorModel.setTodos_periodos("NO");
            fecha_inicio = periodo.getFechaInicio();
            fecha_fin = periodo.getFechaFin();
        }
        MonedaModel moneda = monedaService.obtenerMoneda(Long.parseLong(idMoneda));

        //Solo se busca un debe y haber por DetalleComprobantesLibroMayor, que también tiene un saldo
        //Pero antes, debemos encontrar los comprobantes entre las fechas
        List<ComprobanteModel> comprobantes = comprobanteService.obtenerComprobantesInicioFinEmpresa(fecha_inicio, fecha_fin, periodo.getGestion().getEmpresa().getId());
        //Ahora las cuentas
        List<CuentaModel> cuentas = cuentaService.obtenerCuentasPorEmpresa(periodo.getGestion().getEmpresa().getId());
        //Ahora los detalles
        List<Long> idsComprobantes = new ArrayList<>();
        comprobantes.forEach(comprobante -> {
            idsComprobantes.add(comprobante.getId());
        });
        System.out.println("Estos son los ids de los comprobantes");
        idsComprobantes.forEach(System.out::println);
        cuentas.forEach(cuenta -> {
            cuenta.getDetalle_comprobantes().removeIf(detalle -> {
                return !idsComprobantes.contains(detalle.getComprobante_id());
            });
        });
        //Borramos las cuentas con detalles vacíos
        cuentas.removeIf(cuenta -> cuenta.getDetalle_comprobantes().isEmpty());
        //Ahora ordenamos todo en el modelo  de ReporteComprobanteLibroMayorModel

        //Dentro de este, están los DetallesLibroMayor
        List<DetallesLibroMayor> detallesLibroMayor = new ArrayList<>();
        //Ahora simplemente iteramos sobre las cuentas y las añadimos
        AtomicReference<Float> totalDebe = new AtomicReference<>(0f);
        AtomicReference<Float> totalHaber = new AtomicReference<>(0f);
        AtomicReference<Float> saldo = new AtomicReference<>(0f);
        cuentas.forEach(cuentaModel -> {
                    DetallesLibroMayor nuevoDetalle = new DetallesLibroMayor();
                    nuevoDetalle.setCodigo(cuentaModel.getCodigo());
                    nuevoDetalle.setCuenta(cuentaModel.getNombre());
                    List<DetalleComprobantesLibroMayor> detalles = new ArrayList<>();
                    //Ahora hay que añadirle los detalles del detalle, por lo que iteramos en getDetalle_comprobantes

                    cuentaModel.getDetalle_comprobantes().forEach(detalleComprobanteModel -> {
                        System.out.println(detalleComprobanteModel.getGlosa());
                        DetalleComprobantesLibroMayor nuevoDetalleComprobante = new DetalleComprobantesLibroMayor();
                        nuevoDetalleComprobante.setFecha(detalleComprobanteModel.getComprobante().getFecha());
                        nuevoDetalleComprobante.setNumero(detalleComprobanteModel.getComprobante().getSerie());
                        nuevoDetalleComprobante.setTipo(detalleComprobanteModel.getComprobante().getTipo());
                        nuevoDetalleComprobante.setDebe(detalleComprobanteModel.getMonto_debe());
                        nuevoDetalleComprobante.setGlosa(detalleComprobanteModel.getGlosa());
                        nuevoDetalleComprobante.setHaber(detalleComprobanteModel.getMonto_haber());
                        saldo.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_debe() - detalleComprobanteModel.getMonto_haber()));
                        nuevoDetalleComprobante.setSaldo(saldo.get());
                        totalDebe.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_debe()));
                        totalHaber.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_haber()));

                        detalles.add(nuevoDetalleComprobante);
                    });
                    nuevoDetalle.setDetalles(detalles);
                    detallesLibroMayor.add(nuevoDetalle);
                }
        );
        reporteComprobanteLibroMayorModel.setTotalDebe(totalDebe.get());
        reporteComprobanteLibroMayorModel.setTotalHaber(totalHaber.get());
        reporteComprobanteLibroMayorModel.setDetalles(detallesLibroMayor);
        reporteComprobanteLibroMayorModel.setPeriodo(periodo);
        reporteComprobanteLibroMayorModel.setMoneda(moneda.getNombre());
        reporteComprobanteLibroMayorModel.setEmpresa(periodo.getGestion().getEmpresa().getNombre());
        reporteComprobanteLibroMayorModel.setUsuario(periodo.getUsuario().getNombre());
        reporteComprobanteLibroMayorModel.setGestion(periodo.getGestion().getNombre());
        return reporteComprobanteLibroMayorModel;
    }

    public ReporteSumasSaldosModel report_sumas_saldos(String idMoneda, String idGestion) {
        GestionModel gestion = gestionService.obtenerGestion(Long.parseLong(idGestion));
        MonedaModel moneda = monedaService.obtenerMoneda(Long.parseLong(idMoneda));
        ReporteSumasSaldosModel reporteSumasSaldosModel = new ReporteSumasSaldosModel();
        reporteSumasSaldosModel.setGestion(gestion.getNombre());
        reporteSumasSaldosModel.setMoneda(moneda.getNombre());
        reporteSumasSaldosModel.setEmpresa(gestion.getEmpresa().getNombre());
        reporteSumasSaldosModel.setUsuario(gestion.getUsuario().getNombre());
        //detalles
        List<ComprobanteModel> comprobante = comprobanteService.obtenerComprobantesInicioFinEmpresa(gestion.getFechaInicio(), gestion.getFechaFin(), gestion.getEmpresa().getId());

        List<Long> idsComprobantes = new ArrayList<>();
        comprobante.forEach(comprobanteModel -> {
            idsComprobantes.add(comprobanteModel.getId());
        });
        //Ahora las cuentas
        List<CuentaModel> cuentas = cuentaService.obtenerCuentasPorEmpresa(gestion.getEmpresa().getId());
        //De las cuentas, los detalles que este
        cuentas.forEach(cuentaModel -> {
            cuentaModel.getDetalle_comprobantes().removeIf(detalle -> {
                return !idsComprobantes.contains(detalle.getComprobante_id());
            });
        });
        //Ahora removemos las cuentas con detalles vacios
        cuentas.removeIf(cuentaModel -> cuentaModel.getDetalle_comprobantes().isEmpty());
        //Ahora ordenamos todo en el modelo  de reporteSumasSaldos
//detalleComprobanteModel.getComprobante().getTc()
        List<DetallesSumasSaldos> detallesSumasSaldosList = new ArrayList<>();
        cuentas.forEach(cuentaModel -> {
            DetallesSumasSaldos detallesSumasSaldos = new DetallesSumasSaldos();
            detallesSumasSaldos.setCuenta(cuentaModel.getCodigo() + " - " + cuentaModel.getNombre());
            //Hacer la suma de todos los debe y haber de los detalles
            AtomicReference<Float> totalDebe = new AtomicReference<>(0f);
            AtomicReference<Float> totalHaber = new AtomicReference<>(0f);
            cuentaModel.getDetalle_comprobantes().forEach(detalleComprobanteModel -> {
                //El total lo multiplicamos si el idMoneda es igual al id del comprobante
                if (detalleComprobanteModel.getComprobante().getMoneda().getId() == Long.parseLong(idMoneda)) {
                    totalDebe.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_debe() * detalleComprobanteModel.getComprobante().getTc()));
                    totalHaber.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_haber() * detalleComprobanteModel.getComprobante().getTc()));
                } else {
                    totalDebe.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_debe()));
                    totalHaber.updateAndGet(v -> (float) (v + detalleComprobanteModel.getMonto_haber()));
                }
            });
            detallesSumasSaldos.setDebe_suma(totalDebe.get());
            detallesSumasSaldos.setHaber_suma(totalHaber.get());
            //ahora el debe_saldo es sumas_debe - sumas_haber
            //también el haber_saldo es sumas_haber - sumas_debe
            if(totalDebe.get() > totalHaber.get()){
                detallesSumasSaldos.setDebe_saldo(Math.abs(totalDebe.get() - totalHaber.get()));
                detallesSumasSaldos.setHaber_saldo(0f);
            }else if(totalDebe.get() < totalHaber.get()){
                detallesSumasSaldos.setDebe_saldo(0f);
                detallesSumasSaldos.setHaber_saldo(Math.abs(totalHaber.get() - totalDebe.get()));
            }

            detallesSumasSaldosList.add(detallesSumasSaldos);
        });
        reporteSumasSaldosModel.setDetalles(detallesSumasSaldosList);
        return reporteSumasSaldosModel;
    }
}
