import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-cuentas-integracion',
  templateUrl: './cuentas-integracion.component.html',
  styleUrls: ['./cuentas-integracion.component.css']
})
export class CuentasIntegracionComponent {

  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }

  /*   articuloForm = new FormGroup({
    id: new FormControl(null),
    nombre: new FormControl('', [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    precio_venta: new FormControl(0, [Validators.required]),
    categorias: new FormControl([], [Validators.required]),
  });
 */
  cuenta_integracion_form = new FormGroup({
    caja: new FormControl<any>(null),
    credito_fiscal: new FormControl<any>(null),
    debito_fiscal: new FormControl<any>(null),
    compras: new FormControl<any>(null),
    ventas: new FormControl<any>(null),
    it: new FormControl<any>(null),
    it_por_pagar: new FormControl<any>(null),
    activo: new FormControl<boolean>(false),
  });
  http = inject(HttpClient);

  isDisabled = (id: number) => {
    /* Buscamos en cuenta_informacion_form */
    let cuentas = [
      this.cuenta_integracion_form.value.caja?.id,
      this.cuenta_integracion_form.value.compras?.id,
      this.cuenta_integracion_form.value.credito_fiscal?.id,
      this.cuenta_integracion_form.value.debito_fiscal?.id,
      this.cuenta_integracion_form.value.it?.id,
      this.cuenta_integracion_form.value.it_por_pagar?.id,
      this.cuenta_integracion_form.value.ventas?.id,
    ];
    return cuentas.includes(id);
  };

  cuentas = <any>[];
  empresa_id = null as number | null;
  guardar() {
    /* Chequear que todas sean diferentes */
    //Hagamos un set y luego comparamos el length
    let cuentas = new Set([
      this.cuenta_integracion_form.value.caja?.id,
      this.cuenta_integracion_form.value.compras?.id,
      this.cuenta_integracion_form.value.credito_fiscal?.id,
      this.cuenta_integracion_form.value.debito_fiscal?.id,
      this.cuenta_integracion_form.value.it?.id,
      this.cuenta_integracion_form.value.it_por_pagar?.id,
      this.cuenta_integracion_form.value.ventas?.id,
    ]);
    if (!this.cuenta_integracion_form.value.caja) {
      Notify.failure("Debe seleccionar una cuenta de caja");
      return;
    }
    if (!this.cuenta_integracion_form.value.compras) {
      Notify.failure("Debe seleccionar una cuenta de compras");
      return;
    }
    if (!this.cuenta_integracion_form.value.credito_fiscal) {
      Notify.failure("Debe seleccionar una cuenta de crédito fiscal");
      return;
    }
    if (!this.cuenta_integracion_form.value.debito_fiscal) {
      Notify.failure("Debe seleccionar una cuenta de débito fiscal");
      return;
    }
    if (!this.cuenta_integracion_form.value.it) {
      Notify.failure("Debe seleccionar una cuenta de IT");
      return;
    }
    if (!this.cuenta_integracion_form.value.it_por_pagar) {
      Notify.failure("Debe seleccionar una cuenta de IT por pagar");
      return;
    }
    if (!this.cuenta_integracion_form.value.ventas) {
      Notify.failure("Debe seleccionar una cuenta de ventas");
      return;
    }

    if (cuentas.size != 7) {
      Notify.failure("Las cuentas deben ser diferentes");
      return;
    }
    //Guardar cuentas de integracion
    this.http.post(`${hostUrl}/api/cuentas_integracion/integrar`, {
      empresa_id: this.empresa_id as number,
      cuenta_caja_id: this.cuenta_integracion_form.value.caja.id,
      cuenta_compras_id: this.cuenta_integracion_form.value.compras.id,
      cuenta_credito_fiscal_id: this.cuenta_integracion_form.value.credito_fiscal.id,
      cuenta_debito_fiscal_id: this.cuenta_integracion_form.value.debito_fiscal.id,
      cuenta_it_id: this.cuenta_integracion_form.value.it.id,
      cuenta_it_por_pagar_id: this.cuenta_integracion_form.value.it_por_pagar.id,
      cuenta_ventas_id: this.cuenta_integracion_form.value.ventas.id,
      estado: this.cuenta_integracion_form.value.activo ? "Activo" : "Inactivo"
    } as any, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (result: any) => {
        console.log(result);
      }
    });
  }
  ngOnInit() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.empresa_id = id_empresa;
    this.http.post(`${hostUrl}/api/cuenta/por_empresa`, {
      id: id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (result: any) => {
        console.log(result, "Cuentas de la empresa");
        this.cuentas = result.filter((cuenta: any) => cuenta.tipo == "Detalle");
      }
    });
    //fetch de estado de cuentas de integracion
    this.http.post(`${hostUrl}/api/cuentas_integracion/listar`, {
      empresa_id: id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (result: any) => {
        if (result != null) { //Si integración está implementado

          console.log(result, "Esta integrado");
          //Seteamos el formulario
          this.cuenta_integracion_form.patchValue({
            caja: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_caja.id),
            compras: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_compras.id),
            credito_fiscal: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_credito_fiscal.id),
            debito_fiscal: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_debito_fiscal.id),
            it: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_it.id),
            it_por_pagar: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_it_por_pagar.id),
            ventas: this.cuentas.find((cuenta: any) => cuenta.id == result.cuenta_ventas.id),
            activo: result.estado == "Activo" ? true : false
          });
          //Actualizo el formulario
          this.cuenta_integracion_form.updateValueAndValidity();
        }
      }
    });


  }
  //cuenta_integracion_form

}
