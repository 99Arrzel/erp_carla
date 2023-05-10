package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallesSumasSaldos {
    public String cuenta;
    public Float debe_suma;
    public Float haber_suma;
    public Float debe_saldo;
    public Float haber_saldo;
}
