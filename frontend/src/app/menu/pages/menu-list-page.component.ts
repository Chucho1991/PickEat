import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MenuApiService, MenuItemDto } from '../../core/services/menu-api.service';
import { AuthService } from '../../core/services/auth.service';
import { environment } from '../../../environments/environment';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, takeUntil } from 'rxjs/operators';

/**
 * Pagina con el listado principal de items del menu.
 */
@Component({
  selector: 'app-menu-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Menu</h2>
        <p class="page-subtitle">Administra los platos disponibles.</p>
      </div>
      <a class="btn btn-primary" routerLink="/menu/new">Nuevo item</a>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <span class="table-title">Listado</span>
        <form class="table-actions" [formGroup]="filters" (ngSubmit)="applyFilters()">
          <label class="select">
            <span>Tipo</span>
            <select formControlName="dishType">
              <option value="">Todos</option>
              <option *ngFor="let type of dishTypes" [value]="type">{{ type }}</option>
            </select>
          </label>
          <label class="select">
            <span>Activo</span>
            <select formControlName="activo">
              <option value="">Todos</option>
              <option *ngFor="let option of activeOptions" [value]="option.value">{{ option.label }}</option>
            </select>
          </label>
          <label class="checkbox" *ngIf="isSuperadmin">
            <input type="checkbox" formControlName="includeDeleted" />
            <span>Incluir eliminados</span>
          </label>
          <label class="field inline-field">
            <span>Buscar</span>
            <input type="search" placeholder="Descripcion o pseudonimo" formControlName="search" />
          </label>
          <button class="btn btn-ghost btn-sm" type="button" (click)="clearFilters()">Limpiar</button>
        </form>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Miniatura</th>
              <th>Pseudonimo</th>
              <th>Descripcion corta</th>
              <th>Tipo</th>
              <th>Precio</th>
              <th>Activo</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let item of items">
              <td>
                <div class="thumbnail" [class.thumbnail-empty]="!item.imagePath">
                  <img *ngIf="item.imagePath" [src]="imageUrl(item.imagePath)" [alt]="item.nickname" />
                  <span *ngIf="!item.imagePath">Sin imagen</span>
                </div>
              </td>
              <td>{{ item.nickname }}</td>
              <td>{{ item.shortDescription }}</td>
              <td>{{ item.dishType }}</td>
              <td>{{ item.price | currency: 'USD':'symbol':'1.2-2' }}</td>
              <td>
                <span class="badge badge-danger" *ngIf="item.deleted; else statusBadge">Eliminado</span>
                <ng-template #statusBadge>
                  <span class="badge" [class.badge-success]="item.activo" [class.badge-muted]="!item.activo">
                    {{ item.activo ? 'Activo' : 'Inactivo' }}
                  </span>
                </ng-template>
              </td>
              <td class="text-right">
                <a class="btn btn-ghost btn-sm icon-btn" [routerLink]="['/menu', item.id, 'edit']" title="Editar" aria-label="Editar">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 17.25V20h2.75L18.5 8.25L15.75 5.5L4 17.25Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M13.5 6.5L17.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </a>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="toggleActive(item)" [disabled]="!isSuperadmin" [title]="item.activo ? 'Inactivar' : 'Activar'" [attr.aria-label]="item.activo ? 'Inactivar' : 'Activar'">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
                  </svg>
                </button>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="deleteItem(item)" title="Eliminar" aria-label="Eliminar">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 7H20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                    <path d="M10 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                    <path d="M14 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                    <path d="M6 7L7 19H17L18 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M9 7V5H15V7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </button>
              </td>
            </tr>
            <tr *ngIf="items.length === 0">
              <td colspan="7">
                <div class="empty-state">
                  <p>No hay items disponibles.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class MenuListPageComponent implements OnInit, OnDestroy {
  private menuApi = inject(MenuApiService);
  private snackBar = inject(MatSnackBar);
  private formBuilder = inject(FormBuilder);
  private authService = inject(AuthService);
  private destroy$ = new Subject<void>();
  items: MenuItemDto[] = [];
  dishTypes = ['ENTRADA', 'FUERTE', 'BEBIDA', 'OTRO', 'POSTRE', 'COMBO'];
  activeOptions = [
    { label: 'Activo', value: 'true' },
    { label: 'Inactivo', value: 'false' }
  ];
  isSuperadmin = this.authService.hasRole(['SUPERADMINISTRADOR']);

  filters = this.formBuilder.group({
    dishType: [''],
    activo: [''],
    search: [''],
    includeDeleted: [this.isSuperadmin]
  });

  /**
   * Carga la lista inicial.
   */
  ngOnInit() {
    this.loadItems();
    this.filters.valueChanges
      .pipe(
        debounceTime(300),
        map((value) => JSON.stringify(value ?? {})),
        distinctUntilChanged(),
        takeUntil(this.destroy$)
      )
      .subscribe(() => this.loadItems());
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Aplica filtros y recarga el listado.
   */
  applyFilters() {
    this.loadItems();
  }

  /**
   * Limpia los filtros y recarga el listado.
   */
  clearFilters() {
    this.filters.reset({ dishType: '', activo: '', search: '', includeDeleted: this.isSuperadmin });
    this.loadItems();
  }

  /**
   * Cambia el estado activo del item seleccionado.
   *
   * @param item item objetivo.
   */
  toggleActive(item: MenuItemDto) {
    const nextActive = !item.activo;
    this.menuApi.changeActive(item.id, nextActive).subscribe({
      next: () => {
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadItems();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Elimina el item de forma logica.
   *
   * @param item item objetivo.
   */
  deleteItem(item: MenuItemDto) {
    const confirmed = window.confirm(`Eliminar el item ${item.nickname}?`);
    if (!confirmed) {
      return;
    }
    this.menuApi.delete(item.id).subscribe({
      next: () => {
        this.snackBar.open('Item eliminado', 'Cerrar', { duration: 3000 });
        this.loadItems();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo eliminar', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Resuelve la URL publica de la imagen.
   *
   * @param imagePath ruta guardada.
   */
  imageUrl(imagePath: string) {
    if (!imagePath) {
      return '';
    }
    if (imagePath.startsWith('http')) {
      return imagePath;
    }
    return `${environment.apiUrl}${imagePath}`;
  }

  /**
   * Solicita los items con los filtros actuales.
   */
  private loadItems() {
    const value = this.filters.getRawValue();
    const activeValue = value.activo === '' ? undefined : value.activo === 'true';
    this.menuApi
      .list({
        dishType: value.dishType || undefined,
        activo: activeValue,
        search: value.search || undefined,
        includeDeleted: this.isSuperadmin && Boolean(value.includeDeleted)
      })
      .subscribe({
        next: (items) => (this.items = items),
        error: () => this.snackBar.open('Error cargando menu', 'Cerrar', { duration: 3000 })
      });
  }
}
