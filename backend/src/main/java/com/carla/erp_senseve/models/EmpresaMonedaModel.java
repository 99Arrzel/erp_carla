package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="empresas_monedas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"usuario"})

public class EmpresaMonedaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float cambio;
    @NotNull
    private Boolean estado = true;
    @NotNull

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "empresa_id")
    @JsonBackReference(value = "moneda-empresa")
    private EmpresaModel empresa;

    @JsonProperty("moneda_principal")
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "moneda_principal_id", nullable = false)
    //@JsonBackReference(value = "moneda-principal-empresa")
    @JsonIdentityReference(alwaysAsId = true)
    private MonedaModel moneda_principal;


    @JsonProperty("moneda_alternativa")
    @ManyToOne(fetch =  FetchType.EAGER)
    //@JsonBackReference(value = "moneda-alternativa-empresa")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "moneda_alternativa_id")
    private MonedaModel moneda_alternativa;

    @Column(insertable = false, updatable = false)
    private String nombre_moneda_principal;
    @Column(insertable = false, updatable = false)
    private String nombre_moneda_alternativa;


    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference(value = "usuario-empresa-moneda")
    private UsuarioModel usuario;

    @Column(insertable = false, updatable = false)
    private Long usuario_id;
    @Column(insertable = false, updatable = false)
    private Long empresa_id;
    @Column(insertable = false, updatable = false)
    private Long moneda_principal_id;
    @Column(insertable = false, updatable = false)
    private Long moneda_alternativa_id;
}
