import { Empresa } from './../home/home.component';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Moneda } from '../home/dialog-empresa/dialog-empresa.component';
import { Notify } from 'notiflix';

@Component({
  selector: 'app-reportes',
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.css']
})
export class ReportesComponent {
  /* reportes es un valor, de una tabla, que tiene  ['Nombre', 'Gestion', 'Periodo', 'Moneda', 'Traer todo']*/
  /* Nombre es un valor, "Balance Inicial, Libro Diario, Libro Mayor, Sumas y saldos"  y los demáas solo son dropdowns con opciones en la tabla*/
  constructor(private route: ActivatedRoute, public dialog: MatDialog, private router: Router) { }
  http = inject(HttpClient);
  reportes =
    ['Balance Inicial', 'Libro Diario', 'Libro Mayor', 'Sumas y Saldos'];
  verReporte = (data: any) => {
    //console.log(this.gestion, this.periodo, this.moneda, this.todo);
    console.log(data);
    if (this.gestion == null) {
      Notify.failure("Seleccione una gestion");
      return;
    }
    if (this.periodo == null) {
      Notify.failure("Seleccione un periodo");
      return;
    }
    if (this.moneda == null) {
      Notify.failure("Seleccione una moneda");
      return;
    }
    if (this.todo == null) {
      Notify.failure("Seleccione si desea traer todo");
      return;
    }

    let baseUrl = "";
    //Ahora hay que ver si es balance inicial, libro diario, libro mayor o sumas y saldos
    if (data == 'Balance Inicial') {
      baseUrl = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteBalanceInicial&standAlone=true";
    } else if (data == 'Libro Diario') {
      baseUrl = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteLibroDiario&standAlone=true";
    } else if (data == 'Libro Mayor') {
      baseUrl = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteLibroMayor&standAlone=true";

    }

    abrirReporte({
      baseUrlReporte: baseUrl,
      parameters: {
        id_moneda: this.moneda.toString(),
        id_periodo: this.periodo.toString(),
        id_gestion: this.gestion.toString(),
        traer_todo: this.todo.toString(),
        todos: this.todo.toString(),
        todo: this.todo.toString(),
      }
    });
    //abrirReporte();



  };
  cambiarPeriodos = (gestion: any) => {
    //Cambias los periodos a los periodos de la gestion seleccionada
    this.periodos = gestion.periodos;
  };
  setActionGestion = (gestion: any) => {
    this.periodos = gestion.source.value.periodos;
    this.gestion = gestion.source.value.id;
  };
  setActionPeriodo = (periodo: any) => {
    this.periodo = periodo.source.value.id;
  };
  setActionMoneda = (moneda: any) => {
    this.moneda = moneda.source.value.id;
  };
  setActionTodo = (todo: any) => {
    if (this.todo == "SI") {
      this.todo = "NO";
    } else {
      this.todo = "SI";
    }

  };


  gestion: any = null;
  periodo: any = null;
  moneda: any = null;
  todo: any = "SI";
  gestiones: any = [];
  periodos: any = [];
  empresa: any = null;
  setearPeriodo = (cambio: any) => {
    console.log(cambio.source.value.peridos);
    this.periodos = cambio.source.value.periodos;
  };
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
        this.gestiones = data.gestiones;
      });

  }
  monedas: Moneda[] = [];
  fetchMonedas() {
    this.http.get<Moneda[]>(`${hostUrl}/api/moneda/usuario`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data) => {
      console.log("Data: ", data);
      this.monedas = data;
    });
  }

  ngOnInit(): void {
    /* Fetch gestiones */
    this.fetchMonedas();
    this.updateGestiones();

  };

};
