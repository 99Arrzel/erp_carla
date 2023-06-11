package com.carla.erp_senseve.models;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class NotaCompraDTO {
    Long id;
    Date fecha;
    String numero;
    String estado;
    String descripcion;
    //Array de detalles
    List<NotaCompraProductosDTO> detalles;
    Float total;
    //Empresa
    String empresa_nombre;
    String usuario;
}
