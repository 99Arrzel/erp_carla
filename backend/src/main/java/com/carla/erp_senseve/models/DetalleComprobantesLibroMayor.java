package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class DetalleComprobantesLibroMayor {
    public Date fecha;
    public String numero;
    public String tipo;
    public String glosa;
    public Float debe;
    public Float haber;
    public Float saldo;

}
