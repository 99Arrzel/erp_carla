package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "notas")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Proxy(lazy = false)
public class NotaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nro_nota;
    private Date fecha;
    private String descripcion;
    //Empresa
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JsonIgnore
    @JoinColumn(name = "empresa_id")
    private EmpresaModel empresa;


    //Usuario
    @ManyToOne(fetch = FetchType.EAGER)

    @JsonIgnore
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;
    //Comprobante

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JsonIgnore
    @JoinColumn(name = "comprobante_id", nullable = true)
    private ComprobanteModel comprobante;
    @NotNull
    private Float total;
    @NotNull
    private String tipo;
    @NotNull
    private String estado;


    //lote

    @JsonIgnore
    @OneToMany(mappedBy = "nota", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LotesModel> lote;

    @JsonIgnore
    //detalle
    @OneToMany(mappedBy = "nota", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetallesModel> detalle;


}
