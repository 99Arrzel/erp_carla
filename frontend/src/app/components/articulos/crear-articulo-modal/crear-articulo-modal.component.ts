import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as Notiflix from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-crear-articulo-modal',
  templateUrl: './crear-articulo-modal.component.html',
  styleUrls: ['./crear-articulo-modal.component.css']
})
export class CrearArticuloModalComponent {
  http = inject(HttpClient);
  /* Articulo form */
  articuloForm = new FormGroup({
    id: new FormControl(null),
    nombre: new FormControl('', [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    precio_venta: new FormControl(0, [Validators.required]),
    categorias: new FormControl([], [Validators.required]),
  });
  crearArticulo() {
    console.log("epa");
    if (this.articuloForm.value.nombre?.trim() == "") {
      Notiflix.Notify.failure("El nombre del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.descripcion?.trim() == "") {
      Notiflix.Notify.failure("La descripción del artículo no puede estar vacía");
      return;
    }
    if (this.articuloForm.value.precio_venta == null) {
      Notiflix.Notify.failure("El precio de venta del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.precio_venta <= 0) {
      Notiflix.Notify.failure("El precio de venta del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.categorias?.length == 0) {
      Notiflix.Notify.failure("Seleccione al menos una categoría");
      return;
    }


    this.http.post(`${hostUrl}/api/articulo/crear`, {
      nombre: this.articuloForm.value.nombre,
      descripcion: this.articuloForm.value.descripcion,
      precio: this.articuloForm.value.precio_venta,
      categorias: this.articuloForm.value.categorias,
      empresa_id: this.data.empresa_id,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data) => {
        Notiflix.Notify.success("Artículo creado con éxito");
        console.log(data);
        this.articuloForm.reset();
        this.dialog.close();
        //this.updateArticulos();
      },
      error: (e) => {
        Notiflix.Notify.failure(e.error?.msg);
      },
    });

    /* else {
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

          this.articuloForm.reset();
          this.fetchArticulos();
          //this.updateArticulos();
        },
        error: (e) => {
          Notiflix.Notify.failure(e.error?.msg);
        }
      });
      this.titulo = "Crear Artículo";
    } */
    console.log(this.articuloForm.value);
  }
  categorias: any[] = [];
  ngOnInit(): void {
    this.categorias = this.data.categorias;
  }
  constructor(
    public dialog: MatDialogRef<CrearArticuloModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { categorias: any; empresa_id: any; },
  ) { }


}
