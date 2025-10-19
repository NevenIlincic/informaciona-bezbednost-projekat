import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { RegularUserCertificateDTO } from '../../../dto/certificate/RegularUserCertificateDTO';
import { CommonModule, NgForOf } from '@angular/common';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { RevocationDTO } from '../../../dto/certificate/RevocationDTO';

@Component({
  selector: 'app-certificate-user',
  imports: [MatCardModule, CommonModule],
  templateUrl: './certificate-user.html',
  styleUrl: './certificate-user.css'
})
export class CertificateUser implements OnInit {
  
  constructor(private certificateService: CertificateService){}

  @Input() userCertificate: RegularUserCertificateDTO = {} as RegularUserCertificateDTO;

  keyConstraints: string[] = [];
  validFrom: Date = new Date();
  validTo: Date = new Date();
  revocationDate: Date = new Date();


  ngOnInit(): void {
    this.validFrom = new Date(this.userCertificate.validFrom);
    this.validTo = new Date(this.userCertificate.validTo);

    if (this.userCertificate.revoked){
      this.revocationDate = new Date(this.userCertificate.revocationDate)
    }

    this.keyConstraints = [this.userCertificate.digitalSignature, this.userCertificate.keyEncipherment];
    this.keyConstraints = this.keyConstraints .filter(item => item !== '');
    // if (filteredString == ""){
    //   this.keyConstraints = [];
    // }else{
    // }
  }

  revokeCertificate(certificateId: number){
    const revocation: RevocationDTO = {
      id: certificateId,
      revocationReason: "PrimaryKeyLoss"
    }
    this.certificateService.revokeCertificate(revocation).subscribe({
      next: () => {
        this.userCertificate.revoked = true;
        this.userCertificate.revocationReason = "PrimaryKeyLoss";
        this.revocationDate = new Date();
      }
    });
  }
}
