import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';


@Component({
  selector: 'app-dialog-detalles-comprobante',
  templateUrl: './dialog-detalles-comprobante.component.html',
  styleUrls: ['./dialog-detalles-comprobante.component.css']
})
export class DialogDetallesComprobanteComponent {

  cols = ['Cuenta', 'Glosa', 'Monto Debe', 'Monto Haber',];
  detallesComprobante: any[] = [];
  sumarDebe() {
    let total = 0;
    this.detallesComprobante.forEach((detalle: any) => {
      total += detalle.monto_debe;
    });
    return total;
  }
  sumarHaber() {
    let total = 0;
    this.detallesComprobante.forEach((detalle: any) => {
      total += detalle.monto_haber;
    });
    return total;
  }
  comprobante: any = null;
  ngOnInit(): void {
    console.log(this.data.comprobante, "Datos del comp");
    this.comprobante = this.data.comprobante;
    /* Order by id array */
    //this.detallesComprobante = this.data.comprobante.detalles
    this.detallesComprobante = this.data.comprobante.detalles.sort((a: any, b: any) => {
      return a.id - b.id;
    });
  }
  constructor(
    public dialogRef: MatDialogRef<DialogDetallesComprobanteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { comprobante: any; },
  ) { }

}
