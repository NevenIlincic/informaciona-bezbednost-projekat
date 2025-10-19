import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { NavBar } from './navbar/nav-bar/nav-bar';
import { AuthService } from './account/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterModule, NavBar],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{
  protected readonly title = signal('frontend-app');

  constructor(private authService: AuthService, private router: Router){
    
  }
  ngOnInit(): void {
   if (!this.authService.isLoggedIn()){
    this.router.navigate(["/login"]);
   }else{
    this.router.navigate(["/home"]);
   }
  }
}
