import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import * as Notiflix from 'notiflix';
import { hostUrl } from 'src/app/app-routing.module';

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
  articulos: any[] = [];

  cols: any[] = [ //Columnas de la tabla
    { field: 'nombre', header: 'Nombre' },
    { field: 'descripcion', header: 'Descripción' },
    { field: 'precio_venta', header: 'Precio de venta' },

    { field: 'acciones', header: 'Acciones' },
  ];

  categorias: any[] = [];
  crearArticulo() {
    console.log("epa");
    if (this.articuloForm.value.nombre?.trim() == "") {
      Notiflix.Notify.failure("El nombre del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.descripcion?.trim() == "") {
      Notiflix.Notify.failure("La descripción del artículo no puede estar vacía");
      return;
    }
    if (this.articuloForm.value.precio_venta == null) {
      Notiflix.Notify.failure("El precio de venta del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.precio_venta && this.articuloForm.value.precio_venta <= 0) {
      Notiflix.Notify.failure("El precio de venta del artículo no puede estar vacío");
      return;
    }
    if (this.articuloForm.value.categorias?.length == 0) {
      Notiflix.Notify.failure("Seleccione al menos una categoría");
      return;
    }

    if (this.titulo == "Crear Artículo") {
      this.http.post(`${hostUrl}/api/articulo/crear`, {
        /*    data.getNombre(),
                      data.getDescripcion(),
                      data.getPrecio(),
                      data.getCategorias(), */
        nombre: this.articuloForm.value.nombre,
        descripcion: this.articuloForm.value.descripcion,
        precio: this.articuloForm.value.precio_venta,
        categorias: this.articuloForm.value.categorias,
        empresa_id: this.id_empresa,
      }, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data) => {
          Notiflix.Notify.success("Artículo creado con éxito");
          console.log(data);
          this.articuloForm.reset();
          this.fetchArticulos();
          //this.updateArticulos();
        },
        error: (e) => {
          Notiflix.Notify.failure(e.error?.msg);
        },
      });
    }
    else {
      this.http.post(`${hostUrl}/api/articulo/editar`, {

        id: this.articuloForm.value.id,
        nombre: this.articuloForm.value.nombre,
        descripcion: this.articuloForm.value.descripcion,
        precio: this.articuloForm.value.precio_venta,
        categorias: this.articuloForm.value.categorias,
      }, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data) => {
          Notiflix.Notify.success("Artículo editado con éxito");
          console.log(data);
          //Resetear form

          this.articuloForm.reset();
          this.fetchArticulos();
          //this.updateArticulos();
        },
        error: (e) => {
          Notiflix.Notify.failure(e.error?.msg);
        }
      });
      this.titulo = "Crear Artículo";
    }


    console.log(this.articuloForm.value);
  }
  editarArticulo(a: any) {
    //seteamo el form
    this.articuloForm.controls.id.setValue(a.id);
    this.articuloForm.controls.nombre.setValue(a.nombre);
    this.articuloForm.controls.descripcion.setValue(a.descripcion);
    this.articuloForm.controls.precio_venta.setValue(a.precio);
    this.articuloForm.controls.categorias.setValue(a.categoria);
    this.titulo = "Editar Artículo";

    console.log(a);
  }
  eliminarArticulo(a: any) {
    Notiflix.Confirm.show(
      'Eliminar Artículo',
      '¿Está seguro que desea eliminar el artículo?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/articulo/eliminar`, { id: a.id }, {
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
