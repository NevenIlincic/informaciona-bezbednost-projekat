import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../account/auth.service';
import { ɵEmptyOutletComponent } from "@angular/router";
import { CertificatesTab } from "../../regular-user/certificates-tab/certificates-tab";
import { CertificateTabAdmin } from "../../admin/certificate-tab-admin/certificate-tab-admin";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, CertificatesTab, CertificateTabAdmin],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit { 

  constructor(private authService: AuthService){}

  role:string = "";

  ngOnInit(): void {
    this.role = this.authService.getRole();
  }

}
