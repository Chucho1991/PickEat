import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MesasApiService, MesaDto } from '../../core/services/mesas-api.service';
import { AuthService } from '../../core/services/auth.service';

/**
 * Pagina con el listado de mesas.
 */
@Component({
  selector: 'app-mesas-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Configuracion de mesas</h2>
        <p class="page-subtitle">Administra la distribucion, las sillas y el estado de cada mesa.</p>
      </div>
      <a class="btn btn-primary" routerLink="/mesas/new">Nueva mesa</a>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <span class="table-title">Listado</span>
        <label class="checkbox" *ngIf="isSuperadmin">
          <input type="checkbox" [checked]="includeDeleted" (change)="toggleIncludeDeleted($event)" />
          <span>Incluir eliminados</span>
        </label>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Descripcion</th>
              <th>Sillas</th>
              <th>Estado</th>
              <th>Ocupacion</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let mesa of mesas">
              <td>{{ mesa.description }}</td>
              <td>{{ mesa.seats }}</td>
              <td>
                <span class="badge badge-danger" *ngIf="mesa.deleted; else activeBadge">Eliminado</span>
                <ng-template #activeBadge>
                  <span class="badge" [class.badge-success]="mesa.activo" [class.badge-muted]="!mesa.activo">
                    {{ mesa.activo ? 'Activo' : 'Inactivo' }}
                  </span>
                </ng-template>
              </td>
              <td>
                <span class="badge" [class.badge-danger]="mesa.ocupada" [class.badge-success]="!mesa.ocupada">
                  {{ mesa.ocupada ? 'Ocupada' : 'Libre' }}
                </span>
              </td>
              <td class="text-right">
                <a class="btn btn-ghost btn-sm icon-btn" [routerLink]="['/mesas', mesa.id, 'edit']" title="Editar" aria-label="Editar">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 17.25V20h2.75L18.5 8.25L15.75 5.5L4 17.25Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M13.5 6.5L17.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </a>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="toggleActive(mesa)" [disabled]="!isSuperadmin || mesa.deleted" [title]="mesa.activo ? 'Inactivar' : 'Activar'" [attr.aria-label]="mesa.activo ? 'Inactivar' : 'Activar'">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
                  </svg>
                </button>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="toggleDelete(mesa)" [title]="mesa.deleted ? 'Restaurar' : 'Eliminar'" [attr.aria-label]="mesa.deleted ? 'Restaurar' : 'Eliminar'">
                  <svg *ngIf="mesa.deleted; else deleteIcon" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 12C4 7.58172 7.58172 4 12 4C13.9576 4 15.7416 4.70456 17.1189 5.875M20 12C20 16.4183 16.4183 20 12 20C10.0424 20 8.2584 19.2954 6.88111 18.125" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M7 6V10H3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M17 14V18H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                  <ng-template #deleteIcon>
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M5 7H19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M9 7V5C9 4.44772 9.44772 4 10 4H14C14.5523 4 15 4.44772 15 5V7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M7 7L8 19C8.04605 19.5523 8.44772 20 9 20H15C15.5523 20 15.9539 19.5523 16 19L17 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                  </ng-template>
                </button>
              </td>
            </tr>
            <tr *ngIf="mesas.length === 0">
              <td colspan="5">
                <div class="empty-state">
                  <p>No hay mesas registradas.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class MesasListPageComponent implements OnInit {
  private mesasApi = inject(MesasApiService);
  private snackBar = inject(MatSnackBar);
  private authService = inject(AuthService);
  mesas: MesaDto[] = [];
  isSuperadmin = this.authService.hasRole(['SUPERADMINISTRADOR']);
  includeDeleted = this.isSuperadmin;

  ngOnInit() {
    this.loadMesas();
  }

  private loadMesas() {
    this.mesasApi.list(this.isSuperadmin ? this.includeDeleted : false).subscribe({
      next: (mesas) => (this.mesas = mesas),
      error: () => this.snackBar.open('Error cargando mesas', 'Cerrar', { duration: 3000 })
    });
  }

  toggleIncludeDeleted(event: Event) {
    const input = event.target as HTMLInputElement;
    this.includeDeleted = input.checked;
    this.loadMesas();
  }

  /**
   * Alterna entre eliminar y restaurar una mesa.
   *
   * @param mesa mesa objetivo.
   */
  toggleDelete(mesa: MesaDto) {
    if (!mesa.deleted && !confirm('Deseas eliminar esta mesa?')) {
      return;
    }
    if (mesa.deleted) {
      this.mesasApi.restore(mesa.id).subscribe({
        next: () => {
          this.snackBar.open('Mesa restaurada', 'Cerrar', { duration: 3000 });
          this.loadMesas();
        },
        error: (error: unknown) =>
          this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo restaurar la mesa', 'Cerrar', { duration: 3000 })
      });
      return;
    }

    this.mesasApi.delete(mesa.id).subscribe({
      next: () => {
        this.snackBar.open('Mesa eliminada', 'Cerrar', { duration: 3000 });
        this.loadMesas();
      },
      error: (error: unknown) =>
        this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo eliminar la mesa', 'Cerrar', { duration: 3000 })
    });
  }

  toggleActive(mesa: MesaDto) {
    this.mesasApi.changeActive(mesa.id, !mesa.activo).subscribe({
      next: () => {
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadMesas();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar', 'Cerrar', { duration: 3000 })
    });
  }
}
