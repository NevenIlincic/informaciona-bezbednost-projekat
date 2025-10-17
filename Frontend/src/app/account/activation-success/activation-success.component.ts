import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-activation-success',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activation-success.component.html',
  styleUrls: ['./activation-success.component.css']
})
export class ActivationSuccessComponent { }
