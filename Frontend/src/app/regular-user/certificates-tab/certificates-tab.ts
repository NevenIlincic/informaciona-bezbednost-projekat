import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { CertificatesList } from '../certificates-list/certificates-list';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { RegularUserCertificateDTO } from '../../../dto/certificate/RegularUserCertificateDTO';
import { AuthService } from '../../account/auth.service';
import { CommonModule, NgIf } from '@angular/common';

@Component({
  selector: 'app-certificates-tab',
  imports: [CertificatesList, CommonModule],
  templateUrl: './certificates-tab.html',
  styleUrl: './certificates-tab.css'
})
export class CertificatesTab implements OnInit {

  userCertificates: RegularUserCertificateDTO[] = [];
  constructor(private certificateService: CertificateService, private authService: AuthService){}

  ngOnInit(): void {
    const email: string = this.authService.getEmail();
    this.certificateService.getRegularUserCertificates(email).subscribe({
      next: (foundCertificates: RegularUserCertificateDTO[]) => {
        this.userCertificates = foundCertificates;
      }
    });
  }

}
