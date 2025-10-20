import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TemplateService } from '../template.service';
import { AuthService } from '../../account/auth.service';
import { AuthenticatedUserService } from '../../account/authenticatedUser.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-template',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-template.component.html',
  styleUrls: ['./create-template.component.css']
})
export class CreateTemplateComponent implements OnInit {

  templateForm!: FormGroup;
  currentUser: any;

  constructor(
    private fb: FormBuilder,
    private templateService: TemplateService,
    private authService: AuthService,
    private userService: AuthenticatedUserService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.templateForm = this.fb.group({
      name: ['', Validators.required],
      cnRegex: ['', Validators.required],
      sanRegex: ['', Validators.required],
      maxTtlDays: [1, [Validators.required, Validators.min(1)]],
      defaultKeyUsage: ['Digital Signature', Validators.required],
      defaultExtendedKeyUsage: ['ServerAuth', Validators.required]
    });

    const email = this.authService.getEmail();
    if (email) {
      this.userService.getUserByEmail(email).subscribe({
        next: (user) => {
          this.currentUser = user;
          console.log('Logged in user:', user);
        },
        error: (err) => {
          console.error('Failed to fetch user:', err);
          this.snackBar.open('Failed to load user data.', 'Close', { duration: 3000 });
        }
      });
    }
  }

  onSubmit(): void {
    if (this.templateForm.invalid || !this.currentUser) {
      this.snackBar.open('Please fill all required fields.', 'Close', { duration: 3000 });
      return;
    }

    const formData = this.templateForm.value;

    const dto = {
      name: formData.name,
      organizationId: this.currentUser.organizationId, // ✅ uzeta iz backend usera
      caIssuer: this.currentUser,                  // ✅ ceo korisnik
      cnRegex: formData.cnRegex,
      sanRegex: formData.sanRegex,
      maxTtlDays: formData.maxTtlDays,
      defaultKeyUsage: formData.defaultKeyUsage,
      defaultExtendedKeyUsage: formData.defaultExtendedKeyUsage
    };

    console.log(dto);

    this.templateService.createTemplate(dto).subscribe({
      next: () => {
        this.snackBar.open('Template successfully created!', 'Close', { duration: 3000 });
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Error creating template:', err);
        this.snackBar.open('Failed to create template.', 'Close', { duration: 3000 });
      }
    });
  }
}
