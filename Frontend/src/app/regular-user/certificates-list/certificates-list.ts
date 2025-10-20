import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { CertificateUser } from '../certificate-user/certificate-user';
import { FormsModule } from '@angular/forms';
import { RegularUserCertificateDTO } from '../../../dto/certificate/RegularUserCertificateDTO';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-certificates-list',
  standalone: true,
  imports: [CertificateUser, CommonModule ],
  templateUrl: './certificates-list.html',
  styleUrl: './certificates-list.css'
})
export class CertificatesList{

  @Input() foundCertificates: RegularUserCertificateDTO[] = [];

}
