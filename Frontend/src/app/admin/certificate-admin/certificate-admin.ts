import { Component, Input } from '@angular/core';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CertificateTabDTO } from '../../../dto/certificate/CertificateTabDTO';
import { RevocationDTO } from '../../../dto/certificate/RevocationDTO';
import { RevokeDialog } from '../../dialog/revoke-dialog/revoke-dialog';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-certificate-admin',
  imports: [MatCardModule, CommonModule, MatDialogModule],
  templateUrl: './certificate-admin.html',
  styleUrl: './certificate-admin.css'
})
export class CertificateAdmin {
  constructor(private certificateService: CertificateService, private revokeDialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  @Input() singleCertificate: CertificateTabDTO = {} as CertificateTabDTO;

  keyConstraints: string[] = [];
  extentedKeyConstraints: string[] = [];
  validFrom: Date = new Date();
  validTo: Date = new Date();
  revocationDate: Date = new Date();

  ngOnInit(): void {
    this.validFrom = new Date(this.singleCertificate.validFrom);
    this.validTo = new Date(this.singleCertificate.validTo);

    if (this.singleCertificate.revoked) {
      this.revocationDate = new Date(this.singleCertificate.revocationDate)
    }

    this.keyConstraints = [this.singleCertificate.digitalSignature, this.singleCertificate.keyEncipherment];
    this.keyConstraints = this.keyConstraints.filter(item => item !== '');
    this.extentedKeyConstraints = [this.singleCertificate.serverAuth, this.singleCertificate.clientAuth];
    this.extentedKeyConstraints = this.extentedKeyConstraints.filter(item => item !== '');
    // if (filteredString == ""){
    //   this.keyConstraints = [];
    // }else{
    // }
  }

  revokeCertificate(certificateId: number, reason: string) {
    const revocation: RevocationDTO = {
      id: certificateId,
      revocationReason: reason
    }
    this.certificateService.revokeCertificate(revocation).subscribe({
      next: () => {
        this.singleCertificate.revoked = true;
        this.singleCertificate.revocationReason = reason;
        this.revocationDate = new Date();
        this.snackBar.open('Certificate successfully revoked!', '', {
              duration: 2500,
              verticalPosition: 'bottom',
              panelClass: ["snack-bar-revocation-success"]
            });
      },
      error: () =>{
         this.snackBar.open('Error occured', "", {
              duration: 2500,
              verticalPosition: 'bottom',
              panelClass: ["snack-bar-refresh-token-error"]
            });
      }
    });
  }

  openRevokeDialog(certificateId: number): void {
    const dialogRef = this.revokeDialog.open(RevokeDialog, {
      panelClass:["high-z-index-dialog"]
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if (result) {
        if (result.success) {
          this.revokeCertificate(certificateId, result.reason);
        }
      }
    });
  }
}
