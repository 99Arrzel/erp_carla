package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class DetallesLibroDiario {
    public Date fecha;
    public String codigo;
    public String cuenta;
    public Float debe;
    public Float haber;
}
