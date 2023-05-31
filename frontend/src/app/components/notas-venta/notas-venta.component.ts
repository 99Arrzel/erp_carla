import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-notas-venta',
  templateUrl: './notas-venta.component.html',
  styleUrls: ['./notas-venta.component.css']
})
export class NotasVentaComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }



  verNota() {

  }

  crearNota() {
    
  }
  selectedNota: any = null;

  notas = <any>[];
  cols = ['Nº', 'Fecha', 'Descripción', 'Estado'];

  http = inject(HttpClient);
  ngOnInit() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.http.post(`${hostUrl}/api/notas/listar`, {
      empresa_id: id_empresa,
      tipo: 'COMPRA'
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
