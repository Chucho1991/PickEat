import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MenuApiService, MenuItemDto } from '../../core/services/menu-api.service';
import { environment } from '../../../environments/environment';

/**
 * Página con el listado principal de ítems del menú.
 */
@Component({
  selector: 'app-menu-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Menú</h2>
        <p class="page-subtitle">Administra los platos disponibles.</p>
      </div>
      <a class="btn btn-primary" routerLink="/menu/new">Nuevo ítem</a>
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
            <span>Estado</span>
            <select formControlName="status">
              <option value="">Todos</option>
              <option *ngFor="let status of statusOptions" [value]="status">{{ status }}</option>
            </select>
          </label>
          <label class="field inline-field">
            <span>Buscar</span>
            <input type="search" placeholder="Descripción o pseudónimo" formControlName="search" />
          </label>
          <button class="btn btn-ghost btn-sm" type="submit">Aplicar</button>
          <button class="btn btn-ghost btn-sm" type="button" (click)="clearFilters()">Limpiar</button>
        </form>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Miniatura</th>
              <th>Pseudónimo</th>
              <th>Descripción corta</th>
              <th>Tipo</th>
              <th>Precio</th>
              <th>Estado</th>
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
                <span class="badge" [class.badge-success]="item.status === 'ACTIVO'" [class.badge-muted]="item.status !== 'ACTIVO'">
                  {{ item.status === 'ACTIVO' ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td class="text-right">
                <a class="btn btn-ghost btn-sm icon-btn" [routerLink]="['/menu', item.id, 'edit']" title="Editar" aria-label="Editar">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 17.25V20h2.75L18.5 8.25L15.75 5.5L4 17.25Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M13.5 6.5L17.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </a>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="toggleStatus(item)" [title]="item.status === 'ACTIVO' ? 'Inactivar' : 'Activar'" [attr.aria-label]="item.status === 'ACTIVO' ? 'Inactivar' : 'Activar'">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
                  </svg>
                </button>
              </td>
            </tr>
            <tr *ngIf="items.length === 0">
              <td colspan="7">
                <div class="empty-state">
                  <p>No hay ítems disponibles.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class MenuListPageComponent implements OnInit {
  private menuApi = inject(MenuApiService);
  private snackBar = inject(MatSnackBar);
  private formBuilder = inject(FormBuilder);
  items: MenuItemDto[] = [];
  dishTypes = ['ENTRADA', 'FUERTE', 'BEBIDA', 'OTRO', 'POSTRE', 'COMBO'];
  statusOptions = ['ACTIVO', 'INACTIVO'];

  filters = this.formBuilder.group({
    dishType: [''],
    status: [''],
    search: ['']
  });

  /**
   * Carga la lista inicial.
   */
  ngOnInit() {
    this.loadItems();
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
    this.filters.reset({ dishType: '', status: '', search: '' });
    this.loadItems();
  }

  /**
   * Cambia el estado del ítem seleccionado.
   *
   * @param item ítem objetivo.
   */
  toggleStatus(item: MenuItemDto) {
    const nextStatus = item.status === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    this.menuApi.changeStatus(item.id, nextStatus).subscribe({
      next: () => {
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadItems();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Resuelve la URL pública de la imagen.
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
   * Solicita los ítems con los filtros actuales.
   */
  private loadItems() {
    const value = this.filters.getRawValue();
    this.menuApi
      .list({
        dishType: value.dishType || undefined,
        status: value.status || undefined,
        search: value.search || undefined
      })
      .subscribe({
        next: (items) => (this.items = items),
        error: () => this.snackBar.open('Error cargando menú', 'Cerrar', { duration: 3000 })
      });
  }
}
