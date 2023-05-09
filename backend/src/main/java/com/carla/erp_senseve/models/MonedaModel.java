package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
//Getters y setters
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="monedas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MonedaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    private String descripcion;
    @NotNull
    private String abreviatura;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "usuario-moneda")
    private UsuarioModel usuario;
    @JsonManagedReference(value = "moneda-principal-empresa")
    private List<EmpresaMonedaModel> monedas_primarias_empresa;
    @JsonManagedReference(value = "moneda-alternativa-empresa")
    private List<EmpresaMonedaModel> monedas_alternativa_empresa;

}
