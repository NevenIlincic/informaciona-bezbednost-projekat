import { Component, Input } from '@angular/core';
import { CertificateTabDTO } from '../../../dto/certificate/CertificateTabDTO';
import { CertificateAdmin } from '../certificate-admin/certificate-admin';
import { CommonModule } from '@angular/common';
import { ɵEmptyOutletComponent } from "@angular/router";

@Component({
  selector: 'app-certificate-list-admin',
  imports: [CertificateAdmin, CommonModule],
  templateUrl: './certificate-list-admin.html',
  styleUrl: './certificate-list-admin.css'
})
export class CertificateListAdmin {
  @Input() certificateList: CertificateTabDTO[] = []
}
