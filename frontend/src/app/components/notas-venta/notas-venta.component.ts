import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Notify } from 'notiflix';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-notas-venta',
  templateUrl: './notas-venta.component.html',
  styleUrls: ['./notas-venta.component.css']
})
export class NotasVentaComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }


  reporteNota() {

    if (this.selectedNota == null) {
      Notify.failure("Debe seleccionar una nota");
      return;
    }

    const baseUrlReporteGestion = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FNotaVentaReport&standAlone=true";
    abrirReporte({
      baseUrlReporte: baseUrlReporteGestion,
      parameters: {
        id_nota: this.selectedNota.id
      }
    });
    Notify.success("Reporte abierto con éxito");
  }
  verNota() {
    this.router.navigate(["empresa/" + this.empresa_id + "/notas_venta/ver/" + this.selectedNota.id]);
  }

  crearNota() {
    this.router.navigate(["empresa/" + this.empresa_id + "/notas_venta/crear"]);
  }
  selectedNota: any = null;

  notas = <any>[];
  cols = ['Nº', 'Fecha', 'Descripción', 'Estado'];

  http = inject(HttpClient);
  empresa_id = null as number | null;
  ngOnInit() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.empresa_id = id_empresa;
    this.http.post(`${hostUrl}/api/notas/listar`, {
      empresa_id: id_empresa,
      tipo: 'VENTA'
    },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data: any) => {
          this.notas = data;
          console.log(data);
        }
      });
  }
}
