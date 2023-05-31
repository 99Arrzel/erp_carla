package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Entity
@Getter
@Setter
@Table(name = "detalles")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Proxy(lazy = false)
public class DetallesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float cantidad;
    private Float precio_venta;
    //nota
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nota_id")
    private NotaModel nota;
    //lote
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_id")
    private LotesModel lote;


}
