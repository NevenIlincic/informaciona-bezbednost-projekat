import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import {RouterLink, Router} from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../auth.service';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true, // <--- ovo je važno u standalone pristupu
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MatSnackBarModule, HttpClientModule], // <-- dodato ovde
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  loginForm: FormGroup;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.authService.isLoggedIn()){
      this.router.navigate(["/home"]);
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.isSubmitting = true;

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.snackBar.open('Login successful! Redirecting...', 'Close', {
          duration: 2500,
          panelClass: ['snack-bar-revocation-success']
        });
        this.router.navigate(['/home']);
      },
      error: () => {
        this.isSubmitting = false;
        this.snackBar.open('Invalid email or password.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }
}
