import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import * as Notiflix from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';
import { CrearArticuloModalComponent } from './crear-articulo-modal/crear-articulo-modal.component';
import { EditarArticuloModalComponent } from './editar-articulo-modal/editar-articulo-modal.component';
import { LotesArticuloModalComponent } from './lotes-articulo-modal/lotes-articulo-modal.component';

@Component({
  selector: 'app-articulos',
  templateUrl: './articulos.component.html',
  styleUrls: ['./articulos.component.css']
})
export class ArticulosComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }

  articuloForm = new FormGroup({
    id: new FormControl(null),
    nombre: new FormControl('', [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    precio_venta: new FormControl(0, [Validators.required]),
    categorias: new FormControl([], [Validators.required]),
  });


  verLotes() {
    if (this.selected?.id == null) {
      Notiflix.Notify.failure("Seleccione un artículo");
      return;
    }
    const dialogRef = this.dialog.open(LotesArticuloModalComponent, {
      data: {
        articulo: this.selected
      }
    });
  }
  articulos: any[] = [];

  cols: any[] = [ //Columnas de la tabla
    { field: 'nombre', header: 'Nombre' },
    { field: 'descripcion', header: 'Descripción' },
    { field: 'precio_venta', header: 'Precio de venta' },

    { field: 'acciones', header: 'Acciones' },
  ];

  categorias: any[] = [];
  crearArticulo() {

    //Abrir modal del articulo, crear-articulo
    const dialogRef = this.dialog.open(CrearArticuloModalComponent, {
      data: {
        categorias: this.categorias,
        empresa_id: this.id_empresa,
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      this.fetchArticulos();
    });
  }
  editarArticulo() {
    if (this.selected?.id == null) {
      Notiflix.Notify.failure("Seleccione un artículo");
      return;
    }
    //seteamo el form
    const dialogRef = this.dialog.open(EditarArticuloModalComponent, {
      data: {
        articulo: this.selected,
        categorias: this.categorias,
        empresa_id: this.id_empresa,
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
      this.fetchArticulos();
    });
  }
  eliminarArticulo() {
    if (this.selected?.id == null) {
      Notiflix.Notify.failure("Seleccione un artículo");
      return;
    }
    Notiflix.Confirm.show(
      'Eliminar Artículo',
      '¿Está seguro que desea eliminar el artículo?',
      'Si',
      'No',
      () => {
        ////id del selected
        this.http.post<any>(`${hostUrl}/api/articulo/eliminar`, { id: this.selected?.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe({
            next: (data) => {
              Notiflix.Notify.success("Artículo eliminado correctamente");
              this.fetchArticulos();
              this.fetchCategorias();
            },
            error: (e) => {
              console.error(e);

              Notiflix.Notify.failure("Error al eliminar el artículo");
            }

          });
      },
      () => { }
    );


  }
  http = inject(HttpClient);
  id_empresa: number = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
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
        this.categorias = data;
      },
      error: (e) => {
        console.error(e);
        Notiflix.Notify.failure("Error al cargar las categorías" + e.error?.msg);
      }
    });
  }

  selected: any = null;
  listarCategorias(cats: any) {

    let str = "";
    //Es un array de ints, buscamos estos en this.categorias
    for (let cat of cats.categoria) {
      for (let c of this.categorias) {
        if (c.id == cat) {
          str += c.nombre + ", ";
        }
      }
    }
    return str.slice(0, -2);
  }
  fetchArticulos() {
    this.http.post(`${hostUrl}/api/articulo/listar`, {
      empresa_id: this.id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data: any) => {
        console.log(data, "Data");
        this.articulos = data;
      },
      error: (e) => {
        console.error(e);
        Notiflix.Notify.failure("Error al cargar los artículos" + e.error?.msg);
      }
    });
  }
  ngOnInit(): void {
    this.fetchCategorias();
    this.fetchArticulos();
  };
  titulo: string = "Crear Artículo";
}
