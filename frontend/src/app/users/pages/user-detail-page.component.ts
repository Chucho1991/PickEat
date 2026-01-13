import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UsersService, UserDto } from '../../core/services/users.service';

@Component({
  selector: 'app-user-detail-page',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Detalle de usuario</h2>
        <p class="page-subtitle">Informacion y estado de la cuenta.</p>
      </div>
      <a class="btn btn-ghost" routerLink="/users">Volver</a>
    </div>

    <div class="card" *ngIf="user">
      <div class="detail-grid">
        <div>
          <p class="detail-label">Nombre</p>
          <p class="detail-value">{{ user.nombres }}</p>
        </div>
        <div>
          <p class="detail-label">Correo</p>
          <p class="detail-value">{{ user.correo }}</p>
        </div>
        <div>
          <p class="detail-label">Usuario</p>
          <p class="detail-value">{{ user.username }}</p>
        </div>
        <div>
          <p class="detail-label">Rol</p>
          <span class="badge badge-info">{{ user.rol }}</span>
        </div>
        <div>
          <p class="detail-label">Activo</p>
          <span class="badge" [class.badge-success]="user.activo" [class.badge-muted]="!user.activo">
            {{ user.activo ? 'Si' : 'No' }}
          </span>
        </div>
        <div>
          <p class="detail-label">Eliminado</p>
          <span class="badge" [class.badge-warning]="user.deleted" [class.badge-muted]="!user.deleted">
            {{ user.deleted ? 'Si' : 'No' }}
          </span>
        </div>
      </div>
    </div>
  `
})
export class UserDetailPageComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private usersService = inject(UsersService);
  user?: UserDto;

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.usersService.getById(id).subscribe(user => (this.user = user));
    }
  }
}
