import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-download-dialog',
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './download-dialog.html',
  styleUrl: './download-dialog.css'
})
export class DownloadDialog {
  dialogForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<DownloadDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,

    private fb: FormBuilder,
  ) {
    this.dialogForm = this.fb.group({
      passwordInput: ['', [Validators.required, Validators.pattern(/^(?=.*[A-Za-z0-9]).+$/)]],
    });
  }


  // Metoda pozvana klikom na dugme 'Potvrdi'
  sendDataAndClose(): void {
    if (this.dialogForm.invalid) return;
    const resultData = {
      success: true,
      password: this.dialogForm.get("passwordInput")?.value // Podatak koji vraćamo
    };
    this.dialogRef.close(resultData);
  }

  // Metoda pozvana klikom na dugme 'Odustani'
  onCancel(): void {
    // Obično se vraća falsy vrednost ili undefined da se signalizira otkazivanje
    this.dialogRef.close(undefined);
  }
}
