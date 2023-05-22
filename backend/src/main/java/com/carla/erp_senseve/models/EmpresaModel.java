package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
//@Getter
//@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="empresas")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EmpresaModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    private String nit;
    @NotNull
    private String sigla;
    private String telefono;
    private String correo;
    private String direccion;
    @NotNull
    private Integer niveles;
    @NotNull
    private Boolean estado = true;
    @NotNull
    @ManyToOne(fetch =  FetchType.LAZY)
    @JsonBackReference(value = "usuario-empresa")
    private UsuarioModel usuario;

    @JsonManagedReference(value = "empresa-gestion")
    @OneToMany(mappedBy = "empresa")
    private List<GestionModel> gestiones;

    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference(value = "empresa-cuenta")
    private List<CuentaModel> cuentas;
    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference(value = "moneda-empresa")
    private List<EmpresaMonedaModel> monedas;

    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference(value = "empresa-comprobante")
    @JsonIgnore
    private List<ComprobanteModel> comprobantes;

    //Categorias
    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference(value = "empresa-categoria")
    @JsonIgnore
    private List<CategoriaModel> categorias;

    //Articulos
    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference(value = "empresa-articulo")
    @JsonIgnore
    private List<ArticuloModel> articulos;

}
