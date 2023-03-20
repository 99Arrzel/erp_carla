package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Data //Getters y setters
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
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
  //Imp for UserDetails


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


