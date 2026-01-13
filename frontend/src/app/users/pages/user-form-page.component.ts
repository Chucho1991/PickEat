import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsersService } from '../../core/services/users.service';

@Component({
  selector: 'app-user-form-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">{{ isEdit ? 'Editar usuario' : 'Nuevo usuario' }}</h2>
        <p class="page-subtitle">Completa la informacion del usuario.</p>
      </div>
      <button class="btn btn-ghost" type="button" (click)="back()">Volver</button>
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
          <span>Rol</span>
          <select formControlName="rol">
            <option value="" disabled>Selecciona un rol</option>
            <option *ngFor="let rol of roles" [value]="rol">{{ rol }}</option>
          </select>
        </label>
        <label class="field" *ngIf="!isEdit">
          <span>Contrasena</span>
          <input type="password" formControlName="password" />
        </label>
        <label class="field" *ngIf="!isEdit">
          <span>Confirmar contrasena</span>
          <input type="password" formControlName="confirmPassword" />
        </label>
        <label class="field" *ngIf="isEdit">
          <span>Activo</span>
          <select formControlName="activo">
            <option [value]="true">Si</option>
            <option [value]="false">No</option>
          </select>
        </label>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit" [disabled]="form.invalid">Guardar</button>
          <button class="btn btn-ghost" type="button" (click)="back()">Cancelar</button>
        </div>
      </form>
    </div>
  `
})
export class UserFormPageComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private usersService = inject(UsersService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  isEdit = false;
  userId?: string;
  roles = ['SUPERADMINISTRADOR', 'ADMINISTRADOR', 'MESERO', 'DESPACHADOR'];

  form = this.formBuilder.group({
    nombres: ['', Validators.required],
    correo: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    rol: ['', Validators.required],
    password: [''],
    confirmPassword: [''],
    activo: [true]
  });

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.userId = id;
      this.usersService.getById(id).subscribe(user => {
        this.form.patchValue({
          nombres: user.nombres,
          correo: user.correo,
          username: user.username,
          rol: user.rol,
          activo: user.activo
        });
      });
    }
    this.configurePasswordValidation();
    this.form.setValidators(this.passwordsMatchValidator());
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }
    const value = this.form.getRawValue();
    if (this.isEdit && this.userId) {
      this.usersService.update(this.userId, {
        nombres: value.nombres ?? '',
        correo: value.correo ?? '',
        username: value.username ?? '',
        rol: value.rol ?? '',
        activo: value.activo ?? true
      }).subscribe({
        next: () => {
          this.snackBar.open('Usuario actualizado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/users']);
        },
        error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar', 'Cerrar', { duration: 3000 })
      });
    } else {
      this.usersService.create({
        nombres: value.nombres ?? '',
        correo: value.correo ?? '',
        username: value.username ?? '',
        password: value.password ?? '',
        confirmPassword: value.confirmPassword ?? '',
        rol: value.rol ?? ''
      }).subscribe({
        next: () => {
          this.snackBar.open('Usuario creado', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/users']);
        },
        error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo crear', 'Cerrar', { duration: 3000 })
      });
    }
  }

  back() {
    this.router.navigate(['/users']);
  }

  private configurePasswordValidation() {
    const passwordControl = this.form.get('password');
    const confirmControl = this.form.get('confirmPassword');
    if (this.isEdit) {
      passwordControl?.clearValidators();
      confirmControl?.clearValidators();
    } else {
      passwordControl?.setValidators([Validators.required, Validators.minLength(8)]);
      confirmControl?.setValidators([Validators.required, Validators.minLength(8)]);
    }
    passwordControl?.updateValueAndValidity();
    confirmControl?.updateValueAndValidity();
  }

  private passwordsMatchValidator(): ValidatorFn {
    return (group) => {
      if (this.isEdit) {
        return null;
      }
      const password = group.get('password')?.value;
      const confirmPassword = group.get('confirmPassword')?.value;
      if (!password && !confirmPassword) {
        return null;
      }
      return password === confirmPassword ? null : { passwordMismatch: true };
    };
  }
}
