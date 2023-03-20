package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data //Getters y setters
@NoArgsConstructor
@AllArgsConstructor
@Table(name="gestiones")
public class GestionModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    private Date fechaFin;
    private Boolean estado;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private UsuarioModel usuario;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    @JsonBackReference //Avoid infinite recursion
    private EmpresaModel empresa;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestion_id")
    @JsonManagedReference
    private List<PeriodoModel> periodos;


}
