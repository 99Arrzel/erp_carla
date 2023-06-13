import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as Notiflix from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-editar-articulo-modal',
  templateUrl: './editar-articulo-modal.component.html',
  styleUrls: ['./editar-articulo-modal.component.css']
})
export class EditarArticuloModalComponent {
  categorias: any[] = [];
  articuloForm = new FormGroup({
    id: new FormControl(null),
    nombre: new FormControl('', [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    precio_venta: new FormControl(0, [Validators.required]),
    categorias: new FormControl([], [Validators.required]),
  });
  ngOnInit(): void {
    this.categorias = this.data.categorias;

    //LLenar articulo form con articulo
    this.articuloForm.patchValue({
      id: this.data.articulo.id,
      nombre: this.data.articulo.nombre,
      descripcion: this.data.articulo.descripcion,
      precio_venta: this.data.articulo.precio,
      categorias: this.data.articulo.categoria
    });
  }
  http = inject(HttpClient);
  editarArticulo() {
    this.http.post(`${hostUrl}/api/articulo/editar`, {

      id: this.articuloForm.value.id,
      nombre: this.articuloForm.value.nombre,
      descripcion: this.articuloForm.value.descripcion,
      precio: this.articuloForm.value.precio_venta,
      categorias: this.articuloForm.value.categorias,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data) => {
        Notiflix.Notify.success("Artículo editado con éxito");
        console.log(data);
        //Resetear form
        this.dialog.close();
        //this.updateArticulos();
      },
      error: (e) => {
        Notiflix.Notify.failure(e.error?.msg);
      }
    });
  }
  constructor(
    public dialog: MatDialogRef<EditarArticuloModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { categorias: any; empresa_id: any; articulo: any; },
  ) { }

}
