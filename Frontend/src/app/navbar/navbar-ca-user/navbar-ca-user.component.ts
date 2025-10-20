import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../account/auth.service';

@Component({
  selector: 'app-navbar-ca-user',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar-ca-user.component.html',
  styleUrl: './navbar-ca-user.component.css'
})
export class NavbarCaUserComponent {
  constructor(private authService: AuthService, private router: Router){
  
    }
  
    logout() {
      this.authService.logout();
      this.router.navigate(['login']);
    }

}
