import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-notas-compra',
  templateUrl: './notas-compra.component.html',
  styleUrls: ['./notas-compra.component.css']
})
export class NotasCompraComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }

  notaCompra = new FormGroup({
    fecha: new FormControl(new Date(), [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    empresa_id: new FormControl<number | null>(null, [Validators.required]),
    /* Lista de lotes */
    lotes: new FormControl([], [Validators.required]),
  });

  verNota() {
    if (this.selectedNota == null) {
      Notify.failure("Debe seleccionar una nota");
      return;
    }
    this.router.navigate(["empresa/" + this.empresa_id + "/notas_compra/ver/" + this.selectedNota.id]);
  }
  crearNota() {
    this.router.navigate(["empresa/" + this.empresa_id + "/notas_compra/crear"]);
  }
  selectedNota: any = null;

  notas = <any>[];
  cols = ['Nº', 'Fecha', 'Descripción', 'Estado'];

  empresa_id = null as number | null;
  http = inject(HttpClient);
  ngOnInit() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.empresa_id = id_empresa;
    this.notaCompra.patchValue({ empresa_id: id_empresa });



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


};
