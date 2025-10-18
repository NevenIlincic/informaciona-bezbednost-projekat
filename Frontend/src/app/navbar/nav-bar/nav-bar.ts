import { Component, OnInit } from '@angular/core';
import { NavbarRegularUser } from '../navbar-regular-user/navbar-regular-user';
import { AuthService } from '../../account/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [NavbarRegularUser, CommonModule],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css'
})
export class NavBar implements OnInit {

  role: string = "";

  constructor(private authService: AuthService){
  }
  ngOnInit(): void {
     this.authService.userState.subscribe((result) =>{
      this.role = result;
    })
  }
}
