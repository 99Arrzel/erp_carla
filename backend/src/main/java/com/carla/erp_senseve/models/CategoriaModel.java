package com.carla.erp_senseve.models;


import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categoria")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoriaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference (value = "empresa-categoria")
    private EmpresaModel empresa;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference (value = "usuario-categoria")
    private UsuarioModel usuario;


    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference (value = "categoria-categoria")
    @JsonIgnore
    private CategoriaModel categoria;
    //Self reference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria")
    @JsonManagedReference(value = "categoria-categoria")
    @JsonIgnore
    private List<CategoriaModel> categorias;

    @Column(name = "empresa_id", insertable = false, updatable = false)
    private Long empresa_id;
    @Column(name = "usuario_id", insertable = false, updatable = false)
    private Long usuario_id;
    @Column(name = "categoria_id", insertable = false, updatable = false)
    //Recursivo
    private Long categoria_id;
    //Articulos
    @ManyToMany(mappedBy = "categoria", cascade = CascadeType.PERSIST)
    @JsonBackReference (value = "articulo-categoria")
    private List<ArticuloModel> articulos;

}
