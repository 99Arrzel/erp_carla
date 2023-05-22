package com.carla.erp_senseve.controllers;


import com.carla.erp_senseve.services.CategoriaService;
import com.carla.erp_senseve.validate.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
    @Autowired
    CategoriaService categoriaService;
    @PostMapping(value = "/crear")
    public ResponseEntity<?> crear(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ){
        try{
                //nombre, descripcion, empresa_id, categoria_id
            return ResponseEntity.ok().body(categoriaService.crear(
                    data.get("nombre"),
                    data.get("descripcion"),
                    data.get("empresa_id"),
                    data.get("categoria_id"),
                    authHeader
            ));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }
    @PostMapping(value = "/editar")
    public ResponseEntity<?> editar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> data
    ){
        try{
            //nombre, descripcion, empresa_id, categoria_id
            return ResponseEntity.ok().body(categoriaService.actualizar(
                    data.get("id"),
                    data.get("nombre"),
                    data.get("descripcion")
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
            return ResponseEntity.ok().body(categoriaService.ListaUsuarioEmpresaCategoria(authHeader, data.get("empresa_id")));
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
            return ResponseEntity.ok().body(categoriaService.eliminar(data.get("id")));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage().toString()));
        }
    }


}
