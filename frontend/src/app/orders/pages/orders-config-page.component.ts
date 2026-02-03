import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { OrderBillingFieldDto, OrderChannelDto, OrdersApiService } from '../../core/services/orders-api.service';

/**
 * Pagina para configurar parametros de ordenes.
 */
@Component({
  selector: 'app-orders-config-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, MatSnackBarModule],
  template: `
    <div class="card">
      <div class="page-header">
        <div>
          <h3 class="section-title">Configuracion de ordenes</h3>
          <p class="section-subtitle">Define impuestos y propina en porcentaje.</p>
        </div>
        <button class="btn btn-primary" type="button" (click)="save()" [disabled]="form.invalid">Guardar</button>
      </div>

      <form class="form-grid" [formGroup]="form">
        <label class="field">
          <span>Porcentaje de impuesto</span>
          <input type="number" min="0" max="100" step="0.01" formControlName="taxRate" />
          <span class="error-text" *ngIf="form.controls.taxRate.invalid && form.controls.taxRate.touched">
            Ingresa un valor entre 0 y 100.
          </span>
        </label>

        <label class="field">
          <span>Porcentaje de propina</span>
          <input type="number" min="0" max="100" step="0.01" formControlName="tipValue" />
          <span class="error-text" *ngIf="form.controls.tipValue.invalid && form.controls.tipValue.touched">
            Ingresa un valor entre 0 y 100.
          </span>
        </label>
      </form>
    </div>

    <div class="card">
      <div class="page-header">
        <div>
          <h3 class="section-title">Canales de pedido</h3>
          <p class="section-subtitle">Administra los canales disponibles para las ordenes.</p>
        </div>
      </div>

      <div class="table-toolbar">
        <span class="table-title">Listado</span>
        <label class="checkbox">
          <input type="checkbox" [(ngModel)]="includeDeleted" [ngModelOptions]="{ standalone: true }" (change)="loadChannels()" />
          <span>Incluir eliminados</span>
        </label>
      </div>

      <div class="form-grid">
        <label class="field full-width">
          <span>Nuevo canal</span>
          <div class="channel-input">
            <input type="text" [(ngModel)]="newChannelName" [ngModelOptions]="{ standalone: true }" placeholder="Nombre del canal" />
            <button class="btn btn-primary" type="button" (click)="addChannel()">Agregar</button>
          </div>
        </label>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Canal</th>
              <th>Estado</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let channel of channels">
              <td>
                <div class="cell-main">
                  <div>
                    <p class="cell-title">{{ channel.name }}</p>
                    <p class="cell-subtitle" *ngIf="channel.isDefault">Por defecto</p>
                  </div>
                </div>
              </td>
              <td>
                <span class="badge badge-danger" *ngIf="channel.deleted; else activeBadge">Eliminado</span>
                <ng-template #activeBadge>
                  <span class="badge" [class.badge-success]="channel.activo" [class.badge-muted]="!channel.activo">
                    {{ channel.activo ? 'Activo' : 'Inactivo' }}
                  </span>
                </ng-template>
              </td>
              <td class="text-right">
                <button
                  class="btn btn-ghost btn-sm icon-btn"
                  type="button"
                  (click)="toggleChannelDelete(channel)"
                  [disabled]="channel.locked"
                  [title]="channel.deleted ? 'Restaurar' : 'Eliminar'"
                  [attr.aria-label]="channel.deleted ? 'Restaurar' : 'Eliminar'"
                >
                  <svg *ngIf="channel.deleted; else deleteIcon" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 12C4 7.58172 7.58172 4 12 4C13.9576 4 15.7416 4.70456 17.1189 5.875M20 12C20 16.4183 16.4183 20 12 20C10.0424 20 8.2584 19.2954 6.88111 18.125" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M7 6V10H3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M17 14V18H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                  <ng-template #deleteIcon>
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M4 7H20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M10 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M14 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M6 7L7 19H17L18 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                      <path d="M9 7V5H15V7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                  </ng-template>
                </button>
              </td>
            </tr>
            <tr *ngIf="channels.length === 0">
              <td colspan="3">
                <div class="empty-state">
                  <p>No hay canales configurados.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="card">
      <div class="page-header">
        <div>
          <h3 class="section-title">Campos de facturacion</h3>
          <p class="section-subtitle">Configura los datos adicionales para facturacion.</p>
        </div>
      </div>

      <div class="table-toolbar">
        <span class="table-title">Listado</span>
        <label class="checkbox">
          <input type="checkbox" [(ngModel)]="includeBillingDeleted" [ngModelOptions]="{ standalone: true }" (change)="loadBillingFields()" />
          <span>Incluir eliminados</span>
        </label>
      </div>

      <div class="form-grid">
        <label class="field full-width">
          <span>Nuevo campo</span>
          <div class="channel-input">
            <input type="text" [(ngModel)]="newBillingLabel" [ngModelOptions]="{ standalone: true }" placeholder="Nombre del campo" />
            <input type="number" min="0" [(ngModel)]="newBillingSortOrder" [ngModelOptions]="{ standalone: true }" placeholder="Orden" />
            <button class="btn btn-primary" type="button" (click)="addBillingField()">Agregar</button>
          </div>
        </label>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Campo</th>
              <th>Orden</th>
              <th>Estado</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let field of billingFields">
              <td>
                <input
                  class="w-full rounded-lg border border-[var(--border)] bg-[var(--input-bg)] px-2 py-1 text-sm"
                  type="text"
                  [(ngModel)]="field.label"
                  [ngModelOptions]="{ standalone: true }"
                />
              </td>
              <td>
                <input
                  class="w-full rounded-lg border border-[var(--border)] bg-[var(--input-bg)] px-2 py-1 text-sm"
                  type="number"
                  min="0"
                  [(ngModel)]="field.sortOrder"
                  [ngModelOptions]="{ standalone: true }"
                />
              </td>
              <td>
                <span class="badge badge-danger" *ngIf="field.deleted; else activeField">Eliminado</span>
                <ng-template #activeField>
                  <label class="checkbox">
                    <input type="checkbox" [(ngModel)]="field.active" [ngModelOptions]="{ standalone: true }" />
                    <span>{{ field.active ? 'Activo' : 'Inactivo' }}</span>
                  </label>
                </ng-template>
              </td>
              <td class="text-right">
                <button class="btn btn-ghost btn-sm" type="button" (click)="updateBillingField(field)" [disabled]="field.deleted">
                  Guardar
                </button>
                <button
                  class="btn btn-ghost btn-sm icon-btn"
                  type="button"
                  (click)="toggleBillingDelete(field)"
                  [title]="field.deleted ? 'Restaurar' : 'Eliminar'"
                  [attr.aria-label]="field.deleted ? 'Restaurar' : 'Eliminar'"
                >
                  <svg *ngIf="field.deleted; else deleteIconBilling" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                    <path d="M4 12C4 7.58172 7.58172 4 12 4C13.9576 4 15.7416 4.70456 17.1189 5.875M20 12C20 16.4183 16.4183 20 12 20C10.0424 20 8.2584 19.2954 6.88111 18.125" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M7 6V10H3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    <path d="M17 14V18H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  </svg>
                  <ng-template #deleteIconBilling>
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M4 7H20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M10 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M14 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M6 7L7 19H17L18 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                      <path d="M9 7V5H15V7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                  </ng-template>
                </button>
              </td>
            </tr>
            <tr *ngIf="billingFields.length === 0">
              <td colspan="4">
                <div class="empty-state">
                  <p>No hay campos configurados.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class OrdersConfigPageComponent implements OnInit {
  private ordersApi = inject(OrdersApiService);
  private snackBar = inject(MatSnackBar);
  private formBuilder = inject(FormBuilder);

  channels: OrderChannelDto[] = [];
  newChannelName = '';
  includeDeleted = false;
  billingFields: OrderBillingFieldDto[] = [];
  includeBillingDeleted = false;
  newBillingLabel = '';
  newBillingSortOrder = 0;

  form = this.formBuilder.group({
    taxRate: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
    tipValue: [0, [Validators.required, Validators.min(0), Validators.max(100)]]
  });

  /**
   * Carga la configuracion actual.
   */
  ngOnInit() {
    this.ordersApi.getConfig().subscribe({
      next: (config) => {
        this.form.patchValue({
          taxRate: config.taxRate,
          tipValue: config.tipValue
        });
      },
      error: () => this.snackBar.open('No se pudo cargar la configuracion', 'Cerrar', { duration: 3000 })
    });
    this.loadChannels();
    this.loadBillingFields();
  }

  /**
   * Guarda la configuracion actual.
   */
  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const value = this.form.getRawValue();
    this.ordersApi.updateConfig({ taxRate: Number(value.taxRate), tipValue: Number(value.tipValue) }).subscribe({
      next: () => this.snackBar.open('Configuracion actualizada', 'Cerrar', { duration: 3000 }),
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar la configuracion', 'Cerrar', { duration: 3000 })
    });
  }

  loadChannels() {
    this.ordersApi.listChannels(this.includeDeleted).subscribe({
      next: (channels) => (this.channels = channels),
      error: () => this.snackBar.open('No se pudo cargar canales', 'Cerrar', { duration: 3000 })
    });
  }

  addChannel() {
    const name = this.newChannelName.trim();
    if (!name) {
      this.snackBar.open('El nombre es obligatorio', 'Cerrar', { duration: 3000 });
      return;
    }
    this.ordersApi.createChannel({ name }).subscribe({
      next: () => {
        this.newChannelName = '';
        this.snackBar.open('Canal creado', 'Cerrar', { duration: 3000 });
        this.loadChannels();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo crear el canal', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Carga los campos de facturacion configurados.
   */
  loadBillingFields() {
    this.ordersApi.listBillingFields(this.includeBillingDeleted).subscribe({
      next: (fields) => (this.billingFields = fields),
      error: () => this.snackBar.open('No se pudo cargar campos de facturacion', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Agrega un nuevo campo de facturacion.
   */
  addBillingField() {
    const label = this.newBillingLabel.trim();
    if (!label) {
      this.snackBar.open('El nombre es obligatorio', 'Cerrar', { duration: 3000 });
      return;
    }
    this.ordersApi.createBillingField({ label, sortOrder: Number(this.newBillingSortOrder || 0) }).subscribe({
      next: () => {
        this.newBillingLabel = '';
        this.newBillingSortOrder = 0;
        this.snackBar.open('Campo creado', 'Cerrar', { duration: 3000 });
        this.loadBillingFields();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo crear el campo', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Actualiza un campo de facturacion.
   *
   * @param field campo objetivo.
   */
  updateBillingField(field: OrderBillingFieldDto) {
    this.ordersApi
      .updateBillingField(field.id, { label: field.label, sortOrder: Number(field.sortOrder || 0), active: field.active })
      .subscribe({
        next: () => this.snackBar.open('Campo actualizado', 'Cerrar', { duration: 3000 }),
        error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar el campo', 'Cerrar', { duration: 3000 })
      });
  }

  /**
   * Alterna eliminar o restaurar un campo de facturacion.
   *
   * @param field campo objetivo.
   */
  toggleBillingDelete(field: OrderBillingFieldDto) {
    if (!field.deleted && !confirm(`Eliminar el campo ${field.label}?`)) {
      return;
    }
    if (field.deleted) {
      this.ordersApi.restoreBillingField(field.id).subscribe({
        next: () => {
          this.snackBar.open('Campo restaurado', 'Cerrar', { duration: 3000 });
          this.loadBillingFields();
        },
        error: (error: unknown) =>
          this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo restaurar', 'Cerrar', { duration: 3000 })
      });
      return;
    }
    this.ordersApi.deleteBillingField(field.id).subscribe({
      next: () => {
        this.snackBar.open('Campo eliminado', 'Cerrar', { duration: 3000 });
        this.loadBillingFields();
      },
      error: (error: unknown) =>
        this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo eliminar', 'Cerrar', { duration: 3000 })
    });
  }

  toggleChannelDelete(channel: OrderChannelDto) {
    if (!channel.deleted && !confirm(`Eliminar el canal ${channel.name}?`)) {
      return;
    }
    if (channel.deleted) {
      this.ordersApi.restoreChannel(channel.id).subscribe({
        next: () => {
          this.snackBar.open('Canal restaurado', 'Cerrar', { duration: 3000 });
          this.loadChannels();
        },
        error: (error: unknown) =>
          this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo restaurar', 'Cerrar', { duration: 3000 })
      });
      return;
    }
    this.ordersApi.deleteChannel(channel.id).subscribe({
      next: () => {
        this.snackBar.open('Canal eliminado', 'Cerrar', { duration: 3000 });
        this.loadChannels();
      },
      error: (error: unknown) =>
        this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo eliminar', 'Cerrar', { duration: 3000 })
    });
  }
}
