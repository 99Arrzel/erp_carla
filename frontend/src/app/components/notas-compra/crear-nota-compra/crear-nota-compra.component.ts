import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Notify } from 'notiflix';
import { Observable, map } from 'rxjs';
import { hostUrl } from 'src/app/app-routing.module';

@Component({
  selector: 'app-crear-nota-compra',
  templateUrl: './crear-nota-compra.component.html',
  styleUrls: ['./crear-nota-compra.component.css'],
})
export class CrearNotaCompraComponent {

  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }

  notaCompra = new FormGroup({
    /* nro_nota: new FormControl<any>(null, [Validators.required]), */
    /* Disabled nro nota */
    nro_nota: new FormControl<any>({ value: null, disabled: true },),
    fecha: new FormControl(new Date(), [Validators.required]),
    descripcion: new FormControl('', [Validators.required]),
    empresa_id: new FormControl<number | null>(this.route.parent?.snapshot.paramMap.get('id') as unknown as number, [Validators.required]),
    /* Lista de lotes */
    lotes: new FormControl<any>([], [Validators.required]),
  });

  http = inject(HttpClient);
  selectedLote: any = null;

  lotes = new FormGroup({
    articulo: new FormControl<any>(null, [Validators.required]),
    fecha_vencimiento: new FormControl<Date | null>(new Date(),),
    cantidad: new FormControl<number>(0, [Validators.required]),
    precio: new FormControl<number>(0, [Validators.required]),
    subtotal: new FormControl<number>(0, [Validators.required]),
  });
  isDisabled = (id: number) => {
    return false;
  };
  totalLotes() {
    //notaCompra lotes
    let total = 0;
    for (const lote of this.notaCompra.value.lotes) {
      total += lote.subtotal;
    }
    return total;
  }
  addLote() {

    if (this.lotes.value.articulo == null) {
      Notify.failure("Debe seleccionar un artículo");
      return;
    }

    if (this.lotes.value.cantidad == null || this.lotes.value.cantidad <= 0) {
      Notify.failure("Debe ingresar una cantidad");
      return;
    }
    if (this.lotes.value.precio == null || this.lotes.value.precio <= 0) {
      Notify.failure("Debe ingresar un precio");
      return;
    }

    if (this.lotes.invalid) {
      Notify.failure("Debe llenar todos los campos");
      return;
    }
    this.notaCompra.value.lotes.push(this.lotes.value);
    this.lotes.reset();
    this.selectedLote = null;
  }
  articulosFiltrados: Observable<any[]> | undefined;

  private _filter(value: any): any[] {
    //si es un string, o sea si es l primera vez
    if (typeof value === 'string') {
      const filterValue = value.toLowerCase();
      return this.articulos.filter((option: any) => {
        console.log(option, "La option");
        return option.nombre.toLowerCase().includes(filterValue);
      });
    }
    //si es un objeto, o sea si ya se selecciono una cuenta
    const filterValue = value.nombre.toLowerCase();
    return this.articulos.filter((option: any) => {
      return option.nombre.toLowerCase().includes(filterValue);
    });
  }
  displayArticulos = (articulo: any) => {
    if (articulo == null) return "";
    return articulo.nombre;
  };

  crearNota() {

    if (this.notaCompra.value.lotes.length == 0) {
      Notify.failure("Debe ingresar al menos un lote");
      return;
    }
    if (this.notaCompra.value.descripcion == null || this.notaCompra.value.descripcion == "") {
      Notify.failure("Debe ingresar una descripción");
      return;
    }
    if (this.notaCompra.value.fecha == null) {
      Notify.failure("Debe ingresar una fecha");
      return;
    }


    this.http.post(`${hostUrl}/api/notas/crear_compra`, {

      descripcion: this.notaCompra.value.descripcion,
      fecha: this.notaCompra.value.fecha,
      empresa_id: this.notaCompra.value.empresa_id,
      lotes: this.notaCompra.value.lotes.map((l: any) => {
        return {
          articulo_id: l.articulo.id,
          cantidad: l.cantidad,
          precio: l.precio,
          fecha_vencimiento: l.fecha_vencimiento
        };
      })
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (data: any) => {
        this.router.navigate(["empresa/" + this.route.parent?.snapshot.paramMap.get('id') + "/notas_compra"]);
        Notify.success("Nota creada");
        console.log(data, "Data");
      },
      error: (e) => {
        console.error(e);
        Notify.failure("Error al crear la nota" + (e.error ?? ""));
      }

    });
  }
  articulos = <any>[];
  id_empresa: number = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
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
        Notify.failure("Error al cargar los artículos" + e.error?.msg);
      }
    });
  }

  disabledIfIsInNotas = (id: number) => {
    for (const lote of this.notaCompra.value.lotes) {
      if (lote.articulo.id == id)
        return true;
    }
    return false;
  };
  cambioProgramatico = false;
  ngOnInit(): void {
    this.articulosFiltrados = this.lotes.get('articulo')?.valueChanges.pipe(
      map(value => this._filter(value || ''))
    );



    this.http.post(`${hostUrl}/api/notas/ultimo_numero`, {
      empresa_id: this.id_empresa,
      tipo: 'COMPRA'
    },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe({
        next: (data: any) => {
          this.notaCompra.patchValue({ nro_nota: data });
          console.log(data, "ultimo numero");
        }
      });
    this.lotes.valueChanges.subscribe((v) => {
      console.log(v);
      this.lotes.patchValue({
        subtotal: (v.cantidad ?? 0) * (v.precio ?? 0)
      }, { emitEvent: false });
    });

    this.fetchArticulos();
  }

}
