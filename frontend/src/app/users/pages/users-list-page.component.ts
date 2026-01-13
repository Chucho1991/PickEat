import { Component, inject, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { RouterLink } from '@angular/router';
import { UsersService, UserDto } from '../../core/services/users.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-users-list-page',
  standalone: true,
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatPaginatorModule, RouterLink, MatSnackBarModule],
  template: `
    <div class="page-header">
      <h2 class="page-title">Usuarios</h2>
      <a mat-raised-button color="primary" routerLink="/users/new">Nuevo usuario</a>
    </div>
    <table mat-table [dataSource]="users" class="mat-elevation-z1">
      <ng-container matColumnDef="nombres">
        <th mat-header-cell *matHeaderCellDef>Nombre</th>
        <td mat-cell *matCellDef="let user">{{ user.nombres }}</td>
      </ng-container>
      <ng-container matColumnDef="username">
        <th mat-header-cell *matHeaderCellDef>Username</th>
        <td mat-cell *matCellDef="let user">{{ user.username }}</td>
      </ng-container>
      <ng-container matColumnDef="correo">
        <th mat-header-cell *matHeaderCellDef>Correo</th>
        <td mat-cell *matCellDef="let user">{{ user.correo }}</td>
      </ng-container>
      <ng-container matColumnDef="rol">
        <th mat-header-cell *matHeaderCellDef>Rol</th>
        <td mat-cell *matCellDef="let user">{{ user.rol }}</td>
      </ng-container>
      <ng-container matColumnDef="estado">
        <th mat-header-cell *matHeaderCellDef>Estado</th>
        <td mat-cell *matCellDef="let user">{{ user.activo ? 'Activo' : 'Inactivo' }}</td>
      </ng-container>
      <ng-container matColumnDef="acciones">
        <th mat-header-cell *matHeaderCellDef>Acciones</th>
        <td mat-cell *matCellDef="let user">
          <a mat-icon-button color="primary" [routerLink]="['/users', user.id]" aria-label="Ver">
            <mat-icon>visibility</mat-icon>
          </a>
          <a mat-icon-button color="accent" [routerLink]="['/users', user.id, 'edit']" aria-label="Editar">
            <mat-icon>edit</mat-icon>
          </a>
          <button mat-icon-button color="warn" (click)="toggleDelete(user)">
            <mat-icon>{{ user.deleted ? 'restore' : 'delete' }}</mat-icon>
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns"></tr>
    </table>
    <mat-paginator [length]="total" [pageSize]="pageSize" (page)="onPage($event)"></mat-paginator>
  `,
  styles: [
    `
      .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;
      }
      table {
        width: 100%;
        margin-bottom: 12px;
        background: white;
      }
    `
  ]
})
export class UsersListPageComponent implements OnInit {
  private usersService = inject(UsersService);
  private snackBar = inject(MatSnackBar);
  users: UserDto[] = [];
  displayedColumns = ['nombres', 'username', 'correo', 'rol', 'estado', 'acciones'];
  total = 0;
  pageSize = 10;
  pageIndex = 0;

  ngOnInit() {
    this.loadUsers();
  }

  onPage(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.loadUsers();
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
}
