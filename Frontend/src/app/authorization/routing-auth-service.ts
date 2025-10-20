import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../account/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoutingAuthService implements CanActivate {

  constructor(private authService: AuthService, private router: Router){

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const userRole: string = this.authService.user$.getValue();
    if (userRole == null || userRole == ""){
      this.router.navigate(["/login"]);
      return false;
    }
    if (!route.data['role'].includes(userRole)){
      this.router.navigate(["/home"]);
      return false;
    }
    return true;
  
  }
  
}
