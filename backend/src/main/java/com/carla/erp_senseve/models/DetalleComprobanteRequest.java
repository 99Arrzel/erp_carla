package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalleComprobanteRequest {
    private String glosa;
    private long cuenta_id;
    private Float debe;
    private Float haber;

}