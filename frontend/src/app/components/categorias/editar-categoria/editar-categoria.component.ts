import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-editar-categoria',
  templateUrl: './editar-categoria.component.html',
  styleUrls: ['./editar-categoria.component.css']
})
export class EditarCategoriaComponent {

  categoriaForm = new FormGroup({
    nombre: new FormControl('', [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
  });

  http = inject(HttpClient);


  titulo = 'Crear Categoría';
  crearCategoria() {
    if (this.categoriaForm.value.nombre?.trim() == "") {
      Notify.failure("El nombre de la categoría no puede estar vacío");
      return;
    }
    if (this.categoriaForm.value.descripcion?.trim() == "") {
      Notify.failure("La descripción de la categoría no puede estar vacía");
      return;
    }

    this.http.post(`${hostUrl}/api/categoria/editar`, {
      /*data.get("id"),
                    data.get("nombre"),
                    data.get("descripcion")*/
      id: this.data.id,
      nombre: this.categoriaForm.value.nombre,
      descripcion: this.categoriaForm.value.descripcion,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data) => {
        Notify.success("Categoría editada con éxito");
        console.log(data);
        this.dialogRef.close(true);
      },
      error: (e) => {
        Notify.failure(e.error?.msg);
      },
    });
  }
  ngOnInit(): void {
    /* console.log(this.data.categoria, "Datos del comp"); */
    console.log("xd");
    this.categoriaForm.controls.nombre.setValue(this.data.nombre as string);
    this.categoriaForm.controls.descripcion.setValue(this.data.descripcion as string);

  }
  constructor(
    public dialogRef: MatDialogRef<EditarCategoriaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      id: number;
      nombre: string;
      descripcion: string;
    },
  ) { }
}
