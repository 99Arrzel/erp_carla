import { Component, inject, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Empresa, EmpresaMonedaModel } from '../home.component';
import { z } from "zod";
import { Notify } from 'notiflix/build/notiflix-notify-aio';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { hostUrl } from 'src/app/app-routing.module';
import { handleMsgError } from 'src/app/app.module';
import { lastValueFrom } from 'rxjs';
/* Validación de empresa */
const empresaSchema = z.object({
  id: z.number().optional(),
  nombre: z.string().min(3, "Minimo 3 errores para el nombre").max(50, "Maximo 50 caracteres para el nombre"),
  nit: z.string().min(3, "minimo 3 letras para el nit").max(50, "maximo 50 letras para el nit"),
  sigla: z.string().min(1, "minimo 1 letra para la sigla").max(10, "maximo 10 letras para la sigla"),
  telefono: z.string().min(3, "minimo 3 letras para el telefono").max(50, "maximo 50 letras para el telefono").optional().or(z.literal("")),
  correo: z.string().email(" invalido").optional().or(z.literal("")).optional().or(z.literal("")),
  direccion: z.string().min(3, "miniom 3 letras para la dirección").max(50, "maximo 50 letras para la dirección").optional().or(z.literal("")),
  niveles: z.number().min(3, "nivel minimo 3").max(7, "nivel maximo 7"),
});
export interface Moneda {
  id: number;
  nombre: string;
  descripcion: string | null;
  abreviatura: string;
}

@Component({
  selector: 'app-dialog-empresa',
  templateUrl: './dialog-empresa.component.html',
  styleUrls: ['./dialog-empresa.component.css']
})


export class DialogEmpresaComponent {
  niveles = [3, 4, 5, 6, 7];
  empresa = this.data;
  empresaForm = new FormGroup({
    nombre: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
    nit: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
    sigla: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(10)]),
    telefono: new FormControl('', [Validators.minLength(3), Validators.maxLength(50)]),
    correo: new FormControl('', [Validators.email]),
    direccion: new FormControl('', [Validators.minLength(3), Validators.maxLength(50)]),
    moneda: new FormControl<Moneda | null>({ value: null, disabled: (this.empresa && this.empresa?.monedas.length > 1 ? true : false) }, [Validators.required]),
    niveles: new FormControl<number>(3, [Validators.required, Validators.min(3), Validators.max(7)]),
  });

  http = inject(HttpClient);
  monedas = [] as Moneda[];
  titulo = "";
  ngOnInit(): void {
    this.updateMonedas();
    if (this.empresa) {
      this.titulo = "Editar Empresa " + this.empresa.nombre;
      this.empresaForm.setValue({
        nombre: this.empresa.nombre,
        nit: this.empresa.nit,
        sigla: this.empresa.sigla,
        telefono: this.empresa.telefono,
        correo: this.empresa.correo,
        direccion: this.empresa.direccion,
        niveles: this.empresa.niveles,
        moneda: null
      });
    } else {
      this.titulo = "Crear Empresa";
    }


  }
  getMonedas = async () => {
    const response = this.http.get<Moneda[]>(`${hostUrl}/api/moneda/usuario`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    });
    return await lastValueFrom(response);
  };
  updateMonedas = () => {
    this.getMonedas().then((m) => {
      this.monedas = m;
      if (this.empresa) {
        this.empresaForm.patchValue({
          moneda: m.find((moneda) => moneda.id == this.empresa?.monedas[0].moneda_principal_id)
        });
      }
    });
  };
  crearEmpresa() {
    console.log("Crear empresa");
    console.log(this.empresaForm.value, "XDDD");
    try {
      /* Purgar todos los notify anteriorres */
      empresaSchema.parse(this.empresaForm.value);
      console.log("Validación correcta");
      const body = {
        id: this.empresa?.id,
        correo: this.empresaForm.value.correo,
        direccion: this.empresaForm.value.direccion,
        nit: this.empresaForm.value.nit,
        nombre: this.empresaForm.value.nombre,
        niveles: this.empresaForm.value.niveles,
        sigla: this.empresaForm.value.sigla,
        telefono: this.empresaForm.value.telefono,
        moneda_id: this.empresaForm.value.moneda?.id

      };
      console.log("Request body", body);
      this.http.post(`${hostUrl}/api/empresa/upsert`, body, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
          "Content-Type": "application/json",
        },
      }).subscribe({
        next: (v) => {
          Notify.success("Empresa creada correctamente");
          this.dialogRef.close(v as Empresa);
        },
        error: (e) => {
          handleMsgError(e);
        },
        complete: () => console.info('complete')
      });

    } catch (error: any) {
      console.log(error);
      if (error instanceof z.ZodError) {
        for (const issue of error.issues) {
          Notify.failure(issue.message + " en " + issue.path[0]);
        }
      }
    }
  }
  constructor(
    public dialogRef: MatDialogRef<DialogEmpresaComponent>,

    @Inject(MAT_DIALOG_DATA) public data: Empresa | null,
  ) { }

}
