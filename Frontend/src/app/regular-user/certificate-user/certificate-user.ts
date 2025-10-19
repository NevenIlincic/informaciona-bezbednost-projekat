import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { RegularUserCertificateDTO } from '../../../dto/certificate/RegularUserCertificateDTO';
import { CommonModule, NgForOf } from '@angular/common';
import { CertificateService } from '../../certificate-form-page/certificate-service';
import { RevocationDTO } from '../../../dto/certificate/RevocationDTO';
import { RevokeDialog } from '../../dialog/revoke-dialog/revoke-dialog';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-certificate-user',
  imports: [MatCardModule, CommonModule, MatDialogModule],
  templateUrl: './certificate-user.html',
  styleUrl: './certificate-user.css'
})
export class CertificateUser implements OnInit {

  constructor(private certificateService: CertificateService, private revokeDialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  @Input() userCertificate: RegularUserCertificateDTO = {} as RegularUserCertificateDTO;

  keyConstraints: string[] = [];
  validFrom: Date = new Date();
  validTo: Date = new Date();
  revocationDate: Date = new Date();

  ngOnInit(): void {
    this.validFrom = new Date(this.userCertificate.validFrom);
    this.validTo = new Date(this.userCertificate.validTo);

    if (this.userCertificate.revoked) {
      this.revocationDate = new Date(this.userCertificate.revocationDate)
    }

    this.keyConstraints = [this.userCertificate.digitalSignature, this.userCertificate.keyEncipherment];
    this.keyConstraints = this.keyConstraints.filter(item => item !== '');
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
        this.userCertificate.revoked = true;
        this.userCertificate.revocationReason = reason;
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
