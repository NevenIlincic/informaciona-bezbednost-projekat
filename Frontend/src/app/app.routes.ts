import { Routes } from '@angular/router';
import { LoginComponent } from './account/login/login.component';
import { RegistrationComponent } from './account/registration/registration.component';
import { ActivationSuccessComponent } from './account/activation-success/activation-success.component';
import { ActivationFailureComponent } from './account/activation-failure/activation-failure.component';
import { HomeComponent } from './home/home/home.component';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'register', component: RegistrationComponent },
  { path: 'home', component: HomeComponent },
  { path: 'activation-success', component: ActivationSuccessComponent },
  { path: 'activation-failure', component: ActivationFailureComponent },
];

