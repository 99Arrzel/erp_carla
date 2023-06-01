package com.carla.erp_senseve.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Nullable;
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
@Table(name = "lotes")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Proxy(lazy = false)
public class LotesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nro_lote;
    //Articulo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "articulo_id")
    @JsonIgnore
    private ArticuloModel articulo;
    @NotNull
    private String estado;
    @Nullable
    private Date fecha_vencimiento;
    @NotNull
    private Date fecha_ingreso;
    @NotNull
    private Float cantidad;
    @NotNull
    private Float precio_compra;
    @NotNull
    private Float stock;
    //nota
    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nota_id")
    private NotaModel nota;

    @JsonIgnore
    //detalles
    @OneToMany(mappedBy = "lote", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetallesModel> detalles;

}
