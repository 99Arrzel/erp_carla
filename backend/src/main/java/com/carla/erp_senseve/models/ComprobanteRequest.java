package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;


@Getter
@Setter
public class ComprobanteRequest {

    private String glosa;
    private Date fecha;
    private float tc;
    private String tipo;
    private long empresa_id;
    private long moneda_id;
    private List<DetalleComprobanteRequest> detalles;

}