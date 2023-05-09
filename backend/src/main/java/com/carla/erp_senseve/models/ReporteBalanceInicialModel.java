package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
public class ReporteBalanceInicialModel {
    public String serie;
    public String estado;
    public String tipo;
    public Float cambio;
    public Date fecha;
    public String glosa;
    public moneda moneda;
    public usuario usuario;
    public empresa empresa;
    public List<CuentaModel> cuentas;
    public GestionModel gestion;

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


