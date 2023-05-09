import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Gestiones } from '../gestiones.component';
import { Confirm } from 'notiflix/build/notiflix-confirm-aio';
import { DialogPeriodoComponent } from './dialog-periodo/dialog-periodo.component';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
/*


Inteface periodo
"nombre": "Periodo 1",
            "fechaInicio": "2023-06-30",
            "fechaFin": "2023-07-29",
            "estado": true,*/

export interface Periodos {
  id: number;
  nombre: string;
  fechaInicio: string;
  fechaFin: string;
  estado: boolean;
}
export interface GestionConPeriodos extends Gestiones {
  periodos: Periodos[];
}



@Component({
  selector: 'app-periodos',
  templateUrl: './periodos.component.html',
  styleUrls: ['./periodos.component.css']
})



export class PeriodosComponent {
  constructor(private route: ActivatedRoute, public dialog: MatDialog, private router: Router) { }
  http = inject(HttpClient);

  gestion: GestionConPeriodos | undefined;

  selectedPeriodo: Periodos | undefined;

  openDialogCrear() {
    const dialogRef = this.dialog.open(DialogPeriodoComponent, {
      data: {
        periodo: null,
        gestion_id: this.gestion?.id as number
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.update_periodos();
        }
      },

    }
    );
  }
  openDialogEditar() {
    const dialogRef = this.dialog.open(DialogPeriodoComponent, {
      data: {
        periodo: this.selectedPeriodo,
        gestion_id: this.gestion?.id as number
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.update_periodos();
        }
      },

    }
    );
  }
  openReportes() {
    const baseUrlReportePeriodo = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FZ&reportUnit=%2FZ%2Fperiodo_report&standAlone=true";
    const empresa_id = this.route.parent?.snapshot.paramMap.get('id');
    abrirReporte({
      baseUrlReporte: baseUrlReportePeriodo,
      parameters: {
        IdEmpresa: empresa_id as string,
        IdGestion: this.gestion?.id.toString() as string,
      }
    });
  }
  openDialogDelete() {
    Confirm.show(
      'Eliminar',
      '¿Está seguro que desea eliminar el periodo?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/periodo/eliminar`, { id: this.selectedPeriodo?.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe({
            next: (data) => {
              console.log(data, "la data");
              this.update_periodos();
            },
            error: (e) => {
              console.error(e);
              this.update_periodos();
            },

          });
      }
    );

  }
  openDialogCerrar() {
    Confirm.show(
      'Cerrar',
      '¿Está seguro que desea cerrar el periodo?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/periodo/cerrar`, { id: this.selectedPeriodo?.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe(data => {
            console.log(data, "la data");
            this.update_periodos();
          });
      }
    );

  }

  volverAGestiones() {
    this.router.navigate(['../../'], { relativeTo: this.route });
  }

  update_periodos() {
    this.route.paramMap.subscribe(params => {
      this.http.post<GestionConPeriodos>(`${hostUrl}/api/gestion/con_periodos`, {
        id: params.get('gestion_id')
      }, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }).subscribe({
        next: (result) => {
          this.gestion = result;
        }
      });
    });
  }

  ngOnInit(): void {
    this.update_periodos();

  }
}
