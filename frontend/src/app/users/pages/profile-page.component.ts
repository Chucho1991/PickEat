import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsersService } from '../../core/services/users.service';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatSnackBarModule],
  template: `
    <h2 class="page-title">Mi perfil</h2>
    <mat-card class="card">
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Nombres</mat-label>
          <input matInput formControlName="nombres" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Correo</mat-label>
          <input matInput formControlName="correo" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Username</mat-label>
          <input matInput formControlName="username" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Nueva contraseña</mat-label>
          <input matInput type="password" formControlName="password" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Confirmar contraseña</mat-label>
          <input matInput type="password" formControlName="confirmPassword" />
        </mat-form-field>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Guardar cambios</button>
      </form>
    </mat-card>
  `,
  styles: [
    `
      .full-width {
        width: 100%;
      }
    `
  ]
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
