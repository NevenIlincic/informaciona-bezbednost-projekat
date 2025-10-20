import { Component } from '@angular/core';
import { CertificateTabDTO } from '../../../dto/certificate/CertificateTabDTO';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { AuthService } from '../../account/auth.service';
import { CertificateListCa } from '../certificate-list-ca/certificate-list-ca';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-certificate-tab-ca',
  imports: [CertificateListCa, CommonModule],
  templateUrl: './certificate-tab-ca.html',
  styleUrl: './certificate-tab-ca.css'
})
export class CertificateTabCa {

  allCertificates: CertificateTabDTO[] = [];
  constructor(private certificateService: CertificateService, private authService: AuthService) { }

  ngOnInit(): void {
    const email: string = this.authService.getEmail();
    this.certificateService.getAllCertificatesInChain(email).subscribe({
      next: (foundCertificates: CertificateTabDTO[]) => {
        this.allCertificates = foundCertificates;
      }
    });
  }
}
