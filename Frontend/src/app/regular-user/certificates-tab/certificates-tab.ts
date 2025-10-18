import { Component, ViewEncapsulation } from '@angular/core';
import { CertificatesList } from '../certificates-list/certificates-list';

@Component({
  selector: 'app-certificates-tab',
  imports: [CertificatesList],
  templateUrl: './certificates-tab.html',
  styleUrl: './certificates-tab.css'
})
export class CertificatesTab {
}
