import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Confirm, Notify } from 'notiflix';
import { TreeNode } from 'primeng/api';
import { abrirReporte, hostUrl } from 'src/app/app-routing.module';
import { DialogCuentaComponent } from './dialog-cuenta/dialog-cuenta.component';


//COlor rojo
Confirm.init({
  titleColor: '#ff0000',
  okButtonBackground: '#ff0000',
});

@Component({
  selector: 'app-cuentas',
  templateUrl: './cuentas.component.html',
  styleUrls: ['./cuentas.component.css'],

})

export class CuentasComponent {
  files: any[] = [];
  http = inject(HttpClient);
  constructor(private router: Router, public dialog: MatDialog, private route: ActivatedRoute) { }
  nodeSelect(event: any) {
    console.log(event);
  }
  selectedNode: TreeNode | undefined;
  nodeUnselect(event: any) {
    console.log(event);
  }


  openDialogDelete() {
    Confirm.show(
      'Eliminar Cuenta',
      '¿Está seguro que desea eliminar la cuenta?',
      'Si',
      'No',
      () => {
        this.http.post<any>(`${hostUrl}/api/cuenta/eliminar`, { id: this.selectedNode?.data.id }, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        })
          .subscribe({
            next: (data) => {
              Notify.success("Cuenta eliminada correctamente");
              this.updateCuentas();
            },
            error: (e) => {
              console.error(e);

              Notify.failure("Error al eliminar la cuenta");
            }

          });
      });

  }

  updateCuentas() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    console.log(id_empresa);

    this.http.post(`${hostUrl}/api/cuenta/por_empresa`, {
      id: id_empresa
    }, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token")
      }
    }).subscribe({
      next: (result: any) => {
        console.log(result, "la data nodos");

        this.files = [{ expanded: true, label: '0', data: { nombre: 'Plan de Cuentas', codigo: '-', tipo: '-', nivel: '-' }, children: TreeMaker(result) }];
        console.log(TreeMaker(result), "la data nodos");
      }
    });
  }

  ngOnInit(): void {
    this.updateCuentas();
  }
  openDialogCrear() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    console.log(this.selectedNode, "xd");
    console.log(this.selectedNode, "epa");
    const dialogRef = this.dialog.open(DialogCuentaComponent, {
      data: {
        nodo: {
          id: null,
          padre_id: this.selectedNode?.data.id,
          nombre: this.selectedNode?.data.nombre,
        },
        empresa_id: id_empresa,
        accion: 'Crear'
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.updateCuentas();
        }
      },
    }
    );
  }
  openReportes() {
    const baseUrlReporteGestion = "jasperserver/flow.html?_flowId=viewReportFlow&_flowId=viewReportFlow&ParentFolderUri=%2FZ&reportUnit=%2FZ%2Fcuenta_report&standAlone=true";
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id');
    abrirReporte({
      baseUrlReporte: baseUrlReporteGestion,
      parameters: {
        idEmpresa: id_empresa as string,
      }
    });
  }
  openDialogEditar() {
    let id_empresa = this.route.parent?.snapshot.paramMap.get('id') as unknown as number;
    const dialogRef = this.dialog.open(DialogCuentaComponent, {
      data: {
        nodo: this.selectedNode?.data ?? null,
        empresa_id: id_empresa,
        accion: 'Editar'
      }
    });
    dialogRef.afterClosed().subscribe({
      next: (result) => {
        if (result) {
          this.updateCuentas();
        }
      },
    }
    );
  }



}

interface Data {
  label: string;
  children?: Data[] | undefined;
  key: any;
  data: any;
}


interface Value {
  id: string;
  padre: number | null;
  nombre: string;
  nivel: number;
  tipo: string;
  empresa_id: number;
  usuario_id: number;
}



/* export function TreeMaker(values: Value[], parentId: string | null = null): Data[] {
  const filteredValues = values.filter(value => value.padre === parentId);

  return filteredValues.map(value => {
    const children = TreeMaker(values, value.id);
    return {
      expanded: true,
      label: value.id,
      data: value,
      ...(children.length > 0 && { children })
    };
  });
} */

export function TreeMaker(values: Value[], parentId: string | null = null): Data[] {
  const filteredValues = values.filter(value => value.padre === parentId);
  return filteredValues.map(value => {
    const children = TreeMaker(values, value.id);
    return {
      key: value.id, // Add the required 'key' property here
      label: value.id,
      data: value,
      ...(children.length > 0 && { children })
    };
  });
}



export function TreeMakerGeneric<T extends { [key: string]: any; }>(values: T[], parentId: string | null = null, parentKey: string = 'categoria_id'): Data[] {
  const filteredValues = values.filter(value => value[parentKey] === parentId);
  return filteredValues.map(value => {
    const children = TreeMakerGeneric(values, value['id'], parentKey);
    return {
      label: value['id'],// Add the required 'label' property here
      key: value['id'],
      data: value,
      ...(children.length > 0 && { children })
    };
  });
}
