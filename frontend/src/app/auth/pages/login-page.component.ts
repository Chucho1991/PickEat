import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/services/auth.service';

/**
 * Página de inicio de sesión.
 */
@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, MatSnackBarModule],
  template: `
    <div class="auth-page">
      <section class="auth-panel auth-form">
        <div class="auth-content">
          <a routerLink="/" class="back-link" aria-label="Volver">
            <span class="back-icon" aria-hidden="true">
              <svg viewBox="0 0 20 20" fill="none" aria-hidden="true">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M2.58203 9.99868C2.58174 10.1909 2.6549 10.3833 2.80152 10.53L7.79818 15.5301C8.09097 15.8231 8.56584 15.8233 8.85883 15.5305C9.15183 15.2377 9.152 14.7629 8.85921 14.4699L5.13911 10.7472L16.6665 10.7472C17.0807 10.7472 17.4165 10.4114 17.4165 9.99715C17.4165 9.58294 17.0807 9.24715 16.6665 9.24715L5.14456 9.24715L8.85919 5.53016C9.15199 5.23717 9.15184 4.7623 8.85885 4.4695C8.56587 4.1767 8.09099 4.17685 7.79819 4.46984L2.84069 9.43049C2.68224 9.568 2.58203 9.77087 2.58203 9.99715C2.58203 9.99766 2.58203 9.99817 2.58203 9.99868Z" fill="currentColor"></path>
              </svg>
            </span>
            Volver al inicio
          </a>
          <div class="auth-card">
            <div class="auth-header">
              <h1>Iniciar sesion</h1>
              <p>Ingresa tu usuario o correo y contrasena para entrar.</p>
            </div>

            <div class="oauth-grid">
              <button type="button" class="oauth-btn">
                <span class="oauth-icon" aria-hidden="true">
                  <svg viewBox="0 0 20 20" fill="none" aria-hidden="true">
                    <path fill-rule="evenodd" clip-rule="evenodd" d="M3.04175 9.37363C3.04175 5.87693 5.87711 3.04199 9.37508 3.04199C12.8731 3.04199 15.7084 5.87693 15.7084 9.37363C15.7084 12.8703 12.8731 15.7053 9.37508 15.7053C5.87711 15.7053 3.04175 12.8703 3.04175 9.37363ZM9.37508 1.54199C5.04902 1.54199 1.54175 5.04817 1.54175 9.37363C1.54175 13.6991 5.04902 17.2053 9.37508 17.2053C11.2674 17.2053 13.003 16.5344 14.357 15.4176L17.177 18.238C17.4699 18.5309 17.9448 18.5309 18.2377 18.238C18.5306 17.9451 18.5306 17.4703 18.2377 17.1774L15.418 14.3573C16.5365 13.0033 17.2084 11.2669 17.2084 9.37363C17.2084 5.04817 13.7011 1.54199 9.37508 1.54199Z" fill="currentColor"></path>
                  </svg>
                </span>
                Continuar con Google
              </button>
              <button type="button" class="oauth-btn">
                <span class="oauth-icon" aria-hidden="true">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path fill-rule="evenodd" clip-rule="evenodd" d="M6.21967 7.28131C5.92678 6.98841 5.92678 6.51354 6.21967 6.22065C6.51256 5.92775 6.98744 5.92775 7.28033 6.22065L11.999 10.9393L16.7176 6.22078C17.0105 5.92789 17.4854 5.92788 17.7782 6.22078C18.0711 6.51367 18.0711 6.98855 17.7782 7.28144L13.0597 12L17.7782 16.7186C18.0711 17.0115 18.0711 17.4863 17.7782 17.7792C17.4854 18.0721 17.0105 18.0721 16.7176 17.7792L11.999 13.0607L7.28033 17.7794C6.98744 18.0722 6.51256 18.0722 6.21967 17.7794C5.92678 17.4865 5.92678 17.0116 6.21967 16.7187L10.9384 12L6.21967 7.28131Z" fill="currentColor"></path>
                  </svg>
                </span>
                Continuar con X
              </button>
            </div>

            <div class="divider"><span>O</span></div>

            <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form-fields">
              <label class="field">
                <span>Usuario o correo</span>
                <input
                  type="text"
                  formControlName="usernameOrEmail"
                  placeholder="usuario@correo.com"
                  [class.invalid]="form.get('usernameOrEmail')?.touched && form.get('usernameOrEmail')?.invalid"
                />
                <span class="error-text" *ngIf="form.get('usernameOrEmail')?.touched && form.get('usernameOrEmail')?.hasError('required')">
                  Este campo es obligatorio.
                </span>
              </label>
              <label class="field">
                <span>Contrasena</span>
                <input
                  type="password"
                  formControlName="password"
                  placeholder="********"
                  [class.invalid]="form.get('password')?.touched && form.get('password')?.invalid"
                />
                <span class="error-text" *ngIf="form.get('password')?.touched && form.get('password')?.hasError('required')">
                  Este campo es obligatorio.
                </span>
              </label>
              <button class="primary-btn" type="submit" [disabled]="form.invalid">Entrar</button>
            </form>
          </div>
        </div>
      </section>

      <section class="auth-panel auth-hero" aria-hidden="true">
        <div class="hero-inner">
          <div class="hero-logo">PickEat</div>
          <p>Panel administrativo moderno para gestionar tu restaurante.</p>
        </div>
      </section>
    </div>
  `,
  styles: [
    `
      .auth-page {
        min-height: 100vh;
        display: flex;
        background: radial-gradient(circle at top left, #eef2ff, #f8fafc 45%, #ffffff 100%);
      }
      .auth-panel {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 48px 24px;
      }
      .auth-form {
        background: transparent;
      }
      .auth-content {
        width: 100%;
        max-width: 420px;
      }
      .back-link {
        display: inline-flex;
        align-items: center;
        gap: 8px;
        color: #6b7280;
        text-decoration: none;
        font-size: 14px;
        margin-bottom: 20px;
      }
      .back-link:hover {
        color: #374151;
      }
      .back-icon {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 18px;
        height: 18px;
      }
      .back-icon svg {
        width: 18px;
        height: 18px;
      }
      .auth-card {
        background: #ffffff;
        border-radius: 16px;
        padding: 28px;
        box-shadow: 0 20px 40px rgba(15, 23, 42, 0.08);
        border: 1px solid #e5e7eb;
      }
      .auth-header h1 {
        margin: 0 0 8px;
        font-size: 24px;
        color: #0f172a;
      }
      .auth-header p {
        margin: 0 0 20px;
        color: #6b7280;
        font-size: 14px;
      }
      .oauth-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
        gap: 12px;
        margin-bottom: 18px;
      }
      .oauth-btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        padding: 10px 12px;
        border-radius: 10px;
        border: 1px solid #e5e7eb;
        background: #f8fafc;
        color: #111827;
        cursor: pointer;
        font-size: 13px;
      }
      .oauth-btn:hover {
        background: #eef2ff;
      }
      .oauth-icon {
        width: 22px;
        height: 22px;
        border-radius: 50%;
        background: #ffffff;
        border: 1px solid #e5e7eb;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        color: #111827;
      }
      .oauth-icon svg {
        width: 12px;
        height: 12px;
      }
      .divider {
        position: relative;
        text-align: center;
        margin: 18px 0;
      }
      .divider::before {
        content: "";
        position: absolute;
        top: 50%;
        left: 0;
        right: 0;
        height: 1px;
        background: #e5e7eb;
      }
      .divider span {
        position: relative;
        background: #ffffff;
        padding: 0 12px;
        color: #9ca3af;
        font-size: 12px;
      }
      .auth-form-fields {
        display: grid;
        gap: 14px;
      }
      .field {
        display: grid;
        gap: 6px;
        font-size: 13px;
        color: #374151;
      }
      .field input {
        height: 42px;
        border-radius: 10px;
        border: 1px solid #d1d5db;
        padding: 0 12px;
        font-size: 14px;
      }
      .field input:focus {
        outline: none;
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
      }
      .primary-btn {
        height: 44px;
        border: none;
        border-radius: 10px;
        background: #4f46e5;
        color: #ffffff;
        font-weight: 600;
        cursor: pointer;
        transition: transform 0.1s ease;
      }
      .primary-btn:disabled {
        opacity: 0.6;
        cursor: not-allowed;
      }
      .primary-btn:active:not(:disabled) {
        transform: translateY(1px);
      }
      .auth-hero {
        display: none;
        background: #0f172a;
        color: #e2e8f0;
      }
      .hero-inner {
        text-align: center;
        max-width: 320px;
      }
      .hero-logo {
        font-size: 28px;
        font-weight: 700;
        letter-spacing: 1px;
        margin-bottom: 10px;
      }
      .hero-inner p {
        margin: 0;
        color: #94a3b8;
      }
      @media (min-width: 1024px) {
        .auth-hero {
          display: flex;
        }
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

  /**
   * Envía las credenciales y gestiona el flujo de inicio de sesión.
   */
  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'top',
        panelClass: ['snack-top-center']
      });
      return;
    }
    const { usernameOrEmail, password } = this.form.getRawValue();
    this.authService.login(usernameOrEmail ?? '', password ?? '').subscribe({
      next: (response) => {
        this.authService.handleLogin(response);
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.snackBar.open(error?.error?.error || 'No se pudo iniciar sesion', 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
          panelClass: ['snack-top-center']
        });
      }
    });
  }
}
