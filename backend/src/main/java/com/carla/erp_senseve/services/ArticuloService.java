package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.ArticuloModel;
import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.ArticuloRepository;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticuloService {
    @Autowired
    TokenGetUserService tokenGetUserService;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    EmpresaService empresaService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ArticuloRepository articuloRepository;
    @Autowired
    EmpresaRepository empresaRepository;
    public ArticuloModel crear(String nombre, String descripcion, Float precio, List<Long> categorias, Long idEmpresa,String authHeader) {
        UsuarioModel usuario = usuarioRepository.findByUsuario(tokenGetUserService.username(authHeader)).orElseThrow(
                () -> new RuntimeException("El usuario no existe")
        );
        EmpresaModel empresa = empresaRepository.findById(idEmpresa).orElseThrow(
                () -> new RuntimeException("La empresa no existe")
        );
        //Verificar que le pertenezca
        if(!empresa.getUsuario().getId().equals(usuario.getId())){
            throw new RuntimeException("La empresa no le pertenece");
        }
        ArticuloModel articuloModel = new ArticuloModel();
        articuloModel.setNombre(nombre);
        articuloModel.setDescripcion(descripcion);
        articuloModel.setPrecio(precio);
        articuloModel.setEmpresa(empresa);
        articuloModel.setCategoria(categoriaService.todasPorId(categorias));
        articuloModel.setUsuario(usuario);
        articuloRepository.save(articuloModel);
        return articuloModel;
    }
    public ArticuloModel editar(
            Long id,
            String nombre,
            String descripcion,
            Float precio,
            List<Long> categorias
    ){
        ArticuloModel articuloModel = articuloRepository.findById(id).orElseThrow(
                () -> new RuntimeException("El articulo no existe")
        );
        articuloModel.setNombre(nombre);
        articuloModel.setDescripcion(descripcion);
        articuloModel.setPrecio(precio);
        articuloModel.setCategoria(categoriaService.todasPorId(categorias));
        articuloRepository.save(articuloModel);
        return articuloModel;
    }
    public ArticuloModel eliminar(Long id){
        ArticuloModel articuloModel = articuloRepository.findById(id).orElseThrow(
                () -> new RuntimeException("El articulo no existe")
        );
        articuloRepository.delete(articuloModel);
        return articuloModel;
    }
    public List<ArticuloModel> ListarDeUsuarioPorEmpresa(
            String authHeader,
            String empresa_id
    ){
        UsuarioModel usuario = usuarioRepository.findByUsuario(tokenGetUserService.username(authHeader)).orElseThrow(
                () -> new RuntimeException("El usuario no existe")
        );
        return articuloRepository.findByIdAndUsuario( Long.parseLong(empresa_id), usuario.getId());

    }
}
