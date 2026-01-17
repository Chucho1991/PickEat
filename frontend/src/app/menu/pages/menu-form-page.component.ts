import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MenuApiService, MenuItemRequest } from '../../core/services/menu-api.service';
import { environment } from '../../../environments/environment';

/**
 * Página de formulario para crear o editar ítems del menú.
 */
@Component({
  selector: 'app-menu-form-page',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">{{ isEdit ? 'Editar ítem' : 'Nuevo ítem' }}</h2>
        <p class="page-subtitle">Completa la información del menú.</p>
      </div>
      <button class="btn btn-ghost" type="button" (click)="back()">Volver</button>
    </div>

    <div class="card">
      <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form-grid">
        <label class="field full-width">
          <span>Descripción larga</span>
          <textarea rows="4" formControlName="longDescription" [class.invalid]="isInvalid('longDescription')"></textarea>
          <span class="error-text" *ngIf="showRequired('longDescription')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Descripción corta</span>
          <input type="text" formControlName="shortDescription" [class.invalid]="isInvalid('shortDescription')" />
          <span class="error-text" *ngIf="showRequired('shortDescription')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Pseudónimo</span>
          <input type="text" formControlName="nickname" [class.invalid]="isInvalid('nickname')" />
          <span class="error-text" *ngIf="showRequired('nickname')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Tipo de plato</span>
          <select formControlName="dishType" [class.invalid]="isInvalid('dishType')">
            <option value="" disabled>Selecciona un tipo</option>
            <option *ngFor="let type of dishTypes" [value]="type">{{ type }}</option>
          </select>
          <span class="error-text" *ngIf="showRequired('dishType')">Este campo es obligatorio.</span>
        </label>
        <label class="field">
          <span>Precio</span>
          <input type="number" step="0.01" min="0" formControlName="price" [class.invalid]="isInvalid('price')" />
          <span class="error-text" *ngIf="showRequired('price')">Este campo es obligatorio.</span>
          <span class="error-text" *ngIf="form.get('price')?.hasError('min')">Debe ser mayor o igual a 0.</span>
        </label>
        <label class="field">
          <span>Estado</span>
          <select formControlName="status" [class.invalid]="isInvalid('status')">
            <option *ngFor="let status of statusOptions" [value]="status">{{ status }}</option>
          </select>
          <span class="error-text" *ngIf="showRequired('status')">Este campo es obligatorio.</span>
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
export class MenuFormPageComponent implements OnInit {
  private formBuilder = inject(FormBuilder);
  private menuApi = inject(MenuApiService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  isEdit = false;
  menuId?: string;
  dishTypes = ['ENTRADA', 'FUERTE', 'BEBIDA', 'OTRO', 'POSTRE', 'COMBO'];
  statusOptions = ['ACTIVO', 'INACTIVO'];
  selectedImage?: File;
  imageError = '';
  imagePreview: string | null = null;
  existingImagePath: string | null = null;

  form = this.formBuilder.group({
    longDescription: ['', Validators.required],
    shortDescription: ['', Validators.required],
    nickname: ['', Validators.required],
    dishType: ['', Validators.required],
    price: [0, [Validators.required, Validators.min(0)]],
    status: ['ACTIVO', Validators.required]
  });

  /**
   * Inicializa el formulario.
   */
  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.menuId = id;
      this.menuApi.getById(id).subscribe({
        next: (item) => {
          this.form.patchValue({
            longDescription: item.longDescription,
            shortDescription: item.shortDescription,
            nickname: item.nickname,
            dishType: item.dishType,
            price: item.price,
            status: item.status
          });
          this.existingImagePath = item.imagePath ?? null;
        },
        error: () => this.snackBar.open('No se pudo cargar el ítem', 'Cerrar', { duration: 3000 })
      });
    }
  }

  /**
   * Envía el formulario para crear o actualizar.
   */
  onSubmit() {
    if (this.form.invalid || this.imageError) {
      this.form.markAllAsTouched();
      this.snackBar.open('Completa los campos requeridos', 'Cerrar', { duration: 3000 });
      return;
    }
    const value = this.form.getRawValue();
    const payload: MenuItemRequest = {
      longDescription: value.longDescription ?? '',
      shortDescription: value.shortDescription ?? '',
      nickname: value.nickname ?? '',
      dishType: value.dishType ?? '',
      price: Number(value.price ?? 0),
      status: value.status ?? 'ACTIVO'
    };

    const request$ = this.isEdit && this.menuId ? this.menuApi.update(this.menuId, payload) : this.menuApi.create(payload);

    request$.subscribe({
      next: (item) => {
        if (this.selectedImage) {
          this.menuApi.uploadImage(item.id, this.selectedImage).subscribe({
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

  /**
   * Regresa al listado del menú.
   */
  back() {
    this.router.navigate(['/menu']);
  }

  /**
   * Valida la selección de imagen.
   *
   * @param event evento del input.
   */
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

  /**
   * Determina si el control está inválido.
   *
   * @param control nombre del control.
   */
  isInvalid(control: string) {
    const field = this.form.get(control);
    return !!field && field.touched && field.invalid;
  }

  /**
   * Indica si se debe mostrar error de requerido.
   *
   * @param control nombre del control.
   */
  showRequired(control: string) {
    const field = this.form.get(control);
    return !!field && field.touched && field.hasError('required');
  }

  /**
   * Resuelve la URL pública de la imagen.
   *
   * @param imagePath ruta almacenada.
   */
  imageUrl(imagePath: string | null) {
    if (!imagePath) {
      return '';
    }
    if (imagePath.startsWith('http')) {
      return imagePath;
    }
    return `${environment.apiUrl}${imagePath}`;
  }

  /**
   * Finaliza el guardado y redirige.
   */
  private finishSave() {
    this.snackBar.open(this.isEdit ? 'Ítem actualizado' : 'Ítem creado', 'Cerrar', { duration: 3000 });
    this.router.navigate(['/menu']);
  }

  /**
   * Registra un error de imagen y limpia el preview.
   *
   * @param message mensaje de error.
   */
  private setImageError(message: string) {
    this.imageError = message;
    this.selectedImage = undefined;
    this.imagePreview = null;
  }
}
