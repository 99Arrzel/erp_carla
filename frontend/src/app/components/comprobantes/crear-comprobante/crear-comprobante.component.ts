import { Observable, lastValueFrom } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Moneda } from '../../home/dialog-empresa/dialog-empresa.component';
import { hostUrl } from 'src/app/app-routing.module';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Notify } from 'notiflix';
import { EmpresaMonedaModel } from '../../home/home.component';
import { distinctUntilChanged, map, startWith } from 'rxjs/operators';
import { DateAdapter } from '@angular/material/core';
type DetalleComprobante = {
  glosa: string;
  cuenta_id: number;
  debe?: number;
  haber?: number;
};

type Comprobante = {
  glosa: string;
  fecha: Date;
  tc: number;
  tipo: 'Ingreso' | 'Egreso' | 'Traspaso' | 'Apertura' | 'Ajuste';
  empresa_id: number;
  moneda_id: number;
  detalles: DetalleComprobante[];
};

@Component({
  selector: 'app-crear-comprobante',
  templateUrl: './crear-comprobante.component.html',
  styleUrls: ['./crear-comprobante.component.css']
})
export class CrearComprobanteComponent {
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  http = inject(HttpClient);

  debeDisabled = false;
  haberDisabled = false;



  disableTC = false;
  comprobante = new FormGroup({
    glosa: new FormControl('', [Validators.required]),
    fecha: new FormControl('', [Validators.required]),
    tc: new FormControl({ value: 0, disabled: this.disableTC }, [Validators.required]),
    tipo: new FormControl('', [Validators.required]),
    empresa_id: new FormControl(0, [Validators.required]),
    moneda: new FormControl({ value: null as Moneda | null, disabled: false }, [Validators.required]),
    detalles: new FormControl([] as any[], [Validators.required]),
  });
  detalle = new FormGroup({
    glosa: new FormControl('', [Validators.required]),
    cuenta: new FormControl({} as any, [Validators.required]),
    debe: new FormControl({ value: 0, disabled: false }),
    haber: new FormControl({ value: 0, disabled: false }),
  });
  crearComprobante = () => {
    /* Validar */
    if (this.comprobante.invalid) {
      Notify.failure('Debe llenar todos los campos');
      return;
    }

    if (this.comprobante.value.tc && this.comprobante.value.tc <= 0) {
      Notify.failure('El tipo de cambio debe ser mayor a 0');
      return;
    }
    if (this.comprobante.value.tc && isNaN(this.comprobante.value.tc)) {
      Notify.failure('El tipo de cambio debe ser un número');
      return;
    }
    /*
      Chequear que todos los detalles (debe y haber) sumen lo mismo

    */
    if (this.sumarDebe() !== this.sumarHaber()) {
      Notify.failure('Los montos de los detalles no cuadran');
      return;
    }
    /* chequear que haya al menos 2 detalles */
    if (this.comprobante.value.detalles && this.comprobante.value.detalles.length < 2) {
      Notify.failure('Debe haber al menos 2 detalles');
      return;
    }


    console.log(this.comprobante.value);

    this.http.post(hostUrl + '/api/comprobante/crear', {
      ...this.comprobante.value,
      empresa_id: this.id_empresa,
      moneda_id: this.comprobante.value.moneda?.id,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe(
      {
        next: (data: any) => {
          Notify.success('Comprobante creado');
          this.router.navigate(['../'], { relativeTo: this.route });
        },
        error: (error: any) => {
          console.log(error);
          if (error.error?.msg) {
            Notify.failure(error.error.msg);
          }
          Notify.failure('No se pudo crear el comprobante');
        }
      }

    );

  };
  sumarDebe = () => {
    const d = this.comprobante.value
      .detalles?.map((d: any) => d.debe ?? 0)
      .reduce((a: number, b: number) => a + b, 0);
    return d;
  };
  sumarHaber = () => {
    const d = this.comprobante.value
      .detalles?.map((d: any) => d.haber ?? 0)
      .reduce((a: number, b: number) => a + b, 0);
    return d;
  };
  agregarDetalle = () => {
    const d = this.detalle.value;
    if (this.detalle.invalid) {
      Notify.failure('Debe llenar todos los campos');
      return;
    }
    if ((d.haber ?? 0) > 0 && (d.debe ?? 0) > 0) {
      Notify.failure('Debe llenar solo uno de los campos');
      return;
    }
    if ((d.haber ?? 0) == 0 && (d.debe ?? 0) == 0) {
      Notify.failure('Debe llenar al menos un campo debe o haber mayor a 0');
      return;
    }

    const detalles = this.comprobante.value.detalles ?? [];
    detalles.push({
      ...d,
      cuenta_id: d.cuenta?.id,
    });
    this.comprobante.patchValue({ detalles: detalles });
    this.detalle.reset({ glosa: this.detalle.value.glosa ?? '', cuenta: null, debe: 0, haber: 0 });
    this.seInteractuoEnGlosa = false;


  };
  serie: string = '31231';
  cuentas: any[] = [];
  monedas: Moneda[] = [];
  getMonedas = async () => {

    const response = this.http.get<Moneda[]>(`${hostUrl}/api/moneda/usuario`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    });
    return await lastValueFrom(response);
  };
  updateMonedas = () => {

    this.getMonedas().then((m) => {
      this.monedas = m;
    });
  };
  tipos = ["Ingreso", "Egreso", "Traspaso", "Apertura", "Ajuste"];

  id_empresa = 0;
  seInteractuoEnGlosa = false;
  cambioProgramatico = false;
  cambiosMonedas: any[] = [];
  min_tc = () => {
    if (this.comprobante.value.moneda?.id == this.moneda_principal_id) {
      return Math.floor(this.tc_ultimo);
    }
    return Math.floor(1 / this.tc_ultimo);
  };
  max_tc = () => {
    if (this.comprobante.value.moneda?.id == this.moneda_principal_id) {
      return Math.floor(this.tc_ultimo) + 1;
    }
    return Math.floor(1 / this.tc_ultimo) + 1;
  };
  isInTable = (id_cuenta: any) => {
    //this comprobantes detalles
    const detalles = this.comprobante.value.detalles ?? [];
    const detalle = detalles.find((d: any) => d.cuenta_id == id_cuenta);
    if (detalle) {

      return true;
    }
    return false;
  };

  monedasActivas = () => {
    //filtramos las monedas que sean moneda_alternativa_id y moneda_principal_id
    const monedas = this.monedas.filter((m) => m.id == this.moneda_alternativa_id || m.id == this.moneda_principal_id);
    return monedas;
  };

  cuentasFiltradas: Observable<any[]> | undefined;
  private _filter(value: any): any[] {
    //si es un string, o sea si es l primera vez
    if (typeof value === 'string') {
      const filterValue = value.toLowerCase();
      return this.cuentas.filter(option => {
        console.log(option, "La option");
        return option.nombre.toLowerCase().includes(filterValue);
      });
    }
    //si es un objeto, o sea si ya se selecciono una cuenta
    const filterValue = value.nombre.toLowerCase();
    return this.cuentas.filter(option => {
      return option.nombre.toLowerCase().includes(filterValue);
    });
  }
  displayCuentas = (cuenta: any) => {
    if (cuenta) {
      return cuenta.nombre;
    }
    return '';
  };

  rojitoSeleccionado = () => {
    if (this.selectedDetalle) {
      return "bg-red-500";
    }
    return "bg-gray-600";
  };

  selectedDetalle: any = null;
  eliminarDetalle = () => {
    //filtramos detlales, el selecteDetalle
    const detalles = this.comprobante.value.detalles ?? [];
    const detallesFiltrados = detalles.filter((d: any) => d.cuenta_id != this.selectedDetalle.cuenta_id);
    this.comprobante.patchValue({ detalles: detallesFiltrados });
  };
  moneda_alternativa_id = 0;
  moneda_principal_id = 0;
  tc_ultimo = 0;
  fetchUltimoTC = () => {
    this.http.post(`${hostUrl}/api/moneda_empresa/ultimo_cambio`, {
      empresa_id: this.id_empresa,
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe(
      {
        next: (data: any) => {
          console.log(data, "Ultimo TC");
          this.moneda_alternativa_id = data.moneda_alternativa_id;
          this.moneda_principal_id = data.moneda_principal_id;
          this.tc_ultimo = data.cambio;
        }
      }
    );


  };
  amarilloSeleccionado = () => {
    if (this.selectedDetalle) {
      return "bg-yellow-500";
    }
    return "bg-gray-600";
  };
  ultima_moneda_seleccionada: any = null;
  editarDetalle = () => {

    //Se pone el detalle en el form detalle
    this.detalle.patchValue({
      glosa: this.selectedDetalle.glosa,
      cuenta: this.cuentas.find((c) => c.id == this.selectedDetalle.cuenta_id),
      debe: this.selectedDetalle.debe,
      haber: this.selectedDetalle.haber,
    });
    //Se elimina el detalle de la tabla
    this.eliminarDetalle();
    //Deselecciona
    this.selectedDetalle = null;
  };
  ngOnInit() {
    this.route.parent?.paramMap.subscribe(params => {
      this.id_empresa = Number(params.get("id"));
    });
    this.fetchUltimoTC();
    this.cuentasFiltradas = this.detalle.get('cuenta')?.valueChanges.pipe(
      map(value => this._filter(value || ''))
    );
    this.comprobante.valueChanges
      .subscribe((v) => {

        //Verificar cambio de moneda si cambio o no
        if (v.moneda?.id != this.ultima_moneda_seleccionada?.id) {
          console.log("Cambio de moneda");
          if (v.moneda?.id == this.moneda_principal_id) {
            //Si es la principal, aplicamos el ultimo tc, sino, aplicamos la inversa 1/tc
            this.cambioProgramatico = true;
            this.comprobante.patchValue({ tc: this.tc_ultimo }, { emitEvent: false });
            console.log("Patched", this.tc_ultimo);
            this.cambioProgramatico = false;
          } else {
            this.cambioProgramatico = true;
            this.comprobante.patchValue({ tc: Number((1 / this.tc_ultimo).toFixed(2)) }, { emitEvent: false });
            console.log("Patched", (1 / this.tc_ultimo));
            this.cambioProgramatico = false;
          }
          this.ultima_moneda_seleccionada = v.moneda;
        }
        //verificar que (v.tc sea mayor o menor que el min_tc o max_tc)
        if (v.tc != null) {
          if (v.tc < this.min_tc() || v.tc > this.max_tc()) {
            console.log(this.min_tc(), this.max_tc());
            this.cambioProgramatico = true;
            this.comprobante.patchValue({ tc: this.min_tc() }, { emitEvent: false });
            this.cambioProgramatico = false;
          }
        } else {
          this.cambioProgramatico = true;
          this.comprobante.patchValue({ tc: this.min_tc() }, { emitEvent: false });
          this.cambioProgramatico = false;
        }
        if (this.comprobante.value.detalles?.length == 0) {
          if (!this.seInteractuoEnGlosa) {
            this.detalle.patchValue({ glosa: this.comprobante.value.glosa });
          }
          this.disableTC = false;
        } else {
          console.log("no se puede, disabled tc");
          this.disableTC = true;
        }
        if (isNaN(v.tc as any)) {
          this.comprobante.patchValue({ tc: this.tc_ultimo });
        }

      });
    this.detalle.valueChanges.subscribe((v) => {
      if (v.glosa != this.comprobante.value.glosa) {
        this.seInteractuoEnGlosa = true;
      }
      if (isNaN(Number(v.debe))) {
        this.detalle.patchValue({ debe: 0 });
      }
      if (isNaN(Number(v.haber))) {
        this.detalle.patchValue({ haber: 0 });
      }
      if (Number(v.debe) && !this.cambioProgramatico) {
        // Actualiza el valor de `haber` programáticamente
        this.cambioProgramatico = true;
        this.detalle.patchValue({ haber: 0 });
        this.cambioProgramatico = false;
      }
      // Verifica si el cambio en `haber` es programático o no
      if (Number(v.haber) && !this.cambioProgramatico) {
        // Actualiza el valor de `debe` programáticamente
        this.cambioProgramatico = true;
        this.detalle.patchValue({ debe: 0 });
        this.cambioProgramatico = false;
      }
    });


    this.updateMonedas();
    //Search token in local storage
    if (!localStorage.getItem("token")) {
      console.log("No token found");
      this.router.navigate(["/login"]);
      return;
    }

    //Http request get cuentas
    this.http.get(`${hostUrl}/api/cuenta/solo_detalle/${this.id_empresa}`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data: any) => {
      console.log(data, "Cuentas");
      this.cuentas = data;
    });
    this.http.post<EmpresaMonedaModel[]>(`${hostUrl}/api/moneda_empresa/por_empresa`, {
      id: this.id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data: EmpresaMonedaModel[]) => {
      console.log("Monedas todos cambios: ", data);
      if (data.length <= 1) {
        alert("No se puede crear comprobante, debe tener al menos 2 monedas");
        return;
      } else {
        //Agarrar el ultimo valor del array
        const ultimaMoneda = data[data.length - 1];
        this.comprobante.patchValue({ moneda: this.monedas.find((m) => m.id == ultimaMoneda.moneda_alternativa_id) });
      }

    });
    /* Fetch ultimo numero de serie  para comprobante*/
    this.http.get(`${hostUrl}/api/comprobante/ultimo_numero/${this.id_empresa}`, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe((data: any) => {
      console.log(data, "Numero serie");
      if (data) {
        this.serie = String(Number(data) + 1);
      } else {
        console.log("No hay numero de serie");
        this.serie = "1";
      }
    }
    );
    this.comprobante.patchValue({ empresa_id: this.id_empresa });
  }
};
