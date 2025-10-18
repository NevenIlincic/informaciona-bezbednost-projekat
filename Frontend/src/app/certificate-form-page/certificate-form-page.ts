import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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
      keyUsageKeyEncipherment: [false]
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
    console.log(this.loginForm.get('certificateValidFrom')?.value);
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
      isKeyEncipherment: this.loginForm.get('keyUsageKeyEncipherment')?.value

    }

    this.certificateService.createEECertificateRegularUser(eeCertificateDTO).subscribe({
      next: (pcksDTO: Pcks12DTO) => {
        // Decode Base64 u binarni array
        const binary = atob(pcksDTO.encodedPcks12);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
          bytes[i] = binary.charCodeAt(i);
        }

        // Kreiramo Blob i URL za download
        const blob = new Blob([bytes], { type: 'application/x-pkcs12' });
        const url = window.URL.createObjectURL(blob);

        // Automatski download
        const a = document.createElement('a');
        a.href = url;
        a.download = pcksDTO.fileName;
        a.click();

        window.URL.revokeObjectURL(url);
        this.isSubmitting = false;

      },
      error: (err: HttpErrorResponse) => {
        if (err.status == 400) {
          const errorDTO: Pcks12DTO = err.error;
          if (errorDTO.encodedPcks12 == "Invalid") {
            this.isSubmitting = false;
            this.snackBar.open('Issuer certificate is invalid!', 'I Understand', {
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
