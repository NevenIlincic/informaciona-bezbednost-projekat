import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-activation-failure',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './activation-failure.component.html',
  styleUrls: ['./activation-failure.component.css']
})
export class ActivationFailureComponent { }
