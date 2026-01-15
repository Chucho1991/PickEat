import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

/**
 * Página genérica para secciones en construcción.
 */
@Component({
  selector: 'app-placeholder-page',
  standalone: true,
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">{{ title }}</h2>
        <p class="page-subtitle">Esta seccion esta en preparacion.</p>
      </div>
    </div>
    <div class="card empty-card">
      <div class="empty-state">
        <div class="empty-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
            <path d="M12 5V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
            <path d="M5 12H19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
          </svg>
        </div>
        <p>Contenido disponible proximamente.</p>
      </div>
    </div>
  `
})
export class PlaceholderPageComponent {
  title = this.route.snapshot.data['title'] || 'Modulo';

  /**
   * Crea el componente con acceso a la ruta activa.
   *
   * @param route ruta activa para leer metadatos.
   */
  constructor(private route: ActivatedRoute) {}
}
