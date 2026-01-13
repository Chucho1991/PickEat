import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [MatCardModule],
  template: `
    <h2 class="page-title">Dashboard</h2>
    <mat-card class="card">
      <p>Métricas generales (solo ADMIN/SUPERADMIN).</p>
      <p>Contenido en construcción.</p>
    </mat-card>
  `
})
export class DashboardPageComponent {}
