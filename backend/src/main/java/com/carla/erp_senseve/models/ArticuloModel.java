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
@Table(name = "articulos")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ArticuloModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String descripcion;
    @NotNull
    private Integer stock = 0;
    @NotNull
    private Float precio;

    //usuario
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private UsuarioModel usuario;

    //empresa
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonIgnore
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private EmpresaModel empresa;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonManagedReference(value = "articulo-categoria")
    @JoinTable(
            name = "articulo_categoria",
            joinColumns = @JoinColumn(name = "articulo_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id"),
            //delete cascada
            foreignKey = @ForeignKey(name = "FK_articulo_categoria_articulo_id", foreignKeyDefinition = "FOREIGN KEY (articulo_id) REFERENCES articulos(id) ON DELETE CASCADE"),
            inverseForeignKey = @ForeignKey(name = "FK_articulo_categoria_categoria_id", foreignKeyDefinition = "FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE")
    )
    private List<CategoriaModel> categoria;


}
