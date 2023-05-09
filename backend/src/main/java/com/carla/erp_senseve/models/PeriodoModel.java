package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="periodos")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PeriodoModel{
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
    @JsonBackReference (value = "usuario-periodo")
    private UsuarioModel usuario;
    @NotNull
    @JsonBackReference (value = "gestion-periodo")
    @ManyToOne(fetch =  FetchType.EAGER)
    private GestionModel gestion;
    //Ver que no rompa nada
    @Column(name = "gestion_id", insertable = false, updatable = false)
    private Long gestion_id; // nuevo atributo

}
