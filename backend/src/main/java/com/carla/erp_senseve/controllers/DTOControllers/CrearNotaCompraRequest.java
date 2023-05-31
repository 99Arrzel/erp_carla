package com.carla.erp_senseve.controllers.DTOControllers;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CrearNotaRequest {
    public Date fecha;
    public String descripcion;
    public Long empresa_id;
    public List<LotesCompraRequest> lotes;

    public List<LotesCompraRequest> getLotes() {
        return lotes;
    }
}
