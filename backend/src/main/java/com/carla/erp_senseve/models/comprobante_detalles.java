package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class comprobante_detalles {
    private String numero;
    private String glosa;
    private Float monto_debe;
    private Float monto_haber;
    private String nombreCuenta;
    private String codigoCuenta;
}
