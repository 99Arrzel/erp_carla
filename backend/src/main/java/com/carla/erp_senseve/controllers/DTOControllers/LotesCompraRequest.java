package com.carla.erp_senseve.controllers.DTOControllers;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LotesCompraRequest {
    public Long articulo_id;
    public Float cantidad;
    public Float precio;
    public Date fecha_vencimiento;


}
