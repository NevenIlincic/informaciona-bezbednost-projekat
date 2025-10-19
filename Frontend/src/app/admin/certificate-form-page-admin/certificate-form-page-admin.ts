import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NonRevokedCACertificateDTO } from '../../../dto/certificate/NonRevokedCACertificateDTO';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { EECertificateDTO } from '../../../dto/certificate/EECertificateDTO';
import { Pcks12DTO } from '../../../dto/certificate/Pcks12DTO';
import { NonEECertificateDTO } from '../../../dto/certificate/NonEECertificateDTO';
import { ɵEmptyOutletComponent } from "@angular/router";
import { IntermediateCertificateDTO } from '../../../dto/certificate/IntermediateCertificateDTO';
import { CertificateDTO } from '../../../dto/certificate/CertificateDTO';

@Component({
  selector: 'app-certificate-form-page-admin',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './certificate-form-page-admin.html',
  styleUrl: './certificate-form-page-admin.css'
})
export class CertificateFormPageAdmin {
  certificateForm: FormGroup;
  nonEECertificatesDTO: NonEECertificateDTO[] = [];
  selectedCerticate: NonEECertificateDTO | null = null;
  certificateTypes: string[] = ["ROOT", "CA", "End-Entity (EE)"]
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
      foundNonEECertficates: ['', Validators.required],
      certificateTypes: ['', Validators.required],
      keyUsageDigitalSignature: [false],
      keyUsageKeyEncipherment: [false],
      eKeyUsageServerAuth: [false],
      eKeyUsageClientAuth: [false]
    });
  }

  ngOnInit(): void {
    this.certificateService.getNonRevokedCACertificates().subscribe({
      next: (certificateList: NonEECertificateDTO[]) => {
        this.nonEECertificatesDTO = certificateList;
      }
    });
    this.certificateForm.get('certificateTypes')!.valueChanges.subscribe(
      (certificateType: string) => {
        const issuerControl = this.certificateForm.get('foundNonEECertficates');
        const issuerControlOrganizationName = this.certificateForm.get('subjectOrganizationName');
        const issuerControlOrganizationalUnit = this.certificateForm.get('subjectOrganizationalUnit');

        if (certificateType === 'ROOT') {
          issuerControl!.clearValidators();
          issuerControl!.disable();
          issuerControl!.setValue("ROOT");
          issuerControlOrganizationName?.clearValidators();
          issuerControlOrganizationName?.disable();
          issuerControlOrganizationalUnit?.clearValidators();
          issuerControlOrganizationalUnit?.disable();
        } else {
          issuerControl!.setValidators(Validators.required);
          issuerControl!.enable();
          if (issuerControl!.getRawValue() == "ROOT") {
            issuerControl!.setValue("");
            issuerControlOrganizationName?.setValue("");
            issuerControlOrganizationalUnit?.setValue("");
          }
          issuerControlOrganizationName?.setValidators(Validators.required);
          issuerControlOrganizationName?.enable();
          issuerControlOrganizationalUnit?.setValidators(Validators.required);
          issuerControlOrganizationalUnit?.enable();
        }
        issuerControl!.updateValueAndValidity();

      }
    );
  }


  onSubmit() {
    if (this.certificateForm.invalid) { return; }
    this.isSubmitting = true;
    this.selectedCerticate = this.certificateForm.get("foundNonEECertficates")?.value;

    if (this.certificateForm.get("certificateTypes")?.value == "End-Entity (EE)") {
      const eeCertificateDTO: EECertificateDTO = {
        issuerCertificateId: this.selectedCerticate!.id,
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
      this.certificateService.createEECertificateRegularUser(eeCertificateDTO, 'true').subscribe({
        next: () => {
          this.snackBar.open('Certificate created!', 'I Understand', {
            duration: undefined,
            verticalPosition: 'bottom',
            panelClass: ["snack-bar-revocation-success"]
          });
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
    } else if (this.certificateForm.get("certificateTypes")?.value == "CA") {
      const intermediateCertificateDTO: IntermediateCertificateDTO = {
        issuerCertificateId: this.selectedCerticate!.id,
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
      this.certificateService.createIntermediateCertificate(intermediateCertificateDTO).subscribe({
        next: () => {
          this.snackBar.open('Certificate created!', 'I Understand', {
            duration: undefined,
            verticalPosition: 'bottom',
            panelClass: ["snack-bar-revocation-success"]
          });
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

    }else{
      const certificateDTO: CertificateDTO = {
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
      this.certificateService.createRootCertificate(certificateDTO).subscribe({
        next: () => {
          this.snackBar.open('Certificate created!', 'I Understand', {
            duration: undefined,
            verticalPosition: 'bottom',
            panelClass: ["snack-bar-revocation-success"]
          });
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
}
