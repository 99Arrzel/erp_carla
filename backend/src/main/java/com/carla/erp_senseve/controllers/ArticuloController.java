package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.controllers.DTOControllers.ArticuloRequest;
import com.carla.erp_senseve.services.ArticuloService;
import com.carla.erp_senseve.validate.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/articulo")
public class ArticuloController {
    @Autowired
    ArticuloService articuloService;

    @PostMapping(value = "/crear")
    public ResponseEntity<?> crear(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ArticuloRequest data
            ){
        try{
            //nombre, descripcion, empresa_id, categoria_id
            return ResponseEntity.ok().body(articuloService.crear(
                    data.getNombre(),
                    data.getDescripcion(),
                    data.getPrecio(),
                    data.getCategorias(),
                    data.getEmpresa_id(),
                    authHeader
            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @PostMapping(value = "/editar")
    public ResponseEntity<?> editar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ArticuloRequest data
    ){
        try{
            //nombre, descripcion, empresa_id, categoria_id
            return ResponseEntity.ok().body(articuloService.editar(
                    data.getId(),
                    data.getNombre(),
                    data.getDescripcion(),
                    data.getPrecio(),
                    data.getCategorias()

            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @PostMapping(value = "/listar")
    public ResponseEntity<?> listar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ){
        try{
            return ResponseEntity.ok().body(articuloService.ListarDeUsuarioPorEmpresa(authHeader, data.get("empresa_id")));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    //Eliminar
    @PostMapping(value = "/eliminar")
    public ResponseEntity<?> eliminar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ){
        try{
            return ResponseEntity.ok().body(articuloService.eliminar(Long.parseLong(data.get("id"))));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
}
