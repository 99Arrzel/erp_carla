package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReporteSumasSaldosModel {
    public empresa empresa;
    public moneda moneda;
    public gestion gestion;
    public usuario usuario;
    public List<DetallesSumasSaldos> detalles;
    public void setGestion(String nombre) {
        gestion gestion = new gestion();
        gestion.setNombre(nombre);
        this.gestion = gestion;
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

