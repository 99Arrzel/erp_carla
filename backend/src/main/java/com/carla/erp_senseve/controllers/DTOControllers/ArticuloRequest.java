package com.carla.erp_senseve.controllers.DTOControllers;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ArticuloRequest {
    public Long id;
    public String nombre;
    public String descripcion;
    public Float precio;
    public Long empresa_id;
    public List<Long> categorias;

}
