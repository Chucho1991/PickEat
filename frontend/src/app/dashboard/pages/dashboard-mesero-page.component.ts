import { Component } from '@angular/core';

/**
 * Dashboard operativo para el rol de mesero.
 */
@Component({
  selector: 'app-dashboard-mesero-page',
  standalone: true,
  template: `
    <div>
      <div class="page-header">
        <div>
          <h2 class="page-title">Dashboard de mesero</h2>
          <p class="page-subtitle">Turno actual con métricas rápidas.</p>
        </div>
        <span class="badge badge-success">Turno activo</span>
      </div>

      <div class="stat-grid">
        <div class="stat-card">
          <p class="stat-label">Mesas asignadas</p>
          <h3 class="stat-value">12</h3>
          <p class="stat-meta">5 con pedidos nuevos</p>
        </div>
        <div class="stat-card">
          <p class="stat-label">Órdenes en curso</p>
          <h3 class="stat-value">8</h3>
          <p class="stat-meta">3 listas para entregar</p>
        </div>
        <div class="stat-card">
          <p class="stat-label">Propinas estimadas</p>
          <h3 class="stat-value">$ 46.80</h3>
          <p class="stat-meta">Últimas 6 horas</p>
        </div>
      </div>

      <div class="grid-2">
        <div class="card">
          <div class="card-header">
            <h3>Mesas prioritarias</h3>
            <span class="badge badge-muted">Dummy data</span>
          </div>
          <div class="list">
            <div class="list-item">
              <div>
                <p class="list-title">Mesa 7</p>
                <p class="list-subtitle">3 platos - Espera 12 min</p>
              </div>
              <span class="badge badge-warning">Pendiente</span>
            </div>
            <div class="list-item">
              <div>
                <p class="list-title">Mesa 15</p>
                <p class="list-subtitle">2 bebidas - Espera 6 min</p>
              </div>
              <span class="badge badge-info">En cocina</span>
            </div>
          </div>
        </div>
        <div class="card">
          <div class="card-header">
            <h3>Checklist de turno</h3>
            <span class="badge badge-muted">Actualizado</span>
          </div>
          <div class="list">
            <div class="list-item">
              <div>
                <p class="list-title">OK</p>
                <p class="list-subtitle">Briefing completado</p>
              </div>
              <span class="badge badge-success">Listo</span>
            </div>
            <div class="list-item">
              <div>
                <p class="list-title">Pendiente</p>
                <p class="list-subtitle">Actualizar estado de 5 mesas</p>
              </div>
              <span class="badge badge-warning">Por hacer</span>
            </div>
            <div class="list-item">
              <div>
                <p class="list-title">Pendiente</p>
                <p class="list-subtitle">Registrar cierre parcial</p>
              </div>
              <span class="badge badge-warning">Por hacer</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardMeseroPageComponent {}
