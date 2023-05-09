import { Component, inject, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Gestiones } from '../gestiones.component';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Notify } from 'notiflix/build/notiflix-notify-aio';
import { hostUrl } from 'src/app/app-routing.module';


@Component({
  selector: 'app-dialog-gestion',
  templateUrl: './dialog-gestion.component.html',
  styleUrls: ['./dialog-gestion.component.css']
})
export class DialogGestionComponent {

  gestionForm = new FormGroup({
    nombre: new FormControl('', [Validators.required]),
    fecha_inicio: new FormControl(new Date().toISOString().slice(0, -1)),
    fecha_fin: new FormControl(new Date().toISOString().slice(0, -1)),
  });
  titulo = "Crear Gestión";
  crearGestion() {

    if (this.gestionForm.value.nombre?.trim() == "") {
      Notify.failure("El nombre de la gestión no puede estar vacío");
      return;
    }
    if (this.gestionForm.value.fecha_inicio?.trim() == "") {
      Notify.failure("La fecha de inicio no puede estar vacía");
      return;
    }
    if (this.gestionForm.value.fecha_fin?.trim() == "") {
      Notify.failure("La fecha de fin no puede estar vacía");
      return;
    }

    let inicio = new Date(this.gestionForm.value.fecha_inicio + "T00:00:00"); //T00:00:00 is to avoid timezone issues
    let fin = new Date(this.gestionForm.value.fecha_fin + "T00:00:00");
    console.log(inicio, fin);
    if (inicio > fin) {
      Notify.failure("La fecha de inicio no puede ser mayor a la fecha de fin");
      return;
    }
    this.http.post(`${hostUrl}/api/gestion/upsert`, {
      id: this.data.gestion?.id,
      nombre: this.gestionForm.value.nombre,
      /* Date in format 2023-07-30*/
      fecha_inicio: inicio,
      fecha_fin: fin,
      empresa_id: this.data.empresa_id
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe(
      {
        next:
          (res) => {
            if (res) {
              this.dialogRef.close(true);
            }
          },
        error: (e) => {
          Notify.failure(e.error?.msg);

        },
      }
    );

  }
  http = inject(HttpClient);
  ngOnInit(): void {
    if (this.data.gestion) {
      this.titulo = "Editar Gestión";
      console.log(this.data.gestion);
      console.log(new Date(this.data.gestion.fechaInicio).toISOString().split('T')[0]);
      this.gestionForm.setValue({
        nombre: this.data.gestion.nombre,
        fecha_inicio: new Date(this.data.gestion.fechaInicio).toISOString().split('T')[0],
        fecha_fin: new Date(this.data.gestion.fechaFin).toISOString().split('T')[0],
      });
    }
  }
  constructor(
    public dialogRef: MatDialogRef<DialogGestionComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { gestion: Gestiones | null, empresa_id: number; },
  ) { }
}
