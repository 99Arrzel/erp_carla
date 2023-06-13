package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class ReporteBalanceGeneralModel {
    public String moneda;
    public String usuario;
    public String empresa;
    public List<CuentaParaReporteBalanceInicial> cuentas;
    public String gestion;
    public Date gestion_inicio;
    public Date gestion_fin;
}
