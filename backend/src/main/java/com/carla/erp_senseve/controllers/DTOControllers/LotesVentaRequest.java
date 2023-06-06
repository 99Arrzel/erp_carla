package com.carla.erp_senseve.controllers.DTOControllers;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotesVentaRequest {
    public Long lote_id;
    public Float cantidad;
    public Float precio;
}
