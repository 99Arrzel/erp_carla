import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-crear-categoria',
  templateUrl: './crear-categoria.component.html',
  styleUrls: ['./crear-categoria.component.css']
})
export class CrearCategoriaComponent {


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

    this.http.post(`${hostUrl}/api/categoria/crear`, {

      nombre: this.categoriaForm.value.nombre,
      descripcion: this.categoriaForm.value.descripcion,
      empresa_id: this.data.empresa_id,
      categoria_id: this.data.categoria_padre_id,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data) => {
        Notify.success("Categoría creada con éxito");
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
    this.titulo = 'Crear Categoría en ' + (this.data.categoria_padre_nombre ?? "Categoría raíz");
  }
  constructor(
    public dialogRef: MatDialogRef<CrearCategoriaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      empresa_id: any;
      categoria_padre_id: any;
      categoria_padre_nombre: any;
    },
  ) { }

}
