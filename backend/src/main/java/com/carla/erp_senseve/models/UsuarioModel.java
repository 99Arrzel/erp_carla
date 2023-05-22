package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UsuarioModel implements UserDetails, CredentialsContainer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  @Column(unique = true)
  private String usuario;
  //Hide on call
  @JsonIgnore
  private String password;
  private String tipo;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonManagedReference(value = "usuario-cuenta")
  private List<CuentaModel> cuentas;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonManagedReference(value = "usuario-empresa")
  private List<EmpresaModel> empresas;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonManagedReference(value = "usuario-gestion")
  private List<GestionModel> gestiones;

  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonIgnore
  @JsonManagedReference(value = "usuario-periodo")
  private List<PeriodoModel> periodos;

  @JsonIgnore
  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "usuario-moneda")
  private List<MonedaModel> monedas;

  @JsonIgnore
  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "usuario-empresa-moneda")
  private List<EmpresaMonedaModel> empresas_monedas;

  @JsonIgnore
  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "usuario-detalle_comprobante")
  private List<DetalleComprobanteModel> detalles_comprobantes;

  @JsonIgnore
  @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
  @JsonManagedReference(value = "usuario-comprobante")
  private List<ComprobanteModel> comprobantes;
  //Categoria
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "usuario-categoria")
    private List<CategoriaModel> categorias;
//Articulo
      @JsonIgnore
        @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
        @JsonManagedReference(value = "usuario-articulo")
        private List<ArticuloModel> articulos;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null; //No roles
  }

  @Override
  public String getUsername() {
    return this.usuario;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false; //No expira
  }
  @Override
  public boolean isAccountNonLocked() {
    return false; //No se bloquea
  }
  @Override
  public boolean isCredentialsNonExpired() {
    return false; //No expira
  }

  @Override
  public boolean isEnabled() {
    return true; //Siempre activo
  }

  @Override
  public void eraseCredentials() {
    this.password = null; //No se guarda el password
  }
}


