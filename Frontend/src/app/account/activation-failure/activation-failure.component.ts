import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-activation-failure',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activation-failure.component.html',
  styleUrls: ['./activation-failure.component.css']
})
export class ActivationFailureComponent implements OnInit {

  constructor(private authService: AuthService) {
  }
  loggedIn: boolean = false;
  
  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loggedIn = true;
    }
    this.authService.logout();
  }

}
