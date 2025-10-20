import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-activation-success',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activation-success.component.html',
  styleUrls: ['./activation-success.component.css']
})
export class ActivationSuccessComponent implements OnInit{ 

  constructor(private authService: AuthService){}  

  loggedIn: boolean = false;
  ngOnInit(): void {
    if (this.authService.isLoggedIn()){
      this.loggedIn = true;
    }
    this.authService.logout();
  }
}
