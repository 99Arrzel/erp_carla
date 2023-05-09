package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter

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
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "moneda_principal", fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)

    //@JsonManagedReference(value = "moneda-principal-empresa")
    private List<EmpresaMonedaModel> monedas_primarias_empresa;

    @OneToMany(mappedBy = "moneda_alternativa",fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    //@JsonManagedReference(value = "moneda-alternativa-empresa")
    private List<EmpresaMonedaModel> monedas_alternativa_empresa;

    @OneToMany(mappedBy = "moneda",fetch = FetchType.EAGER)
    @JsonIdentityReference(alwaysAsId = true)
    //@JsonManagedReference(value = "moneda-alternativa-empresa")
    private List<ComprobanteModel> comprobantes;
}
