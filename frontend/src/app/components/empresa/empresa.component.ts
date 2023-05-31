import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { hostUrl } from 'src/app/app-routing.module';
import { Empresa } from '../home/home.component';

@Component({
  selector: 'app-empresa',
  templateUrl: './empresa.component.html',
})
export class EmpresaComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  http = inject(HttpClient);
  showFiller = false;
  empresa: Empresa = {
    id: 0,
    nombre: "",
    nit: "",
    sigla: "",
    telefono: "",
    correo: "",
    direccion: "",
    niveles: 0,
    estado: true,
    monedas: []
  };
  usuario: any = {
    id: 0,
    nombre: "",
  };
  goEmpresas() {
    this.router.navigate(["/"]);
  }
  cerrarSesion() {
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
    window.location.reload();
  }
  categorias() {
    this.router.navigate(["empresa/" + this.empresa.id + "/categorias"]);
  }
  articulos() {
    this.router.navigate(["empresa/" + this.empresa.id + "/articulos"]);
  }

  gestiones() {
    this.router.navigate(["empresa/" + this.empresa.id + "/gestiones"]);
  }
  cuentas() {
    this.router.navigate(["empresa/" + this.empresa.id + "/cuentas"]);
  }
  monedas() {
    this.router.navigate(["empresa/" + this.empresa.id + "/configuracion/moneda"]);
  }
  reportes() {
    this.router.navigate(["empresa/" + this.empresa.id + "/reportes"]);
  }
  comprobantes() {
    this.router.navigate(["empresa/" + this.empresa.id + "/configuracion/comprobantes"]);
  }
  notas_compra() {
    this.router.navigate(["empresa/" + this.empresa.id + "/notas_compra"]);
  }
  notas_venta() {
    this.router.navigate(["empresa/" + this.empresa.id + "/notas_venta"]);
  }

  integracion() {
    this.router.navigate(["empresa/" + this.empresa.id + "/integracion"]);
  }
  fetchEmpresa(id: number) {
    this.http.post(`${hostUrl}/api/empresa/una_empresa`, {
      id: id
    }, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data: any) => {
      this.empresa = data;
    });
  }
  ngOnInit() {
    //Search token in local storage
    if (!localStorage.getItem("token")) {
      console.log("No token found");
      this.router.navigate(["/login"]);
      return;
    }
    this.usuario.nombre = localStorage.getItem("usuario");
    this.route.paramMap.subscribe(params => {
      console.log(params.get("id"), "los params");
      this.fetchEmpresa(params.get("id") as unknown as number);
    });
  }
}
