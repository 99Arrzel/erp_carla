import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-crear-nota-compra',
  templateUrl: './crear-nota-compra.component.html',
  styleUrls: ['./crear-nota-compra.component.css'],
})
export class CrearNotaCompraComponent {

  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }

  notaCompra = new FormGroup({
    nro_nota: new FormControl<any>(null, [Validators.required]),
    fecha: new FormControl(new Date(), [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    empresa_id: new FormControl<number | null>(null, [Validators.required]),
    /* Lista de lotes */
    lotes: new FormControl([], [Validators.required]),
  });

  lotes = new FormGroup({
    articulo: new FormControl<any>(null, [Validators.required]),
    fecha_vencimiento: new FormControl<Date>(new Date(), [Validators.required]),
    cantidad: new FormControl<number>(0, [Validators.required]),
    precio: new FormControl<number>(0, [Validators.required]),
  });
  isDisabled = (id: number) => {
    return false;
  };

  addLote() {

  }

  crearNota() {

  }
  articulos = <any>[];

}
