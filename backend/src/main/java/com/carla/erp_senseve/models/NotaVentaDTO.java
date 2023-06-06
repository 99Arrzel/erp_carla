package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class NotaVentaDTO {

    public String nro_nota;
    public Date fecha;
    public String descripcion;
    public Long id;
    public List<NotaVentaDetallesDTO> detalles;
    public String estado;
    public Float total;

}
