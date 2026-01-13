import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSnackBarModule],
  template: `
    <div class="login-wrapper">
      <mat-card class="card">
        <h2 class="page-title">Iniciar sesión</h2>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Usuario o correo</mat-label>
            <input matInput formControlName="usernameOrEmail" />
          </mat-form-field>
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Contraseña</mat-label>
            <input matInput type="password" formControlName="password" />
          </mat-form-field>
          <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Entrar</button>
        </form>
      </mat-card>
    </div>
  `,
  styles: [
    `
      .login-wrapper {
        max-width: 420px;
        margin: 40px auto;
      }
      .full-width {
        width: 100%;
      }
    `
  ]
})
export class LoginPageComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  form = inject(FormBuilder).group({
    usernameOrEmail: ['', Validators.required],
    password: ['', Validators.required]
  });

  onSubmit() {
    if (this.form.invalid) {
      return;
    }
    const { usernameOrEmail, password } = this.form.getRawValue();
    this.authService.login(usernameOrEmail ?? '', password ?? '').subscribe({
      next: (response) => {
        this.authService.handleLogin(response);
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.snackBar.open(error?.error?.error || 'No se pudo iniciar sesión', 'Cerrar', { duration: 3000 });
      }
    });
  }
}
