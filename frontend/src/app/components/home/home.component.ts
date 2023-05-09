import { Component, inject } from '@angular/core';
import { Router } from "@angular/router";
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DialogEmpresaComponent, Moneda } from './dialog-empresa/dialog-empresa.component';
import { DialogEliminarComponent } from './dialog-eliminar/dialog-eliminar.component';
import { abrirReporte, getReport, hostUrl, reportUrl } from 'src/app/app-routing.module';
import { handleUnauthorizedError } from 'src/app/app.module';
import { lastValueFrom } from 'rxjs';

export interface EmpresaMonedaModel {
  id: number;
  cambio: number;
  estado: boolean;
  fechaCreacion: Date;
  empresa: Empresa;
  moneda_principal: Moneda;
  moneda_principal_id: number;
  moneda_alternativa: Moneda | null;
  moneda_alternativa_id: number | null;
  nombre_moneda_principal: string | null;
  nombre_moneda_alternativa: string | null;


}
export interface Empresa {
  id: number;
  nombre: string;
  nit: string;
  sigla: string;
  telefono: string;
  correo: string;
  direccion: string;
  niveles: number;
  estado: boolean;

  monedas: EmpresaMonedaModel[];
}


@Component({
  templateUrl: './home.component.html',
})

export class HomeComponent {
  constructor(private router: Router, public dialog: MatDialog) {
  }
  onKeyDown(event: KeyboardEvent) {

  }
  http = inject(HttpClient);
  empresas: Empresa[] = [];

  empresaSelected: Empresa | null = null;
  getUpdatedEmpresas = async () => {
    const response = this.http.get<Empresa[]>(`${hostUrl}/api/empresa/mis_empresas`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    });
    return await lastValueFrom(response);
  };
  updateEmpresas = () => {

    this.getUpdatedEmpresas()

      .then((empresas) => {
        this.empresas = empresas;
      }).catch((err: HttpErrorResponse) => {
        handleUnauthorizedError(err);
      });


  };

  ngOnInit() {
    //Search token in local storage
    if (!localStorage.getItem("token")) {
      console.log("No token found");
      this.router.navigate(["/login"]);
      return;
    }
    this.updateEmpresas();

  }
  openDialogCrear() {
    const dialogRef = this.dialog.open(DialogEmpresaComponent);
    dialogRef.afterClosed().subscribe((v) => {
      if (v) {
        this.getUpdatedEmpresas().then((empresas) => {
          this.empresas = empresas;
          for (let a of this.empresas) {
            if (a.id == v.id) {
              console.log("Econtrada Empresa seleccionada: ", a);
              this.empresaSelected = a;
              return;
            }
          }
        });
      }
    });
  };
  openReportes() {
    const baseUrlEmpresas = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FZ&reportUnit=%2FZ%2Fempresa_report&standAlone=true";
    abrirReporte({
      baseUrlReporte: baseUrlEmpresas,
      parameters: {}
    });
  }
  openDialogEditar() {
    const dialogRef = this.dialog.open(DialogEmpresaComponent, {
      data: this.empresaSelected
    });
    dialogRef.afterClosed().subscribe(
      {
        next: (v: Empresa | null) => {
          if (!v) return;
          this.getUpdatedEmpresas().then((empresas) => {
            this.empresas = empresas;
            for (let a of this.empresas) {
              if (a.id == v.id) {
                this.empresaSelected = a;
                return;
              }
            }
          });
        }
      });
  }
  openDialogDelete() {
    if (!this.empresaSelected) return;
    const dialogRef = this.dialog.open(DialogEliminarComponent, {
      data: this.empresaSelected.id
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {

        this.empresaSelected = null;
        this.updateEmpresas();
      }
    });
  }
  ingresarEmpresa() {
    if (!this.empresaSelected) return;
    this.router.navigate([`/empresa/${this.empresaSelected.id}`]);
  }
}
