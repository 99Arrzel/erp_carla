import { Component } from '@angular/core';
import { Router } from "@angular/router";
@Component({
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private router: Router) { }

  ngOnInit() {
    //Search token in local storage
    if (!localStorage.getItem("token")) {
      console.log("No token found");
      this.router.navigate(["/login"]);
    }
  }

}
