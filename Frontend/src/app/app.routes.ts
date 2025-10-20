import { Routes } from '@angular/router';
import { LoginComponent } from './account/login/login.component';
import { RegistrationComponent } from './account/registration/registration.component';
import { ActivationSuccessComponent } from './account/activation-success/activation-success.component';
import { ActivationFailureComponent } from './account/activation-failure/activation-failure.component';
import { HomeComponent } from './home/home/home.component';
import { CertificateFormPage } from './certificate-form-page/certificate-form-page';
import { CertificateUser } from './regular-user/certificate-user/certificate-user';
import { RoutingAuthService } from './authorization/routing-auth-service';
import { CertificatesTab } from './regular-user/certificates-tab/certificates-tab';
import { CreateTemplateComponent } from './template/create-template/create-template.component';
import { CertificateFormPageAdmin } from './admin/certificate-form-page-admin/certificate-form-page-admin';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'register', component: RegistrationComponent },
  { path: 'home', component: HomeComponent },
  { path: 'activation-success', component: ActivationSuccessComponent },
  { path: 'activation-failure', component: ActivationFailureComponent},
  { path: 'certificate-form-page', component: CertificateFormPage, canActivate: [RoutingAuthService], data: {role: ["REGULAR_USER"]}},
  { path: 'certificate-form-page-admin', component: CertificateFormPageAdmin, canActivate: [RoutingAuthService], data: {role: ["ADMIN"]}}
  { path: 'create-template', component: CreateTemplateComponent, canActivate: [RoutingAuthService], data: {role: "CA_USER"} }
];

