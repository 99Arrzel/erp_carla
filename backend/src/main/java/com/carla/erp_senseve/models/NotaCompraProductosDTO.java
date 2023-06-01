package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NotaCompraProductosDTO {
    String articulo;
    Date fecha_vencimiento;
    Float cantidad;
    Float precio;
    Float subtotal;
}
