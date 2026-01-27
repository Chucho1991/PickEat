import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../core/services/auth.service';
import { DiscountsApiService, DiscountItemRequest } from '../../core/services/discounts-api.service';
import { environment } from '../../../environments/environment';

/**
 * Pagina de formulario para crear o editar descuentos.
 */
@Component({
  selector: 'app-discounts-form-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">{{ isEdit ? 'Editar descuento' : 'Nuevo descuento' }}</h2>
        <p class="page-subtitle">Completa la informacion del descuento.</p>
      </div>
      <button class="btn btn-ghost" type="button" (click)="back()">Volver</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <label class="field full-width">
          <span>Descripcion larga</span>
          <textarea rows="4" formControlName="longDescription" [class.invalid]="isInvalid('longDescription')"></textarea>
          <span class="error-text" *ngIf="showRequired('longDescription')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Descripcion corta</span>
          <input type="text" formControlName="shortDescription" [class.invalid]="isInvalid('shortDescription')" />
          <span class="error-text" *ngIf="showRequired('shortDescription')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Pseudonimo</span>
          <input type="text" formControlName="nickname" [class.invalid]="isInvalid('nickname')" />
          <span class="error-text" *ngIf="showRequired('nickname')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Tipo de descuento</span>
          <select formControlName="discountType" [class.invalid]="isInvalid('discountType')">
            <option value="" disabled>Selecciona un tipo</option>
            <option *ngFor="let type of discountTypes" [value]="type">{{ type }}</option>
          </select>
          <span class="error-text" *ngIf="showRequired('discountType')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Valor</span>
          <input type="number" step="0.01" min="0" formControlName="value" [class.invalid]="isInvalid('value')" />
          <span class="error-text" *ngIf="showRequired('value')">Este campo es obligatorio.</span>
          <span class="error-text" *ngIf="form.get('value')?.hasError('min')">Debe ser mayor o igual a 0.</span>
        </label>
        <label class="field">
          <span>Activo</span>
          <select formControlName="activo" [class.invalid]="isInvalid('activo')" [disabled]="!isSuperadmin">
            <option *ngFor="let option of activeOptions" [value]="option.value">{{ option.label }}</option>
          </select>
          <span class="error-text" *ngIf="showRequired('activo')">Este campo es obligatorio.</span>
        </label>
        <label class="field full-width">
          <span>Imagen (se redimensiona automaticamente)</span>
          <input type="file" accept="image/png, image/jpeg" (change)="onFileSelected($event)" />
          <span class="error-text" *ngIf="imageError">{{ imageError }}</span>
          <div class="image-preview" *ngIf="imagePreview || existingImagePath">
            <img [src]="imagePreview || imageUrl(existingImagePath)" alt="Vista previa" />
          </div>
        </label>
        <div class="form-actions">
          <button class="btn btn-primary" type="submit" [disabled]="form.invalid || !!imageError">Guardar</button>
          <button class="btn btn-ghost" type="button" (click)="back()">Cancelar</button>
        </div>
      </form>
    </div>
  `
})
export class DiscountsFormPageComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private discountsApi = inject(DiscountsApiService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);
  isEdit = false;
  discountId?: string;
  discountTypes = ['FIXED', 'PERCENTAGE'];
  activeOptions = [
    { label: 'Activo', value: 'true' },
    { label: 'Inactivo', value: 'false' }
  ];
  isSuperadmin = this.authService.hasRole(['SUPERADMINISTRADOR']);
  selectedImage?: File;
  imageError = '';
  imagePreview: string | null = null;
  existingImagePath: string | null = null;

  form = this.formBuilder.group({
    longDescription: ['', Validators.required],
    shortDescription: ['', Validators.required],
    nickname: ['', Validators.required],
    discountType: ['', Validators.required],
    value: [0, [Validators.required, Validators.min(0)]],
    activo: [{ value: 'true', disabled: !this.isSuperadmin }, Validators.required]
  });

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.discountId = id;
      this.discountsApi.getById(id).subscribe({
        next: (item) => {
          this.form.patchValue({
            longDescription: item.longDescription,
            shortDescription: item.shortDescription,
            nickname: item.nickname,
            discountType: item.discountType,
            value: item.value,
            activo: item.activo ? 'true' : 'false'
          });
          this.existingImagePath = item.imagePath ?? null;
        },
        error: () => this.snackBar.open('No se pudo cargar el descuento', 'Cerrar', { duration: 3000 })
      });
    }
  }

  onSubmit() {
    if (this.form.invalid || this.imageError) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }
    const value = this.form.getRawValue();
    const activeValue = value.activo === 'true';
    const payload: DiscountItemRequest = {
      longDescription: value.longDescription ?? '',
      shortDescription: value.shortDescription ?? '',
      nickname: value.nickname ?? '',
      discountType: value.discountType ?? '',
      value: Number(value.value ?? 0),
      activo: activeValue
    };

    const request$ = this.isEdit && this.discountId ? this.discountsApi.update(this.discountId, payload) : this.discountsApi.create(payload);

    request$.subscribe({
      next: (item) => {
        if (this.selectedImage) {
          this.discountsApi.uploadImage(item.id, this.selectedImage).subscribe({
            next: () => this.finishSave(),
            error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo subir la imagen', 'Cerrar', { duration: 3000 })
          });
        } else {
          this.finishSave();
        }
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo guardar', 'Cerrar', { duration: 3000 })
    });
  }

  back() {
    this.router.navigate(['/descuentos']);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }
    if (!['image/png', 'image/jpeg'].includes(file.type)) {
      this.setImageError('La imagen debe ser PNG o JPG.');
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      const image = new Image();
      image.onload = () => {
        this.imageError = '';
        this.selectedImage = file;
        this.imagePreview = reader.result as string;
      };
      image.onerror = () => this.setImageError('No se pudo leer la imagen.');
      image.src = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  isInvalid(control: string) {
    const field = this.form.get(control);
    return !!field && field.invalid && (field.dirty || field.touched);
  }

  showRequired(control: string) {
    const field = this.form.get(control);
    return !!field && field.hasError('required') && (field.dirty || field.touched);
  }

  private setImageError(message: string) {
    this.imageError = message;
    this.selectedImage = undefined;
    this.imagePreview = null;
  }

  private finishSave() {
    this.snackBar.open('Descuento guardado', 'Cerrar', { duration: 3000 });
    this.back();
  }

  imageUrl(path: string | null) {
    if (!path) {
      return '';
    }
    return `${environment.apiUrl}${path}`;
  }
}
