import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Empresa } from '../home/home.component';
import { DialogGestionComponent } from './dialog-gestion/dialog-gestion.component';
import { Confirm } from 'notiflix/build/notiflix-confirm-aio';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
export interface Gestiones {
  id: number;
  nombre: string;
  fechaInicio: string;
  fechaFin: string;
  estado: boolean;
}

/* Confirm make it all red */

Confirm.init({
  titleColor: '#ff0000',
  okButtonBackground: '#ff0000',
});




/* Extendemos la interface empresa y le ponemos gestiones */
export interface EmpresaYGestiones extends Empresa {
  gestiones: Gestiones[];
}




@Component({
  selector: 'app-gestiones',
  templateUrl: './gestiones.component.html',
  styleUrls: ['./gestiones.component.css']
})
export class GestionesComponent {
  constructor(private route: ActivatedRoute, public dialog: MatDialog, private router: Router) { }
  http = inject(HttpClient);
  empresa: EmpresaYGestiones | undefined;

  displayedColumns: string[] = ['id', 'nombre', 'fechaInicio', 'fechaFin', 'estado'];

  selectedGestion: Gestiones | undefined;

  openReportes() {
    const baseUrlReporteGestion = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FZ&reportUnit=%2FZ%2Fgestion_report&standAlone=true";
    abrirReporte({
      baseUrlReporte: baseUrlReporteGestion,
      parameters: {
        idEmpresa: this.empresa?.id.toString() as string,
      }
    });
  }
  openPeriodos() {
    /* Redireccionamos a periodos */
    this.router.navigate([`${this.selectedGestion?.id}/periodos`], { relativeTo: this.route });

  }
  openDialogCrear() {
    const dialogRef = this.dialog.open(DialogGestionComponent, {
      data: {
        gestion: null,
        empresa_id: this.empresa?.id as number
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.updateGestiones();
        }
      },

    }
    );
  }
  openDialogEditar() {
    const dialogRef = this.dialog.open(DialogGestionComponent, {
      data: {
        gestion: this.selectedGestion,
        empresa_id: this.empresa?.id as number
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.updateGestiones();
        }
      },

    }
    );
  }
  openDialogDelete() {
    Confirm.show(
      'Eliminar',
      '¿Está seguro que desea eliminar la gestión?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/gestion/eliminar`, { id: this.selectedGestion?.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe({
            next: (data) => {
              console.log(data, "la data");
              this.updateGestiones();
            },
            error: (e) => {
              console.error(e);
              this.updateGestiones();
            },

          });
      }
    );

  }
  openDialogCerrar() {
    Confirm.show(
      'Cerrar',
      '¿Está seguro que desea cerrar la gestión?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/gestion/cerrar`, { id: this.selectedGestion?.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe(data => {
            console.log(data, "la data");
            this.updateGestiones();
          });
      }
    );

  }

  updateGestiones() {
    let id = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.http.post<any>(`${hostUrl}/api/gestion/por_empresa`, { id: id }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    })
      .subscribe(data => {
        console.log(data, "la data");
        this.empresa = data;
      });
  }
  ngOnInit(): void {
    /* Fetch gestiones */
    this.updateGestiones();
  }

}
