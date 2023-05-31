import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { EmpresaComponent } from './components/empresa/empresa.component';
import { GestionesComponent } from './components/gestiones/gestiones.component';
import { CuentasComponent } from './components/cuentas/cuentas.component';
import { PeriodosComponent } from './components/gestiones/periodos/periodos.component';
import { MonedasComponent } from './components/monedas/monedas.component';
import { ComprobantesComponent } from './components/comprobantes/comprobantes.component';
import { CrearComprobanteComponent } from './components/comprobantes/crear-comprobante/crear-comprobante.component';
import { ReportesComponent } from './components/reportes/reportes.component';
import { CategoriasComponent } from './components/categorias/categorias.component';
import { ArticulosComponent } from './components/articulos/articulos.component';
import { CuentasIntegracionComponent } from './components/cuentas-integracion/cuentas-integracion.component';
import { NotasCompraComponent } from './components/notas-compra/notas-compra.component';
import { NotasVentaComponent } from './components/notas-venta/notas-venta.component';
import { CrearNotaCompraComponent } from './components/notas-compra/crear-nota-compra/crear-nota-compra.component';
import { CrearNotaVentaComponent } from './components/notas-venta/crear-nota-venta/crear-nota-venta.component';

export const hostUrl = 'http://localhost:8085';
export const reportUrl = 'http://localhost:8080';

export function abrirReporte({ baseUrlReporte, parameters }: { baseUrlReporte: string, parameters: anyParametersUrl; }) {

  fetch(`${hostUrl}/api/getMyData`, {
    method: "GET",
    headers: {
      Authorization: "Bearer " + localStorage.getItem("token")
    }
  }).then((response) => {
    return response.json();
  }).then((data) => {
    const url = getReport({
      parameters: {
        "IdUsuario": data.id as string, //Fix reporte
        "idUsuario": data.id as string,
        sessionDecorator: "no",
        chrome: "false",
        decorate: "no",
        toolbar: "false",
        j_username: 'jasperadmin', j_password: 'bitnami',
        ...parameters
      },
      report: baseUrlReporte
    });
    window.open(url, "_blank");
  });

}

export type anyParametersUrl = {
  [key: string]: string;
};

export function getReport({ parameters, report }: { parameters: anyParametersUrl, report: string; }) {
  const url = new URL(`${reportUrl}/${report}`);
  Object.keys(parameters).forEach(key => url.searchParams.append(key, parameters[key]));
  return url;
}

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: HomeComponent },

  {
    path: 'empresa/:id', component: EmpresaComponent, children: [
      { path: 'configuracion/moneda', component: MonedasComponent },
      { path: 'reportes', component: ReportesComponent },
      { path: 'integracion', component: CuentasIntegracionComponent },
      { path: 'categorias', component: CategoriasComponent },
      { path: 'articulos', component: ArticulosComponent },
      { path: 'notas_compra', component: NotasCompraComponent },
      { path: 'notas_compra/crear', component: CrearNotaCompraComponent },

      { path: 'notas_venta', component: NotasVentaComponent },
      { path: 'notas_venta/crear', component: CrearNotaVentaComponent },

      { path: 'configuracion/comprobantes', component: ComprobantesComponent },
      { path: 'configuracion/comprobantes/crear', component: CrearComprobanteComponent },
      { path: 'gestiones', component: GestionesComponent },
      { path: 'gestiones/:gestion_id/periodos', component: PeriodosComponent },
      { path: 'cuentas', component: CuentasComponent }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
