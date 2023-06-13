package com.carla.erp_senseve.controllers;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class ReporteEstadoResultadosModel {

    public String empresa;
    public String usuario;
    public Date fecha;
    public List<EstadoResultado> ingresos;
    public Float total_ingresos;
    public List<EstadoResultado> costos;
    public Float total_costos;
    public List<EstadoResultado> gastos;
    public Float total_gastos;
    public Float utilidad_operativa; // ingresos - costos - gastos
    public Float utilidad_bruta; // ingresos - costos
    public String moneda;
}
