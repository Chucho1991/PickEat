import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
        <div class="empty-icon">+</div>
        <p>Contenido disponible proximamente.</p>
      </div>
    </div>
  `
})
export class PlaceholderPageComponent {
  title = this.route.snapshot.data['title'] || 'Modulo';

  constructor(private route: ActivatedRoute) {}
}
