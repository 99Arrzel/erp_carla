import { HttpClient } from '@angular/common/http';
import { Component, Inject, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';



interface nodo {
  id: number;
  nombre: string;
  padre_id: number;
}

@Component({
  selector: 'app-dialog-cuenta',
  templateUrl: './dialog-cuenta.component.html',
  styleUrls: ['./dialog-cuenta.component.css']
})

export class DialogCuentaComponent {


  cuentaForm = new FormGroup({
    nombre: new FormControl('', [Validators.required]),
  });
  http = inject(HttpClient);
  crearCuenta() {
    if (this.cuentaForm.value.nombre?.trim() == "") {
      Notify.failure("El nombre de la cuenta no puede estar vacío");
      return;
    }
    this.http.post(`${hostUrl}/api/cuenta/upsert`, {
      id: this.data.nodo?.id,
      nombre: this.cuentaForm.value.nombre,
      empresa_id: this.data.empresa_id,
      padre_id: this.data.nodo?.padre_id,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data) => {
        Notify.success("Cuenta creada con éxito");
        this.dialogRef.close(true);
      },
      error: (e) => {
        Notify.failure(e.error?.msg);
      },
    });
  }
  titulo = 'Crear Cuenta';
  ngOnInit(): void {
    if (this.data.nodo && this.data.accion == "Crear") {
      this.titulo = 'Crear Subcuenta';
    }
    if (this.data.accion == "Editar" && this.data.nodo) {
      this.titulo = 'Editar Cuenta';
      this.cuentaForm.controls.nombre.setValue(this.data.nodo?.nombre as string);
    }
  }
  constructor(
    public dialogRef: MatDialogRef<DialogCuentaComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { nodo: nodo | null, empresa_id: number, accion: string; },
  ) { }
}
