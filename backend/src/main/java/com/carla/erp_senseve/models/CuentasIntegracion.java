package com.carla.erp_senseve.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CuentasIntegracion")
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CuentasIntegracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String estado = "Activo"; //Activo o Inactivo
    //Relacion a cuentas: Caja, credito Fiscal, Debito Fiscal, Compras, Ventas, IT y IT por pagar
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_caja_id", referencedColumnName = "id")
    private CuentaModel cuenta_caja;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_credito_fiscal_id", referencedColumnName = "id")
    private CuentaModel cuenta_credito_fiscal;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_debito_fiscal_id", referencedColumnName = "id")
    private CuentaModel cuenta_debito_fiscal;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_compras_id", referencedColumnName = "id")
    private CuentaModel cuenta_compras;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_ventas_id", referencedColumnName = "id")
    private CuentaModel cuenta_ventas;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_it_id", referencedColumnName = "id")
    private CuentaModel cuenta_it;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "cuenta_it_por_pagar_id", referencedColumnName = "id")
    private CuentaModel cuenta_it_por_pagar;
    //Ahora empresa
    @NotNull
    @ManyToOne
    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    private EmpresaModel empresa;
    //Ahora los ids de las cuentas


}
