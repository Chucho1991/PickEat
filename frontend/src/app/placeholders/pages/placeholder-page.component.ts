import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-placeholder-page',
  standalone: true,
  imports: [MatCardModule],
  template: `
    <h2 class="page-title">{{ title }}</h2>
    <mat-card class="card">
      <p>Coming soon</p>
    </mat-card>
  `
})
export class PlaceholderPageComponent {
  title = this.route.snapshot.data['title'] || 'Módulo';

  constructor(private route: ActivatedRoute) {}
}
