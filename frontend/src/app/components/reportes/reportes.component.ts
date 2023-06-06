import { Empresa } from './../home/home.component';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Moneda } from '../home/dialog-empresa/dialog-empresa.component';
import { Notify } from 'notiflix';
import { MenuItem } from 'primeng/api';

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

  items: MenuItem[] = [
    {
      label: 'Balance Inicial',
    }, {
      label: 'Libro Diario',
    }, {
      label: 'Libro Mayor',
    }
    , {
      label: 'Sumas y Saldos',
    }
  ];

  activeItem: MenuItem = this.items[0];


  id_gestion_bi: number = 0;
  id_monedaa_bi: number = 0;
  verReporteBI = () => {
    const url = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteBalanceInicial&standAlone=true";
    abrirReporte({
      baseUrlReporte: url,
      parameters: {
        id_moneda: this.id_monedaa_bi.toString(),
        id_gestion: this.id_gestion_bi.toString(),
      }
    });
  };
  verReporteLD = () => {
    this.id_gestion_ld;
    this.id_periodo_ld;
    this.id_moneda_ld;
    this.todo_ld;
    const url = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteLibroDiario&standAlone=true";
    abrirReporte({
      baseUrlReporte: url,
      parameters: {
        id_moneda: this.id_moneda_ld.toString(),
        id_periodo: this.id_periodo_ld.toString(),
        id_gestion: this.id_gestion_ld.toString(),
        traer_todo: this.todo_ld.toString(),
        todos: this.todo_ld.toString(),
        todo: this.todo_ld.toString(),
      }
    });
  };


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
    } else if (data == 'Sumas y Saldos') {
      baseUrl = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteSumasSaldo&standAlone=true";
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
  };
  cambiarPeriodos = (gestion: any) => {
    //Cambias los periodos a los periodos de la gestion seleccionada
    this.periodos = gestion.periodos;
  };
  setActionGestionBI = (gestion: any) => {
    this.id_gestion_bi = gestion.source.value.id;
  };
  id_gestion_ld = 0;
  setActionGestionLD = (gestion: any) => {
    this.id_gestion_bi = gestion.source.value.id;
    this.periodosLD = gestion.source.value.periodos;
  };
  periodosLD: any = [];
  id_periodo_ld = 0;
  setActionPeriodoLD = (periodo: any) => {
    this.id_periodo_ld = periodo.source.value.id;
  };
  id_moneda_ld = 0;
  setActionMonedaLD = (moneda: any) => {
    this.id_moneda_ld = moneda.source.value.id;
  };
  todo_ld = "SI";
  setActionTodoLD = (todo: any) => {
    console.log(todo.source.value); //?????????
    this.todo_ld = todo.source.value;
  };

  /* LM */
  id_gestion_lm = 0;
  setActionGestionLM = (gestion: any) => {
    this.id_gestion_lm = gestion.source.value.id;
    this.periodosLM = gestion.source.value.periodos;
  };
  periodosLM: any = [];
  id_periodo_lm = 0;
  setActionPeriodoLM = (periodo: any) => {
    this.id_periodo_lm = periodo.source.value.id;
  };
  id_moneda_lm = 0;
  setActionMonedaLM = (moneda: any) => {
    this.id_moneda_lm = moneda.source.value.id;
  };
  todo_lm = "SI";
  setActionTodoLM = (todo: any) => {
    console.log(todo.source.value); //?????????
    this.todo_lm = todo.source.value;
  };
  verReporteLM = () => {
    const url = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteLibroMayor&standAlone=true";
    abrirReporte({
      baseUrlReporte: url,
      parameters: {
        id_moneda: this.id_moneda_lm.toString(),
        id_periodo: this.id_periodo_lm.toString(),
        id_gestion: this.id_gestion_lm.toString(),
        traer_todo: this.todo_lm.toString(),
        todos: this.todo_lm.toString(),
        todo: this.todo_lm.toString(),
      }
    });
  };
  /* === */

  /* Sumas y saldos */
  id_gestion_ss = 0;

  setActionGestionSS = (gestion: any) => {
    this.id_gestion_ss = gestion.source.value.id;
  };
  id_moneda_ss = 0;
  setActionMonedaSS = (moneda: any) => {
    this.id_moneda_ss = moneda.source.value.id;
  };
  //Ahora ES
  id_gestion_es = 0;
  setActionGestionES = (gestion: any) => {
    this.id_gestion_es = gestion.source.value.id;
  };
  id_moneda_es = 0;
  setActionMonedaES = (moneda: any) => {
    this.id_moneda_es = moneda.source.value.id;
  };
  verReporteES = () => {
    const url = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteSumasSaldo&standAlone=true";
    abrirReporte({
      baseUrlReporte: url,
      parameters: {
        id_moneda: this.id_moneda_es.toString(),
        id_gestion: this.id_gestion_es.toString(),
      }
    });

  }


    ;
  verReporteSS = () => {
    const url = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FReportes&reportUnit=%2FReportes%2FReporteSumasSaldo&standAlone=true";
    abrirReporte({
      baseUrlReporte: url,
      parameters: {
        id_moneda: this.id_moneda_ss.toString(),
        id_gestion: this.id_gestion_ss.toString(),
      }
    });
  };


  /* ========= */


  setActionMonedaBI = (moneda: any) => {
    this.id_monedaa_bi = moneda.source.value.id;
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
    console.log(this.items[0]);
    console.log(this.activeItem);
  };

};
