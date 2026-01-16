import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth.service';
import { DashboardAdminPageComponent } from './dashboard-admin-page.component';
import { DashboardMeseroPageComponent } from './dashboard-mesero-page.component';
import { DashboardDespachadorPageComponent } from './dashboard-despachador-page.component';

/**
 * Página principal del dashboard según rol.
 */
@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [
    CommonModule,
    DashboardAdminPageComponent,
    DashboardMeseroPageComponent,
    DashboardDespachadorPageComponent
  ],
  template: `
    <ng-container [ngSwitch]="role()">
      <app-dashboard-admin-page *ngSwitchCase="'SUPERADMINISTRADOR'"></app-dashboard-admin-page>
      <app-dashboard-admin-page *ngSwitchCase="'ADMINISTRADOR'"></app-dashboard-admin-page>
      <app-dashboard-mesero-page *ngSwitchCase="'MESERO'"></app-dashboard-mesero-page>
      <app-dashboard-despachador-page *ngSwitchCase="'DESPACHADOR'"></app-dashboard-despachador-page>
      <app-dashboard-admin-page *ngSwitchDefault></app-dashboard-admin-page>
    </ng-container>
  `
})
export class DashboardPageComponent {
  private authService = inject(AuthService);

  /**
   * Rol actual para seleccionar el dashboard correspondiente.
   */
  role = computed(() => this.authService.getUser()?.rol ?? '');
}
