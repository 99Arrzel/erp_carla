package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comprobantes")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ComprobanteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String serie;
    @NotNull
    private String glosa;
    @NotNull
    private Date fecha;

    //NO MIGRAR
    //ignorar
    @Column(insertable = false, updatable = false)
    private String moneda_nombre;

    @NotNull
    private Float tc;
    @NotNull
    private String estado;
    @NotNull
    private String tipo;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "usuario-comprobante")
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;
    @Column(insertable = false, updatable = false)
    private Long usuario_id;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "moneda-comprobante")
    private MonedaModel moneda;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comprobante")
    @JsonManagedReference(value = "comprobante-detalles")
    private List<DetalleComprobanteModel> detalles;
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference(value = "empresa-comprobante")
    private EmpresaModel empresa;

    //Nota
    @OneToMany(mappedBy = "comprobante")
    @JsonIgnore
    private List<NotaModel> notas;

}