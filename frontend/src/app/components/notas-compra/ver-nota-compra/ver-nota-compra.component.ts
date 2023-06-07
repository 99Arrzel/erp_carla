import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Confirm, Notify } from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-ver-nota-compra',
  templateUrl: './ver-nota-compra.component.html',
  styleUrls: ['./ver-nota-compra.component.css']
})
export class VerNotaCompraComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  nota: any = null;

  http = inject(HttpClient);

  anularNota() {
    //Notiflix de confirmación
    Confirm.show('¿Está seguro que desea anular la nota de compra?', 'Anular nota de compra', 'Si', 'No', () => {
      this.http.post(`${hostUrl}/api/notas/anular_compra`, {
        nota_id: this.id_nota
      },
        {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        }).subscribe({
          next: (data: any) => {
            this.router.navigate(["empresa/" + this.route.parent?.snapshot.paramMap.get('id') + "/notas_compra"]);
            Notify.success("Nota anulada");
          },
          error: (error: any) => {
            console.log(error);
            Notify.failure(error.error);
          }

        });
    }, () => {
      Notify.failure('No se anuló la nota de compra');
    }
    );

  }
  goBack() {
    this.router.navigate(["empresa/" + this.route.parent?.snapshot.paramMap.get('id') + "/notas_compra"]);
  }

  id_nota: number = this.route.snapshot.paramMap.get('id_nota') as unknown as number;
  ngOnInit() {
    this.http.post(`${hostUrl}/api/notas/una_nota_compra`, {
      nota_id: this.id_nota
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
}
