import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UsersService, UserDto } from '../../core/services/users.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

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
                <span class="badge" [class.badge-success]="user.activo" [class.badge-muted]="!user.activo">
                  {{ user.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td class="text-right">
                <a class="btn btn-ghost btn-sm" [routerLink]="['/users', user.id]">Ver</a>
                <a class="btn btn-ghost btn-sm" [routerLink]="['/users', user.id, 'edit']">Editar</a>
                <button class="btn btn-ghost btn-sm" (click)="toggleDelete(user)">
                  {{ user.deleted ? 'Restaurar' : 'Eliminar' }}
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

  ngOnInit() {
    this.loadUsers();
  }

  get rangeStart() {
    return this.total === 0 ? 0 : this.pageIndex * this.pageSize + 1;
  }

  get rangeEnd() {
    return Math.min(this.total, (this.pageIndex + 1) * this.pageSize);
  }

  onPageSizeChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.pageSize = Number(target.value);
    this.pageIndex = 0;
    this.loadUsers();
  }

  goPrev() {
    if (this.pageIndex > 0) {
      this.pageIndex -= 1;
      this.loadUsers();
    }
  }

  goNext() {
    if (this.rangeEnd < this.total) {
      this.pageIndex += 1;
      this.loadUsers();
    }
  }

  loadUsers() {
    this.usersService.list({ page: this.pageIndex, size: this.pageSize }).subscribe({
      next: (page) => {
        this.users = page.content;
        this.total = page.totalElements;
      },
      error: () => this.snackBar.open('Error cargando usuarios', 'Cerrar', { duration: 3000 })
    });
  }

  toggleDelete(user: UserDto) {
    const action = user.deleted ? this.usersService.restore(user.id) : this.usersService.softDelete(user.id);
    action.subscribe({
      next: () => {
        this.snackBar.open(user.deleted ? 'Usuario restaurado' : 'Usuario eliminado', 'Cerrar', { duration: 3000 });
        this.loadUsers();
      },
      error: () => this.snackBar.open('No se pudo actualizar el usuario', 'Cerrar', { duration: 3000 })
    });
  }

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
