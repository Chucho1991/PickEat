import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsersService } from '../../core/services/users.service';

@Component({
  selector: 'app-user-form-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule
  ],
  template: `
    <h2 class="page-title">{{ isEdit ? 'Editar usuario' : 'Nuevo usuario' }}</h2>
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
          <mat-label>Rol</mat-label>
          <mat-select formControlName="rol">
            <mat-option *ngFor="let rol of roles" [value]="rol">{{ rol }}</mat-option>
          </mat-select>
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width" *ngIf="!isEdit">
          <mat-label>Contraseña</mat-label>
          <input matInput type="password" formControlName="password" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width" *ngIf="!isEdit">
          <mat-label>Confirmar contraseña</mat-label>
          <input matInput type="password" formControlName="confirmPassword" />
        </mat-form-field>
        <mat-form-field appearance="outline" class="full-width" *ngIf="isEdit">
          <mat-label>Activo</mat-label>
          <mat-select formControlName="activo">
            <mat-option [value]="true">Sí</mat-option>
            <mat-option [value]="false">No</mat-option>
          </mat-select>
        </mat-form-field>
        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">Guardar</button>
        <button mat-button type="button" (click)="back()">Cancelar</button>
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
  }

  onSubmit() {
    if (this.form.invalid) {
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
}
