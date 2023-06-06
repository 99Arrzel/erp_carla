import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { TreeNode } from 'primeng/api';
import { hostUrl } from 'src/app/app-routing.module';
import { TreeMakerGeneric } from '../cuentas/cuentas.component';
import { CrearCategoriaComponent } from './crear-categoria/crear-categoria.component';
import * as Notiflix from 'notiflix';
import { EditarCategoriaComponent } from './editar-categoria/editar-categoria.component';

@Component({
  selector: 'app-categorias',
  templateUrl: './categorias.component.html',
  styleUrls: ['./categorias.component.css']
})
export class CategoriasComponent {
  /* Fetch data */
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  http = inject(HttpClient);
  files: any[] = [];


  id_empresa: number = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
  selectedNode: TreeNode | undefined;

  openDialogCrear() {
    //Abrir matdialog
    /* this.dialog.open(DialogDetallesComprobanteComponent, {
        data: {
          comprobante: this.selectedComprobante
        }
      }); */
    const dialogRef = this.dialog.open(CrearCategoriaComponent, {
      data: {
        empresa_id: this.id_empresa,
        categoria_padre_id: this.selectedNode?.data.id

      }

    });
    dialogRef.afterClosed().subscribe((v) => {
      console.log(v, "Cerrado")
        ;
      if (v) {
        this.fetchCategorias();
      }
      /* if (v) {
        this.getUpdatedEmpresas().then((empresas) => {
          this.empresas = empresas;
          for (let a of this.empresas) {
            if (a.id == v.id) {
              console.log("Econtrada Empresa seleccionada: ", a);
              this.empresaSelected = a;
              return;
            }
          }
        });
      } */
    });
  }
  openDialogEditar() {
    const dialogRef = this.dialog.open(EditarCategoriaComponent, {
      data: {
        id: this.selectedNode?.data.id,
        nombre: this.selectedNode?.data.nombre,
        descripcion: this.selectedNode?.data.descripcion,
      }
    }
    );
    dialogRef.afterClosed().subscribe((v) => {
      if (v) {
        this.fetchCategorias();
        this.selectedNode = undefined;
      }
    });
  }
  openDialogDelete() {
    Notiflix.Confirm.show(
      'Eliminar Categoria',
      '¿Está seguro que desea eliminar la categoria?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/categoria/eliminar`, { id: this.selectedNode?.data.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        }).subscribe({
          next: (data) => {
            Notiflix.Notify.success("Cuenta eliminada correctamente");
            this.fetchCategorias();
          }
          ,
          error: (e) => {
            console.error(e);
            Notiflix.Notify.failure("Error al eliminar la cuenta");
          }
        });
      });


  }
  nodeSelect(event: any) {
    console.log(event);
  }
  nodeUnselect(event: any) {
    console.log(event);
  }

  fetchCategorias() {


    this.http.post(`${hostUrl}/api/categoria/listar`, {
      empresa_id: this.id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data: any) => {
        console.log(data, "Data");
        this.files = [{
          expanded: true, label: '0', data: { nombre: 'Categorias', descripcion: 'Raiz' }, children: TreeMakerGeneric(data, null, 'categoria_id')
        }];
        console.log(TreeMakerGeneric(data, null, 'categoria_id'));
      },
      error: (e) => {
        console.error(e);
        Notiflix.Notify.failure("Error al cargar las categorías" + e.error?.msg);
      }
    });
  }
  ngOnInit() {
    this.fetchCategorias();
  }

}

