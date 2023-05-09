package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="detalle_comprobantes")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DetalleComprobanteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String numero;
    @NotNull
    private String glosa;
    private Float monto_debe;
    private Float monto_haber;
    private Float monto_debe_alt;
    private Float monto_haber_alt;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "usuario-detalle_comprobante")
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;
    @Column(insertable = false, updatable = false)
    private Long usuario_id;

    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "comprobante-detalles")
    @JoinColumn(name = "comprobante_id")
    private ComprobanteModel comprobante;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "detalles-cuenta")
    private CuentaModel cuenta;
    @Column(name = "comprobante_id", insertable = false, updatable = false)
    private Long comprobante_id; // nuevo atributo
}