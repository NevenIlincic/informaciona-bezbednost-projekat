import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../account/auth.service';

@Component({
  selector: 'app-navbar-regular-user',
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar-regular-user.html',
  styleUrl: './navbar-regular-user.css'
})
export class NavbarRegularUser {

  constructor(private authService: AuthService, private router: Router){

  }

  logout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }
}
