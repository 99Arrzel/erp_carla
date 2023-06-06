package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotaVentaDetallesDTO {
    public String nombre_articulo;
    public String lote;
    public Float cantidad;
    public Float precio;
    public Float subtotal;
}
