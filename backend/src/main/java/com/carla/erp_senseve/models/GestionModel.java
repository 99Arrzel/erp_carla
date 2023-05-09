package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="gestiones")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GestionModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @NotNull
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @NotNull
    private Boolean estado = true;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference (value = "usuario-gestion")
    private UsuarioModel usuario;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "empresa-gestion")
    private EmpresaModel empresa;
    @OneToMany(mappedBy = "gestion")
    @JsonManagedReference (value = "gestion-periodo")
    private List<PeriodoModel> periodos;

}
