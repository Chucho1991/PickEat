import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsersService } from '../../core/services/users.service';

/**
 * Página para editar el perfil del usuario autenticado.
 */
@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Mi perfil</h2>
        <p class="page-subtitle">Actualiza tu informacion personal.</p>
      </div>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <label class="field">
          <span>Nombres</span>
          <input type="text" formControlName="nombres" [class.invalid]="isInvalid('nombres')" />
          <span class="error-text" *ngIf="showRequired('nombres')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Correo</span>
          <input type="email" formControlName="correo" [class.invalid]="isInvalid('correo')" />
          <span class="error-text" *ngIf="showRequired('correo')">Este campo es obligatorio.</span>
          <span class="error-text" *ngIf="form.get('correo')?.touched && form.get('correo')?.hasError('email')">
            Ingresa un correo valido.
          </span>
        </label>
        <label class="field">
          <span>Username</span>
          <input type="text" formControlName="username" [class.invalid]="isInvalid('username')" />
          <span class="error-text" *ngIf="showRequired('username')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Nueva contrasena</span>
          <input type="password" formControlName="password" [class.invalid]="isInvalid('password') || form.hasError('passwordRequired')" />
          <span class="error-text" *ngIf="form.get('password')?.touched && form.get('password')?.hasError('minlength')">
            Minimo 8 caracteres.
          </span>
          <span class="error-text" *ngIf="form.get('password')?.touched && form.hasError('passwordRequired')">
            Debes completar la contrasena.
          </span>
        </label>
        <label class="field">
          <span>Confirmar contrasena</span>
          <input type="password" formControlName="confirmPassword" [class.invalid]="isInvalid('confirmPassword') || form.hasError('passwordMismatch') || form.hasError('passwordRequired')" />
          <span class="error-text" *ngIf="form.get('confirmPassword')?.touched && form.get('confirmPassword')?.hasError('minlength')">
            Minimo 8 caracteres.
          </span>
          <span class="error-text" *ngIf="form.get('confirmPassword')?.touched && form.hasError('passwordRequired')">
            Debes confirmar la contrasena.
          </span>
          <span class="error-text" *ngIf="form.get('confirmPassword')?.touched && form.hasError('passwordMismatch')">
            Las contrasenas no coinciden.
          </span>
        </label>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit" [disabled]="form.invalid">Guardar cambios</button>
        </div>
      </form>
    </div>
  `
})
export class ProfilePageComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private usersService = inject(UsersService);
  private snackBar = inject(MatSnackBar);

  form = this.formBuilder.group({
    nombres: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['', Validators.minLength(8)],
    confirmPassword: ['', Validators.minLength(8)]
  });

  /**
   * Carga el perfil actual y completa el formulario.
   */
  ngOnInit() {
    this.usersService.getMe().subscribe(user => {
      this.form.patchValue({
        nombres: user.nombres,
        correo: user.correo,
        username: user.username
      });
    });
    this.form.setValidators(this.passwordsMatchValidator());
  }

  /**
   * Envía los cambios del perfil al backend.
   */
  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }
    const value = this.form.getRawValue();
    this.usersService.updateMe({
      nombres: value.nombres ?? '',
      correo: value.correo ?? '',
      username: value.username ?? '',
      password: value.password ?? undefined,
      confirmPassword: value.confirmPassword ?? undefined
    }).subscribe({
      next: () => this.snackBar.open('Perfil actualizado', 'Cerrar', { duration: 3000 }),
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar', 'Cerrar', { duration: 3000 })
    });
  }

  private passwordsMatchValidator() {
    return (control: import('@angular/forms').AbstractControl) => {
      const group = control as typeof this.form;
      const password = group.get('password')?.value;
      const confirmPassword = group.get('confirmPassword')?.value;
      if (!password && !confirmPassword) {
        return null;
      }
      if (!password || !confirmPassword) {
        return { passwordRequired: true };
      }
      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }

  isInvalid(controlName: string) {
    const control = this.form.get(controlName);
    return Boolean(control && control.touched && control.invalid);
  }

  showRequired(controlName: string) {
    const control = this.form.get(controlName);
    return Boolean(control && control.touched && control.hasError('required'));
  }
}
