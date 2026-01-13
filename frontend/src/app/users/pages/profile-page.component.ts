import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsersService } from '../../core/services/users.service';

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
          <input type="text" formControlName="nombres" />
        </label>
        <label class="field">
          <span>Correo</span>
          <input type="email" formControlName="correo" />
        </label>
        <label class="field">
          <span>Username</span>
          <input type="text" formControlName="username" />
        </label>
        <label class="field">
          <span>Nueva contrasena</span>
          <input type="password" formControlName="password" />
        </label>
        <label class="field">
          <span>Confirmar contrasena</span>
          <input type="password" formControlName="confirmPassword" />
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
    password: [''],
    confirmPassword: ['']
  });

  ngOnInit() {
    this.usersService.getMe().subscribe(user => {
      this.form.patchValue({
        nombres: user.nombres,
        correo: user.correo,
        username: user.username
      });
    });
  }

  onSubmit() {
    if (this.form.invalid) {
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
}
