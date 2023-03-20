package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data //Getters y setters
@NoArgsConstructor
@AllArgsConstructor
@Table(name="empresas")
public class EmpresaModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String nit;
    private String sigla;
    private String telefono;
    private String correo;
    private String direccion;
    private Integer niveles;
    private Boolean estado;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;
    //Fetch gestiones
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    @JsonManagedReference //Avoid infinite recursion
    private List<GestionModel> gestiones;


}
