package com.carla.erp_senseve.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetallesLibroMayor {
    public String cuenta;
    public String codigo;
    public List<DetalleComprobantesLibroMayor> detalles;

}
