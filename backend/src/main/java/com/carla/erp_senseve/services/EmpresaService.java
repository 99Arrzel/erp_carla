package com.carla.erp_senseve.services;

import com.carla.erp_senseve.models.EmpresaModel;
import com.carla.erp_senseve.models.UsuarioModel;
import com.carla.erp_senseve.repositories.EmpresaRepository;
import com.carla.erp_senseve.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;



    public EmpresaModel delete(Long id, String usuario) {
        Optional<UsuarioModel> u = usuarioRepository.findByUsuario(usuario);
        if (u.isPresent()) {
            EmpresaModel e = empresaRepository.findById(id).orElse(null);
            if (e != null) {
                e.setEstado(false);
                return empresaRepository.save(e);
            }
        }
        return null;
    }
    public List<EmpresaModel> empresasUsuario(String usuario) {
        Optional<UsuarioModel> u = usuarioRepository.findByUsuario(usuario);
        if (u.isPresent()) {
            return empresaRepository.findByUsuarioIdAndEstado(u.get().getId(), true);
        }
        return null;
    }
    public EmpresaModel upsert(EmpresaModel empresa, String usuario) {
        UsuarioModel e = usuarioRepository.findByUsuario(usuario).orElse(null);
        if(empresa.getId() == null){
            //Suponiendo que se valido
            empresa.setUsuario(e);
            empresaRepository.save(empresa);
        }
        EmpresaModel emp = empresaRepository.findById(empresa.getId()).orElse(null);
        emp.setUsuario(e);
        emp.setNombre(empresa.getNombre());
        emp.setNit(empresa.getNit());
        emp.setSigla(empresa.getSigla());
        emp.setDireccion(empresa.getDireccion());
        emp.setTelefono(empresa.getTelefono());
        emp.setEstado(empresa.getEstado() == null ? true : empresa.getEstado()); //Default true
        return empresaRepository.save(emp);
    }
    public Optional<EmpresaModel> existeEmpresaNitOSigla(String nit, String sigla) {
        EmpresaModel empresa = empresaRepository.findByNitOrSiglaAndEstado(nit, sigla, true);
        if (empresa != null) {
            return Optional.of(empresa);
        }
        return Optional.empty();
    }

    public EmpresaModel una_empresa(Long id, String username) {
        Optional<UsuarioModel> u = usuarioRepository.findByUsuario(username);
        if (u.isPresent()) {
            EmpresaModel e = empresaRepository.findById(id).orElse(null);
            if (e != null) {
                return e;
            }
        }
        return null;
    }
}
