import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  credentials: RegisterRequest = { username: '', email: '', password: '' };
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register() {
    if (!this.credentials.username || !this.credentials.email || !this.credentials.password) {
      this.error = 'Tous les champs sont requis';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.register(this.credentials).subscribe({
      next: () => {
        this.router.navigate(['/plants']);
      },
      error: (err) => {
        this.error = err.error?.message || 'Erreur lors de l\'inscription';
        this.loading = false;
      }
    });
  }
}
