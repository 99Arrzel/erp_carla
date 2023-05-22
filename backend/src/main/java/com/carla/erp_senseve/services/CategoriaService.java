package com.carla.erp_senseve.services;


import com.carla.erp_senseve.models.CategoriaModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.CategoriaRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    CategoriaRepository categoriaRepository;
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    EmpresaService empresaService;
    @Autowired
    EmpresaRepository empresaRepository;


    public List<CategoriaModel> todasPorId(List<Long> ids){
        return categoriaRepository.findAllById(ids);
    }
    public List<CategoriaModel> ListaUsuarioEmpresaCategoria(String token_usuario, String empresa_id){
        UsuarioModel usuario = usuarioRepository.findByUsuario(tokenGetUserService.username(token_usuario)).orElseThrow(
                () -> new RuntimeException("El usuario no existe")
        );
        EmpresaModel empresa = empresaRepository.findById(Long.parseLong(empresa_id)).orElseThrow(
                () -> new RuntimeException("La empresa no existe")
        );
        //Verificar que le pertenezca
        if(!empresa.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("La empresa no le pertenece");
        }
        return categoriaRepository.findByEmpresaId(empresa.getId());
    }
    public CategoriaModel crear(
            String nombre,
            String descripcion,
            String empresa_id,
            String categoria_id,
            String token_usuario
    ) {

        //Verificar nombre único
        if(categoriaRepository.findByNombreEnEmpresa(nombre, Long.parseLong(empresa_id)).isPresent()){
            throw new RuntimeException("El nombre ya existe");
        }


        String usuario = tokenGetUserService.username(token_usuario);
        UsuarioModel usuarioModel = usuarioRepository.findByUsuario(usuario).orElseThrow(
                () -> new RuntimeException("El usuario no existe")
        );
        CategoriaModel categoria_padre = null;
        if(categoria_id != null){
            categoria_padre = categoriaRepository.findById(Long.parseLong(categoria_id)).orElse(null);
            if(categoria_padre == null){
                throw new RuntimeException("La categoria no existe");
            }
        }
        EmpresaModel empresaModel = empresaRepository.findById(Long.parseLong(empresa_id)).orElseThrow(
                () -> new RuntimeException("La empresa no existe")
        );
        CategoriaModel categoriaModel = new CategoriaModel();
        categoriaModel.setNombre(nombre);
        categoriaModel.setDescripcion(descripcion);
        categoriaModel.setEmpresa(empresaModel);
        categoriaModel.setCategoria(categoria_padre);
        categoriaModel.setUsuario(usuarioModel);

        categoriaRepository.save(categoriaModel);

        return categoriaModel;
    }
    public CategoriaModel actualizar(String id, String nombre, String descripcion){

        //Al actualizar, verificar que no repita nombre



        CategoriaModel categoriaModel = categoriaRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new RuntimeException("La categoria no existe")
        );


        //Buscamos uno con nombre igual en la misma empresa
        CategoriaModel categoriaModel2 = categoriaRepository.findByNombreEnEmpresa(nombre, categoriaModel.getEmpresa().getId()).orElse(null);
        if(categoriaModel2 != null){
            if(!categoriaModel2.getId().equals(categoriaModel.getId())){
                throw new RuntimeException("El nombre ya existe");
            }
        }

        if(nombre != null){
            categoriaModel.setNombre(nombre);
        }
        if(descripcion != null){
            categoriaModel.setDescripcion(descripcion);
        }
        categoriaRepository.save(categoriaModel);
        return categoriaModel;
    }
    public Boolean eliminar(String id){
        CategoriaModel categoriaModel = categoriaRepository.findById(Long.parseLong(id)).orElseThrow(
                () -> new RuntimeException("La categoria no existe")
        );
        categoriaRepository.delete(categoriaModel);
        return true;
    }

}
