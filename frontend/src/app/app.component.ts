import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule, MatIconModule, MatSnackBarModule],
  template: `
    <mat-toolbar color="primary">
      <span>PickEat</span>
      <span class="spacer"></span>
      <ng-container *ngIf="isAuthenticated()">
        <a mat-button routerLink="/dashboard" *ngIf="isAdmin()">Dashboard</a>
        <a mat-button routerLink="/users" *ngIf="isAdmin()">Usuarios</a>
        <a mat-button routerLink="/profile">Perfil</a>
        <a mat-button routerLink="/mesas">Mesas</a>
        <a mat-button routerLink="/menu">Menú</a>
        <a mat-button routerLink="/ordenes">Órdenes</a>
        <a mat-button routerLink="/despachador">Despachador</a>
        <button mat-button (click)="logout()">Salir</button>
      </ng-container>
    </mat-toolbar>
    <div class="container">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [
    `
      .spacer {
        flex: 1 1 auto;
      }
      a[mat-button] {
        color: #fff;
      }
    `
  ]
})
export class AppComponent {
  private authService = inject(AuthService);
  isAuthenticated = computed(() => this.authService.isAuthenticated());
  isAdmin = computed(() => this.authService.hasRole(['SUPERADMINISTRADOR', 'ADMINISTRADOR']));

  logout() {
    this.authService.logout();
  }
}
