import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MesasApiService, MesaDto } from '../../core/services/mesas-api.service';

/**
 * Página con el listado de mesas.
 */
@Component({
  selector: 'app-mesas-list-page',
  standalone: true,
  imports: [CommonModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Configuración de mesas</h2>
        <p class="page-subtitle">Administra la distribución, las sillas y el estado de cada mesa.</p>
      </div>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <span class="table-title">Listado</span>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Descripción</th>
              <th>Sillas</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let mesa of mesas">
              <td>{{ mesa.description }}</td>
              <td>{{ mesa.seats }}</td>
              <td>
                <span class="badge" [class.badge-success]="mesa.status === 'ACTIVO'" [class.badge-muted]="mesa.status !== 'ACTIVO'">
                  {{ mesa.status === 'ACTIVO' ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
            </tr>
            <tr *ngIf="mesas.length === 0">
              <td colspan="3">
                <div class="empty-state">
                  <p>No hay mesas registradas.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class MesasListPageComponent implements OnInit {
  private mesasApi = inject(MesasApiService);
  private snackBar = inject(MatSnackBar);
  mesas: MesaDto[] = [];

  /**
   * Carga el listado inicial.
   */
  ngOnInit() {
    this.loadMesas();
  }

  /**
   * Solicita las mesas disponibles.
   */
  private loadMesas() {
    this.mesasApi.list().subscribe({
      next: (mesas) => (this.mesas = mesas),
      error: () => this.snackBar.open('Error cargando mesas', 'Cerrar', { duration: 3000 })
    });
  }
}
