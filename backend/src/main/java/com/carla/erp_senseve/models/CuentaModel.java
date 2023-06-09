package com.carla.erp_senseve.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Proxy;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name="cuentas")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Proxy(lazy = false)
public class CuentaModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String codigo;
    private Integer nivel;
    private String tipo;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "empresa-cuenta")
    private EmpresaModel empresa;
    @ManyToOne(fetch =  FetchType.EAGER)
    private CuentaModel padre;
    @NotNull
    @ManyToOne(fetch =  FetchType.EAGER)
    @JsonBackReference(value = "usuario-cuenta")
    private UsuarioModel usuario;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cuenta")
    @JsonManagedReference(value = "detalles-cuenta")
    private List<DetalleComprobanteModel> detalle_comprobantes;

    @OneToMany(mappedBy = "cuenta_caja")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_caja;
    @OneToMany(mappedBy = "cuenta_credito_fiscal")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_credito_fiscal;
    @OneToMany(mappedBy = "cuenta_debito_fiscal")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_debito_fiscal;
    @OneToMany(mappedBy = "cuenta_ventas")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_ventas;
    @OneToMany(mappedBy = "cuenta_compras")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_compras;
    @OneToMany(mappedBy = "cuenta_it")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_it;
    @OneToMany(mappedBy = "cuenta_it_por_pagar")
    @JsonIgnore
    private List<CuentasIntegracion> cuenta_it_por_pagar;











    //No migrar
    @Transient
    private DetalleComprobanteModel unicodetalle;

    @Transient
    private Float total_balance_inicial;
    public void setUnicoDetalle(DetalleComprobanteModel detalleComprobanteModel) {
        this.unicodetalle = detalleComprobanteModel;
    }
}
