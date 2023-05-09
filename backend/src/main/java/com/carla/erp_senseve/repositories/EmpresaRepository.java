package com.carla.erp_senseve.repositories;


import com.carla.erp_senseve.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {

    List<EmpresaModel> findByUsuarioId(Long usuario_id);

    List<EmpresaModel> findByUsuarioIdAndEstado(Long usuario_id, Boolean estado);

    List<EmpresaModel> findByNitOrSigla(String nit, String sigla);
    @Query(value = "SELECT * FROM empresas WHERE (nit = :nit OR sigla = :sigla OR nombre = :nombre) AND id != :id AND estado = true", nativeQuery = true) //Select first
    List<EmpresaModel> findByNitOrSiglaOrNombreAndEstado(
            @Param("nit") String nit,
            @Param("sigla") String sigla,
            @Param("nombre") String nombre,
            @Param("id") Long id);
    @Query(value = "SELECT * FROM empresas WHERE (nit = :nit OR sigla = :sigla OR nombre = :nombre) AND estado = true", nativeQuery = true) //Select first
    List<EmpresaModel> findByNitOrSiglaOrNombreAndEstado(
            @Param("nit") String nit,
            @Param("sigla") String sigla,
            @Param("nombre") String nombre);

    //Find by name and estado true
    Optional<EmpresaModel> findByNombreAndEstado(String nombre, Boolean estado);
    @Query(value = "SELECT * FROM empresas WHERE (nit = :nit OR sigla = :sigla) AND estado = :estado", nativeQuery = true)
    Optional<EmpresaModel> findByNitOrSiglaAndEstado(
            @Param("nit") String nit,
            @Param("sigla") String sigla,
            @Param("estado") Boolean estado);
}
