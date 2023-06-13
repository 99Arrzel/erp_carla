package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaParaReporteBalanceInicial {
    public Long id;
    public Long id_padre;
    public String codigo;
    public String nombre;
    public Float saldo;
    public Integer nivel;
}
