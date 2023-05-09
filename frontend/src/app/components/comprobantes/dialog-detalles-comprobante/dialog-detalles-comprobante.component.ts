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

  cols = ['Numero', 'Glosa', 'Monto Debe', 'Monto Haber', 'Monto Debe Alt.', 'Monto Haber Alt.'];
  detallesComprobante: any[] = [];

  ngOnInit(): void {
    console.log(this.data.comprobante, "Datos del comp");
    this.detallesComprobante = this.data.comprobante.detalles;
  }
  constructor(
    public dialogRef: MatDialogRef<DialogDetallesComprobanteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { comprobante: any; },
  ) { }

}
