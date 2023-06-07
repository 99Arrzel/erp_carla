import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Confirm, Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-ver-nota-venta',
  templateUrl: './ver-nota-venta.component.html',
  styleUrls: ['./ver-nota-venta.component.css']
})
export class VerNotaVentaComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  nota: any = null;
  empresa_id: number | null = null;

  http = inject(HttpClient);

  anularNota() {
    //Notiflix de confirmación
    Confirm.show('¿Está seguro que desea anular la nota de venta?', 'Anular nota de venta', 'Si', 'No', () => {
      this.http.post(`${hostUrl}/api/notas/anular_venta`, {
        nota_id: this.nota.id,
      },
        {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        }).subscribe({
          next: (data: any) => {
            this.nota = data;
            Notify.success("Nota anulada");
            this.fetchNota();
            console.log(data);
          },
          error: (error: any) => {
            Notify.failure("Error al anular nota");
            console.log(error);
          }
        });
    }, () => {
      Notify.failure('No se anuló la nota de venta');
    }
    );


  }
  goBack() {
    this.router.navigate([`/empresa/${this.empresa_id}/notas_venta`]);
  }
  id_nota: number | null = null;
  fetchNota() {
    this.http.post(`${hostUrl}/api/notas/una_nota_venta`, {
      nota_id: this.id_nota,
    },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data: any) => {
          this.nota = data;
          console.log(data);
        }
      });
  }
  ngOnInit() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    let id_nota = this.route.snapshot.paramMap.get('id_nota') as unknown as number;
    this.empresa_id = id_empresa;
    this.id_nota = id_nota;

    this.fetchNota();
  }

}
