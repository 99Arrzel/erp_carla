import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-lotes-articulo-modal',
  templateUrl: './lotes-articulo-modal.component.html',
  styleUrls: ['./lotes-articulo-modal.component.css']
})
export class LotesArticuloModalComponent {

  ngOnInit(): void {
    console.log(this.data.articulo);
    /* Editar data articulo lotes, en estado, si stock == 0 cambiar a "AGOTADO" */
    this.data.articulo.lotes.forEach((lote: any) => {
      if (lote.stock == 0) {
        lote.estado = "AGOTADO";
      }
    }
    );
  }

  constructor(
    public dialog: MatDialogRef<LotesArticuloModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { articulo: any; },

  ) { }

}
