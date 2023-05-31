package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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
    @GeneratedValue
    private Long id;
    private String nro_nota;
    private Date fecha;
    private String descripcion;
    //Empresa
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id")
    private EmpresaModel empresa;


    //Usuario
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;
    //Comprobante


    @Nullable
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comprobante_id")
    private ComprobanteModel comprobante;


    private Float total;
    private String tipo;
    private String estado;

    //lote
    @OneToMany(mappedBy = "nota", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LotesModel> lote;

    //detalle
    @OneToMany(mappedBy = "nota", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetallesModel> detalle;


}
