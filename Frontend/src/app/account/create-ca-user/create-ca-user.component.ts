import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { OrganizationService, Organization } from '../../organization/organization.service';
import { AuthenticatedUserService } from '../authenticatedUser.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { Role } from '../role';

@Component({
  selector: 'app-create-ca-user',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatSnackBarModule],
  templateUrl: './create-ca-user.component.html',
  styleUrl: './create-ca-user.component.css'
})
export class CreateCaUserComponent {
  registerForm: FormGroup;
    organizations: Organization[] = [];
  
    constructor(private fb: FormBuilder,
      private router: Router,
      private authService: AuthenticatedUserService,
      private orgService: OrganizationService,
      private snackBar: MatSnackBar) {
      this.registerForm = this.fb.group({
        firstName: ['', Validators.required],
        lastName: ['', Validators.required],
        email: ['', [Validators.required, Validators.email]],
        organization: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(8), this.passwordStrengthValidator]],
        repeatPassword: ['', Validators.required],
      }, { validators: this.passwordMatchValidator });
    }
  
    ngOnInit(): void {
      this.loadOrganizations();
    }
  
    loadOrganizations() {
      this.orgService.getAll().subscribe({
        next: (data) => (this.organizations = data),
        error: (err) => this.snackBar.open('Failed to load organizations.', 'Close', { duration: 3000 })
      });
    }
  
    isInvalid(controlName: string) {
      const control = this.registerForm.get(controlName);
      return control?.invalid && (control.dirty || control.touched);
    }
  
    passwordStrengthValidator(control: any) {
      const value = control.value || '';
      const hasUpper = /[A-Z]/.test(value);
      const hasLower = /[a-z]/.test(value);
      const hasNumber = /[0-9]/.test(value);
      const hasSpecial = /[@$!%*?&]/.test(value);
      return hasUpper && hasLower && hasNumber && hasSpecial ? null : { weakPassword: true };
    }
  
    passwordMatchValidator(group: FormGroup) {
      const pass = group.get('password')?.value;
      const repeat = group.get('repeatPassword')?.value;
      return pass === repeat ? null : { notMatching: true };
    }
  
    onSubmit(): void {
      if (this.registerForm.invalid) {
        this.snackBar.open('Please fill in all required fields correctly.', 'Close', { duration: 3000 });
        return;
      }
  
      const formValue = this.registerForm.value;
  
      if (formValue.password !== formValue.repeatPassword) {
        this.snackBar.open('Passwords do not match.', 'Close', { duration: 3000 });
        return;
      }
  
      // pronađi izabranu organizaciju po ID-u
      const selectedOrg = this.organizations.find(org => org.id === Number(formValue.organization));
  
      const userData = {
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        password: formValue.password,
        repeatedPassword: formValue.repeatPassword,
        organization: selectedOrg,
        role: Role.CA_USER
      };
  
      this.authService.registerCaUser(userData).subscribe({
        next: () => {
          this.snackBar.open(
            'CA User created!',
            'Close',
            { duration: 5000, panelClass: ['snackbar-success'] }
          );
          this.registerForm.reset();
        },
        error: (err: HttpErrorResponse) => {
          if (err.error == "Password doesn't meet the requirements!") {
            this.snackBar.open('Password does not meet the requirements', 'Close', { duration: 4000, panelClass: ['snackbar-error'] });
  
          } else if (err.error == "Passwords do not match!") {
            this.snackBar.open('Passwords do not match!', 'Close', { duration: 4000, panelClass: ['snackbar-error'] });
          } else {
            this.snackBar.open('Registration failed. Please try again.', 'Close', { duration: 4000, panelClass: ['snackbar-error'] });
          }
        }
      });
    }
}
