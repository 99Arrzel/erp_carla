import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpErrorResponse } from "@angular/common/http";
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogEmpresaComponent } from './components/home/dialog-empresa/dialog-empresa.component';
import { DialogEliminarComponent } from './components/home/dialog-eliminar/dialog-eliminar.component';
import { EmpresaComponent } from './components/empresa/empresa.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { GestionesComponent } from './components/gestiones/gestiones.component';
import { CuentasComponent } from './components/cuentas/cuentas.component';
import { MatTableModule } from '@angular/material/table';
import { DialogGestionComponent } from './components/gestiones/dialog-gestion/dialog-gestion.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
//mat-checkbox
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TableModule } from 'primeng/table';
import { PeriodosComponent } from './components/gestiones/periodos/periodos.component';
import { DialogPeriodoComponent } from './components/gestiones/periodos/dialog-periodo/dialog-periodo.component';

import { TreeModule } from 'primeng/tree';
import { MatSelectModule } from '@angular/material/select';
import { DialogCuentaComponent } from './components/cuentas/dialog-cuenta/dialog-cuenta.component';
import { Notify } from 'notiflix';
import { MonedasComponent } from './components/monedas/monedas.component';
import { InputNumberModule } from 'primeng/inputnumber';
import { ComprobantesComponent } from './components/comprobantes/comprobantes.component';
import { CrearComprobanteComponent } from './components/comprobantes/crear-comprobante/crear-comprobante.component';
import { DialogDetallesComprobanteComponent } from './components/comprobantes/dialog-detalles-comprobante/dialog-detalles-comprobante.component';
import { ReportesComponent } from './components/reportes/reportes.component';
import { CategoriasComponent } from './components/categorias/categorias.component';
import { ArticulosComponent } from './components/articulos/articulos.component';
import { CrearCategoriaComponent } from './components/categorias/crear-categoria/crear-categoria.component';
import { EditarCategoriaComponent } from './components/categorias/editar-categoria/editar-categoria.component';
import { CuentasIntegracionComponent } from './components/cuentas-integracion/cuentas-integracion.component';
import { NotasCompraComponent } from './components/notas-compra/notas-compra.component';
import { NotasVentaComponent } from './components/notas-venta/notas-venta.component';
import { CrearNotaCompraComponent } from './components/notas-compra/crear-nota-compra/crear-nota-compra.component';
import { CrearNotaVentaComponent } from './components/notas-venta/crear-nota-venta/crear-nota-venta.component';
import { CalendarModule } from 'primeng/calendar';
import { VerNotaCompraComponent } from './components/notas-compra/ver-nota-compra/ver-nota-compra.component';
import { VerNotaVentaComponent } from './components/notas-venta/ver-nota-venta/ver-nota-venta.component';
import { TabViewModule } from 'primeng/tabview';


export function handleUnauthorizedError(e: HttpErrorResponse) {
  if (e.status == 401) {
    localStorage.removeItem("token");
    window.location.href = "/login";
  }
}
export function handleMsgError(e: HttpErrorResponse) {
  //Check if e has a message "msg"
  if (e.error.msg) {
    Notify.failure(e.error.msg);
  }
}





@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    DialogEmpresaComponent,
    DialogEliminarComponent,
    EmpresaComponent,
    GestionesComponent,
    CuentasComponent,
    DialogGestionComponent,
    PeriodosComponent,
    DialogPeriodoComponent,
    DialogCuentaComponent,
    MonedasComponent,
    ComprobantesComponent,
    CrearComprobanteComponent,
    DialogDetallesComprobanteComponent,
    ReportesComponent,
    CategoriasComponent,
    ArticulosComponent,
    CrearCategoriaComponent,
    EditarCategoriaComponent,
    CuentasIntegracionComponent,
    NotasCompraComponent,
    NotasVentaComponent,
    CrearNotaCompraComponent,
    CrearNotaVentaComponent,
    VerNotaCompraComponent,
    VerNotaVentaComponent,


  ],
  imports: [
    TabViewModule,
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatTooltipModule,
    MatIconModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    MatSidenavModule,
    MatTableModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatCheckboxModule,
    TableModule,
    TreeModule,
    MatSelectModule,
    InputNumberModule,
    MatSlideToggleModule,
    CalendarModule



  ],
  providers: [],
  bootstrap: [AppComponent],
  exports: [
    DialogEmpresaComponent
  ]
})
export class AppModule { }
