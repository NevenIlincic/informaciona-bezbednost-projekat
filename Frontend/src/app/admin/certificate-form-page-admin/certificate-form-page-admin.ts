import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NonRevokedCACertificateDTO } from '../../../dto/certificate/NonRevokedCACertificateDTO';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { EECertificateDTO } from '../../../dto/certificate/EECertificateDTO';
import { Pcks12DTO } from '../../../dto/certificate/Pcks12DTO';

@Component({
  selector: 'app-certificate-form-page-admin',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './certificate-form-page-admin.html',
  styleUrl: './certificate-form-page-admin.css'
})
export class CertificateFormPageAdmin {
  certificateForm: FormGroup;
  nonRevokedCACertificates: NonRevokedCACertificateDTO[] = [];
  selectedCACertificate: NonRevokedCACertificateDTO | null = null;
  isSubmitting = false;

  constructor(private formBuilder: FormBuilder, private certificateService: CertificateService, private snackBar: MatSnackBar) {
    this.certificateForm = formBuilder.group({
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
    if (this.certificateForm.invalid) { return; }
    this.isSubmitting = true;
    this.selectedCACertificate = this.certificateForm.get("foundCACertificates")?.value;

    // Dodati if isAdminCreating
    const eeCertificateDTO: EECertificateDTO = {
      issuerCertificateId: this.selectedCACertificate!.id,
      passwordForCertificate: this.certificateForm.get('certificatePassword')?.value,
      subjectCommonName: this.certificateForm.get('subjectCommonName')?.value,
      subjectCountry: this.certificateForm.get('subjectCountry')?.value,
      subjectEmail: this.certificateForm.get('subjectEmail')?.value,
      subjectOrganizationalUnit: this.certificateForm.get('subjectOrganizationalUnit')?.value,
      subjectOrganizationName: this.certificateForm.get('subjectOrganizationName')?.value,
      validFrom: this.certificateForm.get('certificateValidFrom')?.value,
      validTo: this.certificateForm.get('certificateValidTo')?.value,
      isDigitalSignature: this.certificateForm.get('keyUsageDigitalSignature')?.value,
      isKeyEncipherment: this.certificateForm.get('keyUsageKeyEncipherment')?.value,
      isServerAuth: this.certificateForm.get("eKeyUsageServerAuth")?.value,
      isClientAuth: this.certificateForm.get("eKeyUsageClientAuth")?.value
    }

    this.certificateService.createEECertificateRegularUser(eeCertificateDTO).subscribe({
      next: () => {

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
