import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

/**
 * Contenedor del modulo de ordenes con submenus.
 */
@Component({
  selector: 'app-orders-shell-page',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Ordenes</h2>
        <p class="page-subtitle">Consulta el listado y registra nuevas ordenes.</p>
      </div>
    </div>

    <div class="subnav">
      <a class="subnav-link" routerLink="/ordenes/lista" routerLinkActive="active">Lista de ordenes</a>
      <a class="subnav-link" routerLink="/ordenes/toma" routerLinkActive="active">Toma de ordenes</a>
      <a class="subnav-link" routerLink="/ordenes/configuracion" routerLinkActive="active">Configuracion</a>
    </div>

    <router-outlet></router-outlet>
  `
})
export class OrdersShellPageComponent {}
