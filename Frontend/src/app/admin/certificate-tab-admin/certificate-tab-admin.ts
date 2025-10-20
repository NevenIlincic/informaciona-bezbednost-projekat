import { Component } from '@angular/core';
import { CertificateTabDTO } from '../../../dto/certificate/CertificateTabDTO';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { AuthService } from '../../account/auth.service';
import { CertificateListAdmin } from '../certificate-list-admin/certificate-list-admin';
import { CommonModule, NgIf } from '@angular/common';

@Component({
  selector: 'app-certificate-tab-admin',
  imports: [CertificateListAdmin, CommonModule],
  templateUrl: './certificate-tab-admin.html',
  styleUrl: './certificate-tab-admin.css'
})
export class CertificateTabAdmin {
  allCertificates: CertificateTabDTO[] = [];
  constructor(private certificateService: CertificateService, private authService: AuthService) { }

  ngOnInit(): void {
    const email: string = this.authService.getEmail();
    this.certificateService.getAllCertificatesAdmin().subscribe({
      next: (foundCertificates: CertificateTabDTO[]) => {
        this.allCertificates = foundCertificates;
      }
    });
  }

}
