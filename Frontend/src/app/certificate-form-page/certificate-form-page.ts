import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CertificateService } from './certificate-service';
import { EECertificateDTO } from '../../dto/certificate/EECertificateDTO';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';

@Component({
  selector: 'app-certificate-form-page',
  imports: [ReactiveFormsModule, CommonModule, FormsModule],
  templateUrl: './certificate-form-page.html',
  styleUrl: './certificate-form-page.css'
})
export class CertificateFormPage implements OnInit {
  loginForm: FormGroup;
  nonRevokedCACertificates: NonRevokedCACertificateDTO[] = [];
  selectedCACertificate: NonRevokedCACertificateDTO | null = null;
  isSubmitting = false;
 
  constructor(private formBuilder: FormBuilder, private certificateService: CertificateService){
    this.loginForm = formBuilder.group({
      subjectCommonName: ['', Validators.required],
      subjectEmail: ['', Validators.required],
      subjectCountry: ['', Validators.required],
      subjectOrganizationName: ['', Validators.required],
      subjectOrganizationalUnit: ['', Validators.required],
      certificatePassword: ['', Validators.required],
      certificateValidFrom: ['', Validators.required],
      certificateValidTo: ['', Validators.required],
      foundCACertificates: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.certificateService.getNonRevokedCACertificates().subscribe({
      next: (certificateList: NonRevokedCACertificateDTO[]) => {
        this.nonRevokedCACertificates = certificateList;
        console.log(this.nonRevokedCACertificates);
      }
    });
  }


  onSubmit(){
    if (this.loginForm.invalid) {return;}
    this.isSubmitting = true;

    const eeCertificateDTO: EECertificateDTO = {
      issuerCertificateId: 30,
      passwordForCertificate: this.loginForm.get('certificatePassword')?.value,
      subjectCommonName: this.loginForm.get('subjectCommonName')?.value,
      subjectCountry: this.loginForm.get('subjectCountry')?.value,
      subjectEmail: this.loginForm.get('subjectEmail')?.value,
      subjectOrganizationalUnit: this.loginForm.get('subjectOrganizationalUnit')?.value,
      subjectOrganizationName: this.loginForm.get('subjectOrganizationName')?.value,
      validFrom: this.loginForm.get('certificateValidFrom')?.value,
      validTo: this.loginForm.get('certificateValidTo')?.value,
    }

  }
}
