import { HttpClient } from '@angular/common/http';
import { Component, inject, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-dialog-eliminar',
  templateUrl: './dialog-eliminar.component.html',
  styleUrls: ['./dialog-eliminar.component.css']
})
export class DialogEliminarComponent {
  http = inject(HttpClient);
  eliminar = () => {
    this.http.post(`${hostUrl}/api/empresa/delete`, {
      id: this.data
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (v) => {
        console.log(v);
        /* Cerrar */
        this.dialogRef.close(true);
      },
      error: (e) => console.error(e),
      complete: () => console.info('complete')
    });
  };
  constructor(
    public dialogRef: MatDialogRef<DialogEliminarComponent>,
    @Inject(MAT_DIALOG_DATA) public data: number,
  ) { }
}
