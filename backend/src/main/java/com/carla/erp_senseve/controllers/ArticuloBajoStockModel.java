package com.carla.erp_senseve.controllers;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticuloBajoStockModel {
    public String empresa;
    public String usuario;
    public String categoria;
    public List<ArticulosBajoStock> articulos;
}
