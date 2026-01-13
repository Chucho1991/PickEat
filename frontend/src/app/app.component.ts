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
      <div class="app-shell" [class.sidebar-collapsed]="isSidebarCollapsed">
        <aside class="sidebar">
          <div class="brand">
            <span class="brand-mark">P</span>
            <span class="brand-name">PickEat</span>
          </div>
          <nav class="nav">
            <a routerLink="/dashboard" routerLinkActive="active"><span class="nav-label">Dashboard</span></a>
            <a routerLink="/users" routerLinkActive="active" *ngIf="isAdmin()"><span class="nav-label">Usuarios</span></a>
            <a routerLink="/profile" routerLinkActive="active"><span class="nav-label">Perfil</span></a>
            <a routerLink="/mesas" routerLinkActive="active"><span class="nav-label">Mesas</span></a>
            <a routerLink="/menu" routerLinkActive="active"><span class="nav-label">Menu</span></a>
            <a routerLink="/ordenes" routerLinkActive="active"><span class="nav-label">Ordenes</span></a>
            <a routerLink="/despachador" routerLinkActive="active"><span class="nav-label">Despachador</span></a>
          </nav>
          <button class="btn btn-ghost sidebar-logout" (click)="logout()">Salir</button>
        </aside>
        <div class="app-body">
          <header class="topbar">
            <div class="topbar-left">
              <button class="btn btn-ghost btn-sm" type="button" (click)="toggleSidebar()" aria-label="Alternar menu">
                <span class="menu-icon">≡</span>
              </button>
              <div>
                <p class="topbar-title">Administracion</p>
                <p class="topbar-subtitle">Panel de control</p>
              </div>
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
  isSidebarCollapsed = false;
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

  toggleSidebar() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
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
