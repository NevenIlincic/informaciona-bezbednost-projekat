import { Component, Input } from '@angular/core';
import { CertificateTabDTO } from '../../../dto/certificate/CertificateTabDTO';
import { CertificateCa } from '../certificate-ca/certificate-ca';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-certificate-list-ca',
  imports: [CertificateCa, CommonModule],
  templateUrl: './certificate-list-ca.html',
  styleUrl: './certificate-list-ca.css'
})
export class CertificateListCa {
  @Input() certificateList: CertificateTabDTO[] = []

}
