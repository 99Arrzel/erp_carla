package com.carla.erp_senseve.repositories;


import com.carla.erp_senseve.models.EmpresaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {
    //This method retrieves a user from the database by their username
    List<EmpresaModel> findByUsuarioId(Long usuario_id);

    //Fetch by usuario_id but only where estado is true
    List<EmpresaModel> findByUsuarioIdAndEstado(Long usuario_id, Boolean estado);
    //Fetch by NIT or Sigla
    List<EmpresaModel> findByNitOrSigla(String nit, String sigla);
    //Fetch by NIT or Sigla but only where estado is true
    EmpresaModel findByNitOrSiglaAndEstado(String nit, String sigla, Boolean estado);
}
