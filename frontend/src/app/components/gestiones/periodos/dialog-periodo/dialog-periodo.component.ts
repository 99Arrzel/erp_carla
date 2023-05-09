import { Component, inject, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Notify } from 'notiflix/build/notiflix-notify-aio';
import { Periodos } from '../periodos.component';
import { hostUrl } from 'src/app/app-routing.module';


@Component({
  selector: 'app-dialog-periodo',
  templateUrl: './dialog-periodo.component.html',
  styleUrls: ['./dialog-periodo.component.css']
})
export class DialogPeriodoComponent {

  periodoForm = new FormGroup({
    nombre: new FormControl('', [Validators.required]),
    fecha_inicio: new FormControl(new Date().toISOString().slice(0, -1)),
    fecha_fin: new FormControl(new Date().toISOString().slice(0, -1)),
  });
  titulo = "Crear Periodo";
  crearPeriodo() {
    if (this.periodoForm.value.nombre?.trim() == "") {
      Notify.failure("El nombre del periodo no puede estar vacío");
      return;
    }
    if (this.periodoForm.value.fecha_inicio?.trim() == "") {
      Notify.failure("La fecha de inicio no puede estar vacía");
      return;
    }
    if (this.periodoForm.value.fecha_fin?.trim() == "") {
      Notify.failure("La fecha de fin no puede estar vacía");
      return;
    }
    let inicio = new Date(this.periodoForm.value.fecha_inicio + "T00:00:00"); //T00:00:00 is to avoid timezone issues
    let fin = new Date(this.periodoForm.value.fecha_fin + "T00:00:00");
    if (inicio > fin) {
      Notify.failure("La fecha de inicio no puede ser mayor a la fecha de fin");
      return;
    }
    this.http.post(`${hostUrl}/api/periodo/upsert`, {
      id: this.data.periodo?.id,
      nombre: this.periodoForm.value.nombre,
      /* Date in format 2023-07-30*/
      fecha_inicio: inicio,
      fecha_fin: fin,
      gestion_id: this.data.gestion_id
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
    console.log(this.data);
    if (this.data.periodo) {
      this.titulo = "Editar Periodo";
      this.periodoForm.setValue({
        nombre: this.data.periodo.nombre,
        fecha_inicio: new Date(this.data.periodo.fechaInicio).toISOString().split('T')[0],
        fecha_fin: new Date(this.data.periodo.fechaFin).toISOString().split('T')[0],
      });
    }
  }
  constructor(
    public dialogRef: MatDialogRef<DialogPeriodoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { periodo: Periodos | null, gestion_id: number; },
  ) { }
}
