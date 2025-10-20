import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CertificateService } from './certificate-service';
import { EECertificateDTO } from '../../dto/certificate/EECertificateDTO';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';
import { Pcks12DTO } from '../../dto/certificate/Pcks12DTO';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private formBuilder: FormBuilder, private certificateService: CertificateService, private snackBar: MatSnackBar) {
    this.loginForm = formBuilder.group({
      subjectCommonName: ['', Validators.required],
      subjectEmail: ['', Validators.required],
      subjectCountry: ['', Validators.required],
      subjectOrganizationName: ['', Validators.required],
      subjectOrganizationalUnit: ['', Validators.required],
      certificatePassword: ['', Validators.required],
      certificateValidFrom: ['', Validators.required],
      certificateValidTo: ['', Validators.required],
      foundCACertificates: ['', Validators.required],
      keyUsageDigitalSignature: [false],
      keyUsageKeyEncipherment: [false],
      eKeyUsageServerAuth: [false],
      eKeyUsageClientAuth: [false]
    });
  }

  ngOnInit(): void {
    this.certificateService.getNonRevokedCACertificates().subscribe({
      next: (certificateList: NonRevokedCACertificateDTO[]) => {
        this.nonRevokedCACertificates = certificateList;
      }
    });
  }


  onSubmit() {
    if (this.loginForm.invalid) { return; }
    this.isSubmitting = true;
    this.selectedCACertificate = this.loginForm.get("foundCACertificates")?.value;

    const eeCertificateDTO: EECertificateDTO = {
      issuerCertificateId: this.selectedCACertificate!.id,
      passwordForCertificate: this.loginForm.get('certificatePassword')?.value,
      subjectCommonName: this.loginForm.get('subjectCommonName')?.value,
      subjectCountry: this.loginForm.get('subjectCountry')?.value,
      subjectEmail: this.loginForm.get('subjectEmail')?.value,
      subjectOrganizationalUnit: this.loginForm.get('subjectOrganizationalUnit')?.value,
      subjectOrganizationName: this.loginForm.get('subjectOrganizationName')?.value,
      validFrom: this.loginForm.get('certificateValidFrom')?.value,
      validTo: this.loginForm.get('certificateValidTo')?.value,
      isDigitalSignature: this.loginForm.get('keyUsageDigitalSignature')?.value,
      isKeyEncipherment: this.loginForm.get('keyUsageKeyEncipherment')?.value,
      isServerAuth: this.loginForm.get("eKeyUsageServerAuth")?.value,
      isClientAuth: this.loginForm.get("eKeyUsageClientAuth")?.value
    }

    this.certificateService.createEECertificateRegularUser(eeCertificateDTO, 'false').subscribe({
      next: (pcksDTO: Pcks12DTO) => {
        this.certificateService.downloadCertificate(pcksDTO);
        this.isSubmitting = false;
      },
      error: (err: HttpErrorResponse) => {
        if (err.status == 400) {
          const errorDTO: Pcks12DTO = err.error;
          if (errorDTO.fileName == "Invalid") {
            this.isSubmitting = false;
            this.snackBar.open(errorDTO.encodedPcks12, 'I Understand', {
              duration: undefined,
              verticalPosition: 'bottom',
              panelClass: ["snack-bar-refresh-token-error"]
            });
          }
        }
      }
    });


  }
}
