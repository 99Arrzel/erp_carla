package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data //Getters y setters
@Table(name="cuentas")
@NoArgsConstructor
@AllArgsConstructor
public class CuentaModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String codigo;
    private Integer nivel;
    private String tipo;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference //Avoid infinite recursion
    private EmpresaModel empresa;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference //Avoid infinite recursion
    private CuentaModel padre;
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonBackReference //Avoid infinite recursion
    private UsuarioModel usuario;



}
