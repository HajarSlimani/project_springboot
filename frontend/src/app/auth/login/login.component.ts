import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/models';

declare const google: any;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  credentials: LoginRequest = { email: '', password: '' };
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngAfterViewInit() {
    this.initializeGoogleSignIn();
  }

  initializeGoogleSignIn() {
    if (typeof google !== 'undefined') {
      google.accounts.id.initialize({
        client_id: '1011731565766-d2u2889t7e3vmmajnn8lu07qmf76ts1d.apps.googleusercontent.com',
        callback: (response: any) => this.handleGoogleCredential(response)
      });
      google.accounts.id.renderButton(
        document.getElementById('googleBtn'),
        { theme: 'outline', size: 'large', text: 'continue_with' }
      );
    }
  }

  handleGoogleCredential(response: any) {
    this.loading = true;
    this.authService.googleAuth(response.credential).subscribe({
      next: () => {
        this.router.navigate(['/plants']);
      },
      error: () => {
        this.error = 'Erreur Google Sign-In';
        this.loading = false;
      }
    });
  }

  login() {
    if (!this.credentials.email || !this.credentials.password) {
      this.error = 'Email et mot de passe requis';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/plants']);
      },
      error: (err) => {
        this.error = 'Email ou mot de passe incorrect';
        this.loading = false;
      }
    });
  }
}
