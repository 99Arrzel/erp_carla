import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  constructor(private router: Router) { }
  loginForm = new FormGroup({
    usuario: new FormControl(''),
    password: new FormControl(''),
  });
  /* Validate */

  error = {
    usuario: "",
    password: ""
  };
  http = inject(HttpClient);

  ngOnInit() {

    /* Check local storage for token */
    if (localStorage.getItem("token")) {

      this.router.navigate(["/"]);
    } else {
      console.log("No token found"); //Login
    }
  }
  login() {
    if (this.loginForm.value.usuario?.trim() == "") {
      this.error.usuario = "El usuario es requerido";
    } else {
      this.error.usuario = "";
    }
    if (this.loginForm.value.password?.trim() == "") {
      this.error.password = "La contraseña es requerida";
    }
    else {
      this.error.password = "";
    }

    this.http.post("http://localhost:8085/api/auth/authenticate", this.loginForm.value)
      .subscribe(
        (data: any) => {
          localStorage.setItem("token", data.token);
          this.router.navigate(["/"]);
        },
        (error) => {
          console.log(error);
          error.status == 403 ? this.error.password = "Sin coincidencia" : this.error.password = "Error de conexión";
        }
      );

  }

}
