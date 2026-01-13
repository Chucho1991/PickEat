import { Component, computed, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <ng-container *ngIf="isAuthenticated(); else authOnly">
      <div class="app-shell">
        <aside class="sidebar">
          <div class="brand">
            <span class="brand-mark">P</span>
            <span class="brand-name">PickEat</span>
          </div>
          <nav class="nav">
            <a routerLink="/dashboard" routerLinkActive="active">Dashboard</a>
            <a routerLink="/users" routerLinkActive="active" *ngIf="isAdmin()">Usuarios</a>
            <a routerLink="/profile" routerLinkActive="active">Perfil</a>
            <a routerLink="/mesas" routerLinkActive="active">Mesas</a>
            <a routerLink="/menu" routerLinkActive="active">Menu</a>
            <a routerLink="/ordenes" routerLinkActive="active">Ordenes</a>
            <a routerLink="/despachador" routerLinkActive="active">Despachador</a>
          </nav>
          <button class="btn btn-ghost sidebar-logout" (click)="logout()">Salir</button>
        </aside>
        <div class="app-body">
          <header class="topbar">
            <div>
              <p class="topbar-title">Administracion</p>
              <p class="topbar-subtitle">Panel de control</p>
            </div>
            <div class="topbar-user">
              <div class="avatar">{{ userInitials() }}</div>
              <div>
                <p class="user-name">{{ userName() }}</p>
                <p class="user-role">{{ userRole() }}</p>
              </div>
            </div>
          </header>
          <main class="content">
            <router-outlet></router-outlet>
          </main>
        </div>
      </div>
    </ng-container>
    <ng-template #authOnly>
      <router-outlet></router-outlet>
    </ng-template>
  `,
  styles: [
    `
      :host {
        display: block;
      }
    `
  ]
})
export class AppComponent {
  private authService = inject(AuthService);
  isAuthenticated = computed(() => this.authService.isAuthenticated());
  isAdmin = computed(() => this.authService.hasRole(['SUPERADMINISTRADOR', 'ADMINISTRADOR']));
  userName = computed(() => {
    this.authService.isAuthenticated();
    return this.authService.getUser()?.nombres ?? 'Usuario';
  });
  userRole = computed(() => {
    this.authService.isAuthenticated();
    return this.authService.getUser()?.rol ?? 'Rol';
  });

  logout() {
    this.authService.logout();
  }

  userInitials() {
    const name = this.userName();
    return name
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((part) => part[0])
      .join('')
      .toUpperCase();
  }
}
