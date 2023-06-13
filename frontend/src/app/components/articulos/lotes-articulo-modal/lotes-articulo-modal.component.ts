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
  }

  constructor(
    public dialog: MatDialogRef<LotesArticuloModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { articulo: any; },

  ) { }

}
