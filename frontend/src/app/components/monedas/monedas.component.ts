import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { EmpresaMonedaModel } from '../home/home.component';
import { hostUrl } from 'src/app/app-routing.module';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Moneda } from '../home/dialog-empresa/dialog-empresa.component';
import { Notify } from 'notiflix';

@Component({
  selector: 'app-monedas',
  templateUrl: './monedas.component.html',
  styleUrls: ['./monedas.component.css']
})
export class MonedasComponent {

  monedaForm = new FormGroup({
    monedaPrincipal: new FormControl<Moneda | null>({ value: null, disabled: true }, [Validators.required]),
    monedaAlternativa: new FormControl<Moneda | null>(null, [Validators.required]),
    cambio: new FormControl<Number>(0.01, [Validators.required]),
  });
  monedas: Moneda[] = [];
  http = inject(HttpClient);
  moneda_alternativa: any | null = null;

  /* Si es que ya hay más de 1 registro de historial de cambios (cambios) settear la moneda alternative como únicamente lo último*/
  setMonedaAlternativa() {
    if (this.cambios.length > 0) {
      const ultimo = this.cambios[0];


    }
  }

  fetchEmpresaMonedas(id: number) {
    this.http.post<EmpresaMonedaModel[]>(`${hostUrl}/api/moneda_empresa/por_empresa`, {
      id: id
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data: EmpresaMonedaModel[]) => {
      //Agarrar el ultimo del array
      this.fetchMonedas();

      if (this.monedaForm.get('monedaPrincipal')?.value == null) {

        const nMon: Moneda = {
          id: data[0].moneda_principal_id,
          nombre: data[0].nombre_moneda_principal as string,
          descripcion: "Default",
          abreviatura: "Default"
        };

        this.monedaForm.get('monedaPrincipal')?.setValue(nMon);
        /* Logeamos que carajos quedo */
      }
      console.log(data, "Data");
      /* Si data es mayor a 1 */



      console.log(this.monedaForm.get('monedaPrincipal')?.value, "Moneda principal");
      const ultimo = data[data.length - 1];
      console.log("Ultimo cambio", ultimo, "Datos");
      this.moneda_alternativa = ultimo.moneda_alternativa_id;
      this.moneda_principal = ultimo.moneda_principal_id;
      this.monedaForm.patchValue({
        cambio: ultimo.cambio
      });
      console.log(this.monedas, "Las monedas dispo x2");
      this.cambios = data.filter((cambio) => {
        return cambio.cambio != null;
      }).reverse();
    });
  }

  moneda_principal: any | null = null;
  fetchMonedas() {
    this.http.get<Moneda[]>(`${hostUrl}/api/moneda/usuario`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data) => {
      console.log("Data: ", data);
      this.monedas = data.filter((moneda) => {
        return moneda.id != this.monedaForm.get('monedaPrincipal')?.value?.id;
      });

      if (this.moneda_alternativa != null) {
        this.monedas = this.monedas.filter((moneda) => {
          return moneda.id == this.moneda_alternativa;
        });
      }
      /* Además, si existe ya un tipo de cambio, el alternativo se vuelve solo 1 */
      console.log(data.filter((moneda) => {
        return moneda.id != this.monedaForm.get('monedaPrincipal')?.value?.id;
      }), "Las monedas dispo");


      const ultimo = this.monedas.find((moneda) => { return moneda.id == this.moneda_alternativa; });
      this.monedaForm.get('monedaAlternativa')?.setValue(ultimo as Moneda || null);

    });
  }

  ngOnInit(): void {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    this.fetchEmpresaMonedas(id_empresa);

  }

  crear() {
    /* Checkear que no es igual al último, en el cambio, no en la moneda*/
    /* Buscamos el activo */
    const activo = this.cambios.find((cambio) => {
      return cambio.estado;
    });
    console.log(activo);
    /* Checkear que no es el mismo camibo que el último */

    if (activo?.cambio == this.monedaForm.get('cambio')?.value && activo?.moneda_alternativa_id == this.monedaForm.get('monedaAlternativa')?.value?.id) {
      Notify.failure("El cambio no puede ser el mismo que el último");
      return;
    }



    if (this.monedaForm.valid) {
      this.http.post(`${hostUrl}/api/moneda_empresa/insertar`, {
        moneda_principal_id: this.monedaForm.get('monedaPrincipal')?.value?.id,
        moneda_alternativa_id: this.monedaForm.get('monedaAlternativa')?.value?.id,
        cambio: this.monedaForm.get('cambio')?.value,
        empresa_id: this.route.parent?.snapshot.paramMap.get('id') as unknown as number
      }, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).subscribe(
        {
          error: (err) => {
            console.log("Error: ", err.error?.msg);
            Notify.failure("Error al agregar la moneda: " + err.error?.msg);
          },
          next: (data) => {
            Notify.success("Moneda agregada correctamente");
            this.fetchEmpresaMonedas(this.route.parent?.snapshot.paramMap.get('id') as unknown as number);
          }

        }

      );

    } else {
      if (this.monedaForm.get('monedaAlternativa')?.value == null) {
        Notify.failure("Seleccione una moneda alternativa");
      }

    }
  }
  constructor(private router: Router, private route: ActivatedRoute) { }
  cambios: EmpresaMonedaModel[] = [];
}

