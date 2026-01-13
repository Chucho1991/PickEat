import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { UsersService, UserDto } from '../../core/services/users.service';

@Component({
  selector: 'app-user-detail-page',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, RouterLink],
  template: `
    <h2 class="page-title">Detalle de usuario</h2>
    <mat-card class="card" *ngIf="user">
      <p><strong>Nombre:</strong> {{ user.nombres }}</p>
      <p><strong>Correo:</strong> {{ user.correo }}</p>
      <p><strong>Username:</strong> {{ user.username }}</p>
      <p><strong>Rol:</strong> {{ user.rol }}</p>
      <p><strong>Activo:</strong> {{ user.activo ? 'Sí' : 'No' }}</p>
      <p><strong>Eliminado:</strong> {{ user.deleted ? 'Sí' : 'No' }}</p>
      <button mat-raised-button color="primary" routerLink="/users">Volver</button>
    </mat-card>
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
