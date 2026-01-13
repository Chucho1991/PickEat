import { Component } from '@angular/core';

/**
 * Página principal del panel administrativo.
 */
@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Dashboard</h2>
        <p class="page-subtitle">Resumen general de actividad.</p>
      </div>
      <button class="btn btn-ghost">Exportar</button>
    </div>

    <div class="stat-grid">
      <div class="stat-card">
        <p class="stat-label">Ordenes del dia</p>
        <h3 class="stat-value">128</h3>
        <p class="stat-meta">+12% vs ayer</p>
      </div>
      <div class="stat-card">
        <p class="stat-label">Mesas activas</p>
        <h3 class="stat-value">34</h3>
        <p class="stat-meta">Turno tarde</p>
      </div>
      <div class="stat-card">
        <p class="stat-label">Ticket promedio</p>
        <h3 class="stat-value">$ 12.50</h3>
        <p class="stat-meta">Ultimas 24h</p>
      </div>
      <div class="stat-card">
        <p class="stat-label">Pedidos pendientes</p>
        <h3 class="stat-value">9</h3>
        <p class="stat-meta">Cocina</p>
      </div>
    </div>

    <div class="grid-2">
      <div class="card">
        <div class="card-header">
          <h3>Ventas por hora</h3>
          <span class="badge badge-info">Hoy</span>
        </div>
        <div class="chart-placeholder">
          <div class="chart-bar" style="height: 40%"></div>
          <div class="chart-bar" style="height: 55%"></div>
          <div class="chart-bar" style="height: 30%"></div>
          <div class="chart-bar" style="height: 70%"></div>
          <div class="chart-bar" style="height: 45%"></div>
          <div class="chart-bar" style="height: 60%"></div>
        </div>
      </div>
      <div class="card">
        <div class="card-header">
          <h3>Ultimas ordenes</h3>
          <button class="btn btn-primary btn-sm">Ver todo</button>
        </div>
        <div class="list">
          <div class="list-item">
            <div>
              <p class="list-title">Mesa 12</p>
              <p class="list-subtitle">2 platos - $ 24.00</p>
            </div>
            <span class="badge badge-success">Listo</span>
          </div>
          <div class="list-item">
            <div>
              <p class="list-title">Mesa 4</p>
              <p class="list-subtitle">3 platos - $ 32.00</p>
            </div>
            <span class="badge badge-warning">En cocina</span>
          </div>
          <div class="list-item">
            <div>
              <p class="list-title">Mesa 9</p>
              <p class="list-subtitle">1 plato - $ 9.50</p>
            </div>
            <span class="badge badge-info">Nuevo</span>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardPageComponent {}
