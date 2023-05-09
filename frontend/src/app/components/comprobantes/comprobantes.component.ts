import { Moneda } from './../home/dialog-empresa/dialog-empresa.component';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { lastValueFrom } from 'rxjs';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
import { DialogDetallesComprobanteComponent } from './dialog-detalles-comprobante/dialog-detalles-comprobante.component';
import { Notify } from 'notiflix';
interface Valores {
  [key: string]: string;
}
export function urlReporte({ valores, urlBase }: { valores: Valores; urlBase: string; }) {
  let params = new URLSearchParams();
  for (let key in valores) {
    params.append(key, valores[key]);
  }
  return `${urlBase}&${params.toString()}`;
}

@Component({
  selector: 'app-comprobantes',
  templateUrl: './comprobantes.component.html',
  styleUrls: ['./comprobantes.component.css']
})




export class ComprobantesComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  http = inject(HttpClient);
  //crearComprobante redirecciona a la ruta actual + /crear
  crearComprobante = () => {
    this.router.navigate(["empresa/" + this.id_empresa + "/configuracion/comprobantes/crear"]);
  };


  comprobante = new FormGroup({
    glosa: new FormControl('', [Validators.required]),
    fecha: new FormControl('', [Validators.required]),
    tc: new FormControl('', [Validators.required]),
    tipo: new FormControl('', [Validators.required]),
    empresa_id: new FormControl('', [Validators.required]),
    moneda: new FormControl(null as Moneda | null, [Validators.required]),
    detalles: new FormControl([], [Validators.required]),
  });
  id_empresa: number = 0;
  selectedComprobante: any = null;
  verDetallesComprobante = () => {
    //mostrar un matdialog
    if (this.selectedComprobante != null) {
      this.dialog.open(DialogDetallesComprobanteComponent, {
        data: {
          comprobante: this.selectedComprobante
        }
      });
    }
  };
  openReportes = () => {
    //Verificar que este seleccionado
    if (this.selectedComprobante != null) {
      const baseUrlReporteGestion = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FComprobanteReporte&standAlone=true";
      abrirReporte({
        baseUrlReporte: baseUrlReporteGestion,
        parameters: {
          id: this.selectedComprobante.id.toString() as string,
        }
      });
      Notify.success("Reporte abierto con éxito");
    }
    else {
      Notify.failure("Debe seleccionar un comprobante");
    }
  };
  anularComprobante = () => {
    if (this.selectedComprobante != null) {
      if (this.selectedComprobante.estado == "ANULADO") {
        Notify.failure("El comprobante ya está anulado");
        return;
      }
      if (this.selectedComprobante.estado == "CERRADO") {
        Notify.failure("El comprobante ya está cerrado");
        return;
      }
      this.http.get(hostUrl + '/api/comprobante/anular/' + this.selectedComprobante.id, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        },
      }).subscribe({
        next: (data) => {
          console.log(data);
          Notify.success("Comprobante anulado con éxito");
          this.ngOnInit();
        },
        error: (e) => {
          if (e.error?.msg) {
            Notify.failure(e.error?.msg);
          }
          Notify.failure("Error al anular el comprobante");
        }
      });
    }
  };
  cerrarComprobante = () => {
    if (this.selectedComprobante != null) {
      if (this.selectedComprobante.estado == "Anulado") {
        Notify.failure("El comprobante ya está anulado");
        return;
      }
      if (this.selectedComprobante.estado == "Cerrado") {
        Notify.failure("El comprobante ya está cerrado");
        return;
      }
      this.http.get(hostUrl + '/api/comprobante/cerrar/' + this.selectedComprobante.id, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data) => {
          console.log(data);
          Notify.success("Comprobante cerrado con éxito");
          this.ngOnInit();
        },
        error: (e) => {
          if (e.error?.msg) {
            Notify.failure(e.error?.msg);
          }
          Notify.failure("Error al cerrar el comprobante");
        }
      });
    }
  };
  ngOnInit() {
    //Search token in local storage
    this.route.parent?.paramMap.subscribe(params => {
      console.log(params.get("id"), "los params");
      this.id_empresa = Number(params.get("id"));
    });
    /* Request http para la lista */
    this.http.get(hostUrl + '/api/comprobante/listar/' + this.id_empresa, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      },
    }).subscribe((data: any) => {
      this.comprobantes = data;
      console.log(this.comprobantes);
    });

  }

  serie: string = '';
  comprobantes: any[] = [];
  cols: string[] = ["Glosa", "Serie", "Tipo", "Moneda", "TC", "Estado", "Fecha"];
}
