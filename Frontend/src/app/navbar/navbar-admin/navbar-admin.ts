import { Component } from '@angular/core';
import { AuthService } from '../../account/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-navbar-admin',
  imports: [RouterLink],
  templateUrl: './navbar-admin.html',
  styleUrl: './navbar-admin.css'
})
export class NavbarAdmin {

   constructor(private authService: AuthService, private router: Router){

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
