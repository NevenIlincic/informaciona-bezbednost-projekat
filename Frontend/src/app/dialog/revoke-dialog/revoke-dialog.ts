import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog'
@Component({
  selector: 'app-revoke-dialog',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './revoke-dialog.html',
  styleUrl: './revoke-dialog.css'
})
export class RevokeDialog {
  dialogForm: FormGroup;

  reasonList: string[] = ["Key Compromise", "CA Compromise", "Affiliation Changed", "Superseded", "Cessation of Operation"];

  constructor(
    public dialogRef: MatDialogRef<RevokeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,

    private fb: FormBuilder,
  ) { 
    this.dialogForm = this.fb.group({
      reasonInput: ['', [Validators.required, Validators.pattern(/^(?=.*[A-Za-z0-9]).+$/)]],
    });
  }


  // Metoda pozvana klikom na dugme 'Potvrdi'
  sendDataAndClose(): void {
    if (this.dialogForm.invalid) return;
    const resultData = {
      success: true,
      reason: this.dialogForm.get("reasonInput")?.value // Podatak koji vraćamo
    };
    this.dialogRef.close(resultData);
  }

  // Metoda pozvana klikom na dugme 'Odustani'
  onCancel(): void {
    // Obično se vraća falsy vrednost ili undefined da se signalizira otkazivanje
    this.dialogRef.close(undefined);
  }
}

