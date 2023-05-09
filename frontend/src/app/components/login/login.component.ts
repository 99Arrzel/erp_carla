import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs';
import { hostUrl } from 'src/app/app-routing.module';
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
  errorResponse: String = "";
  /* Validate */
  error = {
    usuario: "",
    password: ""
  };
  http = inject(HttpClient);
  ngOnInit() {
    /* Check local storage for token */
    if (localStorage.getItem("token")) {
      console.log("Token found"); //Home
      console.log(localStorage.getItem("token"));
      this.router.navigate(["/"]);
    } else {
      console.log("No token found"); //Login
    }
  }
  login() {
    if (this.loginForm.value.usuario?.trim() == "") {
      this.error.usuario = "El usuario es requerido";
      return;
    } else {
      this.error.usuario = "";
    }
    if (this.loginForm.value.password?.trim() == "") {
      this.error.password = "La contraseña es requerida";
      return;
    }
    else {
      this.error.password = "";
    }
    this.http.post(`${hostUrl}/api/auth/authenticate`, this.loginForm.value)
      .subscribe({
        next: (data: any) => {
          localStorage.setItem("token", data.token);
          localStorage.setItem("usuario", this.loginForm.value.usuario as string);
          this.router.navigate(["/"]);
        },
        error: (error) => {
          console.log(error.error?.msg);
          this.errorResponse = error.error?.msg;
        }
      });
  }
}
