import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UsersService, UserDto } from '../../core/services/users.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

/**
 * Página con el listado de usuarios y acciones principales.
 */
@Component({
  selector: 'app-users-list-page',
  standalone: true,
  imports: [CommonModule, RouterLink, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Usuarios</h2>
        <p class="page-subtitle">Gestiona el equipo y sus permisos.</p>
      </div>
      <a class="btn btn-primary" routerLink="/users/new">Nuevo usuario</a>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <span class="table-title">Listado</span>
        <div class="table-actions">
          <label class="select">
            <span>Mostrar</span>
            <select [value]="pageSize" (change)="onPageSizeChange($event)">
              <option *ngFor="let size of pageSizes" [value]="size">{{ size }}</option>
            </select>
          </label>
        </div>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Usuario</th>
              <th>Correo</th>
              <th>Rol</th>
              <th>Estado</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let user of users">
              <td>
                <div class="cell-main">
                  <span class="avatar">{{ initials(user.nombres) }}</span>
                  <div>
                    <p class="cell-title">{{ user.nombres }}</p>
                    <p class="cell-subtitle">ID {{ user.id | slice:0:8 }}</p>
                  </div>
                </div>
              </td>
              <td>{{ user.username }}</td>
              <td>{{ user.correo }}</td>
              <td><span class="badge badge-info">{{ user.rol }}</span></td>
              <td>
                <span class="badge badge-danger" *ngIf="user.deleted; else activeBadge">Eliminado</span>
                <ng-template #activeBadge>
                  <span class="badge" [class.badge-success]="user.activo" [class.badge-muted]="!user.activo">
                    {{ user.activo ? 'Activo' : 'Inactivo' }}
                  </span>
                </ng-template>
              </td>
              <td class="text-right">
                <a class="btn btn-ghost btn-sm icon-btn" [routerLink]="['/users', user.id]" title="Ver" aria-label="Ver">
                  <svg viewBox="0 0 21 20" fill="none" aria-hidden="true">
                    <path d="M2.96487 10.7925C2.73306 10.2899 2.73306 9.71023 2.96487 9.20764C4.28084 6.35442 7.15966 4.375 10.4993 4.375C13.8389 4.375 16.7178 6.35442 18.0337 9.20765C18.2655 9.71024 18.2655 10.2899 18.0337 10.7925C16.7178 13.6458 13.8389 15.6252 10.4993 15.6252C7.15966 15.6252 4.28084 13.6458 2.96487 10.7925Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M13.5202 10C13.5202 11.6684 12.1677 13.0208 10.4993 13.0208C8.83099 13.0208 7.47852 11.6684 7.47852 10C7.47852 8.33164 8.83099 6.97917 10.4993 6.97917C12.1677 6.97917 13.5202 8.33164 13.5202 10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </a>
                <a class="btn btn-ghost btn-sm icon-btn" [routerLink]="['/users', user.id, 'edit']" title="Editar" aria-label="Editar">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 17.25V20h2.75L18.5 8.25L15.75 5.5L4 17.25Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M13.5 6.5L17.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                </a>
                <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="toggleActive(user)" [disabled]="user.deleted" [title]="user.activo ? 'Inactivar' : 'Activar'" [attr.aria-label]="user.activo ? 'Inactivar' : 'Activar'">
                  <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
                  </svg>
                </button>
                <button class="btn btn-ghost btn-sm icon-btn" (click)="toggleDelete(user)" [title]="user.deleted ? 'Restaurar' : 'Eliminar'" [attr.aria-label]="user.deleted ? 'Restaurar' : 'Eliminar'">
                  <svg *ngIf="user.deleted; else deleteIcon" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 12C4 7.58172 7.58172 4 12 4C13.9576 4 15.7416 4.70456 17.1189 5.875M20 12C20 16.4183 16.4183 20 12 20C10.0424 20 8.2584 19.2954 6.88111 18.125" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M7 6V10H3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M17 14V18H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                  <ng-template #deleteIcon>
                    <svg viewBox="0 0 20 20" fill="none" aria-hidden="true">
                      <path fill-rule="evenodd" clip-rule="evenodd" d="M6.54142 3.7915C6.54142 2.54886 7.54878 1.5415 8.79142 1.5415H11.2081C12.4507 1.5415 13.4581 2.54886 13.4581 3.7915V4.0415H15.6252H16.666C17.0802 4.0415 17.416 4.37729 17.416 4.7915C17.416 5.20572 17.0802 5.5415 16.666 5.5415H16.3752V8.24638V13.2464V16.2082C16.3752 17.4508 15.3678 18.4582 14.1252 18.4582H5.87516C4.63252 18.4582 3.62516 17.4508 3.62516 16.2082V13.2464V8.24638V5.5415H3.3335C2.91928 5.5415 2.5835 5.20572 2.5835 4.7915C2.5835 4.37729 2.91928 4.0415 3.3335 4.0415H4.37516H6.54142V3.7915ZM14.8752 13.2464V8.24638V5.5415H13.4581H12.7081H7.29142H6.54142H5.12516V8.24638V13.2464V16.2082C5.12516 16.6224 5.46095 16.9582 5.87516 16.9582H14.1252C14.5394 16.9582 14.8752 16.6224 14.8752 16.2082V13.2464ZM8.04142 4.0415H11.9581V3.7915C11.9581 3.37729 11.6223 3.0415 11.2081 3.0415H8.79142C8.37721 3.0415 8.04142 3.37729 8.04142 3.7915V4.0415ZM8.3335 7.99984C8.74771 7.99984 9.0835 8.33562 9.0835 8.74984V13.7498C9.0835 14.1641 8.74771 14.4998 8.3335 14.4998C7.91928 14.4998 7.5835 14.1641 7.5835 13.7498V8.74984C7.5835 8.33562 7.91928 7.99984 8.3335 7.99984ZM12.4168 8.74984C12.4168 8.33562 12.081 7.99984 11.6668 7.99984C11.2526 7.99984 10.9168 8.33562 10.9168 8.74984V13.7498C10.9168 14.1641 11.2526 14.4998 11.6668 14.4998C12.081 14.4998 12.4168 14.1641 12.4168 13.7498V8.74984Z" fill="currentColor"></path>
                    </svg>
                  </ng-template>
                </button>
              </td>
            </tr>
            <tr *ngIf="users.length === 0">
              <td colspan="6">
                <div class="empty-state">
                  <p>No hay usuarios para mostrar.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="table-footer">
        <span>Mostrando {{ rangeStart }} - {{ rangeEnd }} de {{ total }}</span>
        <div class="pager">
          <button class="btn btn-ghost btn-sm" (click)="goPrev()" [disabled]="pageIndex === 0">Anterior</button>
          <button class="btn btn-ghost btn-sm" (click)="goNext()" [disabled]="rangeEnd >= total">Siguiente</button>
        </div>
      </div>
    </div>
  `
})
export class UsersListPageComponent implements OnInit {
  private usersService = inject(UsersService);
  private snackBar = inject(MatSnackBar);
  users: UserDto[] = [];
  total = 0;
  pageSize = 10;
  pageIndex = 0;
  pageSizes = [5, 10, 20, 50];

  /**
   * Carga el listado inicial de usuarios.
   */
  ngOnInit() {
    this.loadUsers();
  }

  /**
   * Rango inicial de la paginación actual.
   */
  get rangeStart() {
    return this.total === 0 ? 0 : this.pageIndex * this.pageSize + 1;
  }

  /**
   * Rango final de la paginación actual.
   */
  get rangeEnd() {
    return Math.min(this.total, (this.pageIndex + 1) * this.pageSize);
  }

  /**
   * Actualiza el tamaño de página y recarga la lista.
   *
   * @param event evento del selector.
   */
  onPageSizeChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.pageSize = Number(target.value);
    this.pageIndex = 0;
    this.loadUsers();
  }

  /**
   * Retrocede una página en la paginación.
   */
  goPrev() {
    if (this.pageIndex > 0) {
      this.pageIndex -= 1;
      this.loadUsers();
    }
  }

  /**
   * Avanza una página en la paginación.
   */
  goNext() {
    if (this.rangeEnd < this.total) {
      this.pageIndex += 1;
      this.loadUsers();
    }
  }

  /**
   * Solicita los usuarios según el estado actual de paginación.
   */
  loadUsers() {
    this.usersService.list({ page: this.pageIndex, size: this.pageSize }).subscribe({
      next: (page) => {
        this.users = page.content;
        this.total = page.totalElements;
      },
      error: () => this.snackBar.open('Error cargando usuarios', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Alterna entre eliminar y restaurar un usuario.
   *
   * @param user usuario objetivo.
   */
  toggleDelete(user: UserDto) {
    if (!user.deleted && !confirm('Deseas eliminar este usuario?')) {
      return;
    }
    const action = user.deleted ? this.usersService.restore(user.id) : this.usersService.softDelete(user.id);
    action.subscribe({
      next: () => {
        this.snackBar.open(user.deleted ? 'Usuario restaurado' : 'Usuario eliminado', 'Cerrar', { duration: 3000 });
        this.loadUsers();
      },
      error: () => this.snackBar.open('No se pudo actualizar el usuario', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Alterna el estado activo/inactivo.
   *
   * @param user usuario objetivo.
   */
  toggleActive(user: UserDto) {
    const payload = {
      nombres: user.nombres,
      correo: user.correo,
      username: user.username,
      rol: user.rol,
      activo: !user.activo
    };
    this.usersService.update(user.id, payload).subscribe({
      next: () => {
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadUsers();
      },
      error: () => this.snackBar.open('No se pudo actualizar el usuario', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Obtiene las iniciales para el avatar.
   *
   * @param name nombre completo.
   */
  initials(name: string) {
    return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part[0])
      .join('')
      .toUpperCase();
  }
}
