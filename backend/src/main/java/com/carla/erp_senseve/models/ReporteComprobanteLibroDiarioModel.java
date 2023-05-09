package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.Period;
import java.util.List;

@Getter
@Setter
public class ReporteComprobanteLibroDiarioModel {
    public String todos_periodos;
    public moneda moneda;
    public usuario usuario;
    public empresa empresa;
    public gestion gestionNombre;
    public PeriodoModel periodo;
    public List<DetallesLibroDiario> detalles;
    public Float totalDebe;
    public Float totalHaber;
    public void setGestion(String nombre) {
        gestion gestion = new gestion();
        gestion.setNombre(nombre);
        this.gestionNombre = gestion;
    }
    public void setMoneda(String nombre) {
        moneda moneda = new moneda();
        moneda.setNombre(nombre);
        this.moneda = moneda;
    }
    public void setUsuario(String nombre) {
        usuario usuario = new usuario();
        usuario.setNombre(nombre);
        this.usuario = usuario;
    }
    public void setEmpresa(String nombre) {
        empresa empresa = new empresa();
        empresa.setNombre(nombre);
        this.empresa = empresa;
    }
}
@Getter
@Setter
class gestion {
    public String nombre;
}