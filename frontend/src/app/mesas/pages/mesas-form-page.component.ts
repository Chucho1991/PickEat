import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MesasApiService, MesaRequest } from '../../core/services/mesas-api.service';
import { AuthService } from '../../core/services/auth.service';

/**
 * Pagina de formulario para crear o editar mesas.
 */
@Component({
  selector: 'app-mesas-form-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">{{ isEdit ? 'Editar mesa' : 'Nueva mesa' }}</h2>
        <p class="page-subtitle">Administra la configuracion de mesas.</p>
      </div>
      <button class="btn btn-ghost" type="button" (click)="back()">Volver</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <label class="field">
          <span>Descripcion</span>
          <input type="text" formControlName="description" [class.invalid]="isInvalid('description')" />
          <span class="error-text" *ngIf="showRequired('description')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Sillas</span>
          <input type="number" min="1" formControlName="seats" [class.invalid]="isInvalid('seats')" />
          <span class="error-text" *ngIf="showRequired('seats')">Este campo es obligatorio.</span>
          <span class="error-text" *ngIf="form.get('seats')?.hasError('min')">Debe ser mayor o igual a 1.</span>
        </label>
        <label class="field">
          <span>Estado</span>
          <select formControlName="activo" [class.invalid]="isInvalid('activo')" [disabled]="!isSuperadmin">
            <option [value]="true">ACTIVO</option>
            <option [value]="false">INACTIVO</option>
          </select>
          <span class="error-text" *ngIf="showRequired('activo')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Ocupacion</span>
          <select formControlName="ocupada" [class.invalid]="isInvalid('ocupada')" [disabled]="!isSuperadmin">
            <option [value]="false">LIBRE</option>
            <option [value]="true">OCUPADA</option>
          </select>
          <span class="error-text" *ngIf="showRequired('ocupada')">Este campo es obligatorio.</span>
        </label>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit" [disabled]="form.invalid">Guardar</button>
          <button class="btn btn-ghost" type="button" (click)="back()">Cancelar</button>
        </div>
      </form>
    </div>
  `
})
export class MesasFormPageComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private mesasApi = inject(MesasApiService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);

  isEdit = false;
  mesaId?: string;
  isSuperadmin = this.authService.hasRole(['SUPERADMINISTRADOR']);

  form = this.formBuilder.group({
    description: ['', Validators.required],
    seats: [1, [Validators.required, Validators.min(1)]],
    activo: [{ value: true, disabled: !this.isSuperadmin }, Validators.required],
    ocupada: [{ value: false, disabled: !this.isSuperadmin }, Validators.required]
  });

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.mesaId = id;
      this.mesasApi.getById(id).subscribe({
        next: (mesa) =>
          this.form.patchValue({
            description: mesa.description,
            seats: mesa.seats,
            activo: mesa.activo,
            ocupada: mesa.ocupada
          }),
        error: () => this.snackBar.open('No se pudo cargar la mesa', 'Cerrar', { duration: 3000 })
      });
    }
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }
    const value = this.form.getRawValue();
    const payload: MesaRequest = {
      description: value.description ?? '',
      seats: Number(value.seats ?? 0),
      activo: value.activo ?? true,
      ocupada: value.ocupada ?? false
    };

    const request$ = this.isEdit && this.mesaId
      ? this.mesasApi.update(this.mesaId, payload)
      : this.mesasApi.create(payload);

    request$.subscribe({
      next: () => this.finishSave(),
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo guardar', 'Cerrar', { duration: 3000 })
    });
  }

  back() {
    this.router.navigate(['/mesas']);
  }

  isInvalid(control: string) {
    const field = this.form.get(control);
    return !!field && field.touched && field.invalid;
  }

  showRequired(control: string) {
    const field = this.form.get(control);
    return !!field && field.touched && field.hasError('required');
  }

  private finishSave() {
    this.snackBar.open(this.isEdit ? 'Mesa actualizada' : 'Mesa creada', 'Cerrar', { duration: 3000 });
    this.router.navigate(['/mesas']);
  }
}
