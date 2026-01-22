import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MenuApiService, MenuItemDto } from '../../core/services/menu-api.service';
import { MesasApiService, MesaDto } from '../../core/services/mesas-api.service';
import { OrderConfigDto, OrdersApiService } from '../../core/services/orders-api.service';
import { environment } from '../../../environments/environment';

interface OrderLine {
  menuItem: MenuItemDto;
  quantity: number;
  unitPrice: number;
  total: number;
}

/**
 * Pagina para generar ordenes.
 */
@Component({
  selector: 'app-orders-page',
  standalone: true,
  imports: [CommonModule, FormsModule, MatSnackBarModule],
  template: `
    <div class="page-header">
      <div>
        <h2 class="page-title">Ordenes</h2>
        <p class="page-subtitle">Crea una orden asignada a una mesa.</p>
      </div>
      <button class="btn btn-primary" type="button" (click)="saveOrder()">Guardar orden</button>
    </div>

    <div class="card order-header-card">
      <div class="order-header">
        <div>
          <p class="order-label">Orden No.</p>
          <h3 class="order-number">{{ orderNumberDisplay }}</h3>
        </div>
        <label class="field">
          <span>Mesa</span>
          <select [(ngModel)]="selectedMesaId">
            <option value="">Selecciona una mesa</option>
            <option *ngFor="let mesa of mesas" [value]="mesa.id">{{ mesa.description }}</option>
          </select>
        </label>
      </div>
    </div>

    <div class="orders-layout">
      <section class="card">
        <div class="order-menu-header">
          <div>
            <h3 class="section-title">Menu disponible</h3>
            <p class="section-subtitle">Selecciona los platos para la orden.</p>
          </div>
          <label class="field inline-field">
            <span>Buscar</span>
            <input type="search" placeholder="Buscar plato" [(ngModel)]="searchTerm" (input)="applyFilters()" />
          </label>
        </div>

        <div class="menu-types">
          <button
            class="menu-type"
            type="button"
            [class.menu-type-active]="selectedDishType === ''"
            (click)="setDishType('')"
          >
            Todo
          </button>
          <button
            class="menu-type"
            type="button"
            *ngFor="let type of dishTypes"
            [class.menu-type-active]="selectedDishType === type"
            (click)="setDishType(type)"
          >
            {{ type }}
          </button>
        </div>

        <div class="menu-grid">
          <button class="menu-card" type="button" *ngFor="let item of filteredMenuItems" (click)="addItem(item)">
            <div class="menu-thumb" [class.menu-thumb-empty]="!item.imagePath">
              <img *ngIf="item.imagePath" [src]="imageUrl(item.imagePath)" [alt]="item.nickname" />
              <span *ngIf="!item.imagePath">Sin imagen</span>
            </div>
            <div class="menu-info">
              <p class="menu-title">{{ item.nickname }}</p>
              <p class="menu-subtitle">{{ item.shortDescription }}</p>
            </div>
            <div class="menu-price">{{ formatMoney(item.price) }}</div>
          </button>
        </div>
      </section>

      <aside class="card order-summary">
        <div class="order-summary-header">
          <h3 class="section-title">Detalle de orden</h3>
          <p class="section-subtitle">{{ orderItems.length }} items agregados</p>
        </div>

        <div class="order-items">
          <div class="order-item-row" *ngFor="let line of orderItems">
            <div class="order-item-main">
              <p class="order-item-name">{{ line.menuItem.nickname }}</p>
              <p class="order-item-meta">{{ line.menuItem.shortDescription }}</p>
            </div>
            <div class="order-item-qty">
              <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="decreaseItem(line.menuItem)" title="Restar" aria-label="Restar">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                  <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                </svg>
              </button>
              <span class="order-item-count">{{ line.quantity }}</span>
              <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="addItem(line.menuItem)" title="Sumar" aria-label="Sumar">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                  <path d="M12 6V18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                  <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                </svg>
              </button>
            </div>
            <div class="order-item-prices">
              <span class="order-item-unit">{{ formatMoney(line.unitPrice) }}</span>
              <span class="order-item-total">{{ formatMoney(line.total) }}</span>
            </div>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="removeItem(line.menuItem)" title="Eliminar" aria-label="Eliminar">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M4 7H20" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                <path d="M10 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                <path d="M14 11V17" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                <path d="M6 7L7 19H17L18 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M9 7V5H15V7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </button>
          </div>
          <div class="empty-state" *ngIf="orderItems.length === 0">
            <p>Agrega platos desde el menu.</p>
          </div>
        </div>

        <div class="order-totals">
          <div class="total-row">
            <span>Subtotal</span>
            <span>{{ formatMoney(subtotal) }}</span>
          </div>
          <div class="total-row">
            <span>Impuesto ({{ config.taxRate }}%)</span>
            <span>{{ formatMoney(taxAmount) }}</span>
          </div>
          <div class="total-row">
            <span>Descuento</span>
            <span>{{ formatMoney(discountAmount) }}</span>
          </div>
          <div class="total-row">
            <span>{{ tipLabel }}</span>
            <span>{{ formatMoney(tipAmount) }}</span>
          </div>
          <div class="total-row total-main">
            <span>Total</span>
            <span>{{ formatMoney(totalAmount) }}</span>
          </div>
        </div>
      </aside>
    </div>
  `
})
export class OrdersPageComponent implements OnInit {
  private menuApi = inject(MenuApiService);
  private mesasApi = inject(MesasApiService);
  private ordersApi = inject(OrdersApiService);
  private snackBar = inject(MatSnackBar);

  menuItems: MenuItemDto[] = [];
  filteredMenuItems: MenuItemDto[] = [];
  mesas: MesaDto[] = [];
  orderItems: OrderLine[] = [];
  selectedMesaId = '';
  selectedDishType = '';
  searchTerm = '';
  orderNumber?: number;
  config: OrderConfigDto = {
    taxRate: 0,
    tipType: 'PERCENTAGE',
    tipValue: 0,
    currencyCode: 'USD',
    currencySymbol: '$'
  };
  dishTypes = ['ENTRADA', 'FUERTE', 'BEBIDA', 'OTRO', 'POSTRE', 'COMBO'];

  /**
   * Carga datos iniciales.
   */
  ngOnInit() {
    this.loadConfig();
    this.loadMesas();
    this.loadMenuItems();
  }

  /**
   * Agrega un item al detalle de la orden.
   */
  addItem(item: MenuItemDto) {
    const existing = this.orderItems.find((line) => line.menuItem.id === item.id);
    if (existing) {
      existing.quantity += 1;
      existing.total = this.round(existing.quantity * existing.unitPrice);
      return;
    }
    this.orderItems.push({
      menuItem: item,
      quantity: 1,
      unitPrice: Number(item.price),
      total: this.round(Number(item.price))
    });
  }

  /**
   * Reduce la cantidad de un item.
   */
  decreaseItem(item: MenuItemDto) {
    const existing = this.orderItems.find((line) => line.menuItem.id === item.id);
    if (!existing) {
      return;
    }
    existing.quantity -= 1;
    if (existing.quantity <= 0) {
      this.removeItem(item);
      return;
    }
    existing.total = this.round(existing.quantity * existing.unitPrice);
  }

  /**
   * Elimina un item del detalle.
   */
  removeItem(item: MenuItemDto) {
    this.orderItems = this.orderItems.filter((line) => line.menuItem.id !== item.id);
  }

  /**
   * Cambia el filtro por tipo de plato.
   */
  setDishType(type: string) {
    this.selectedDishType = type;
    this.loadMenuItems();
  }

  /**
   * Aplica filtros de busqueda.
   */
  applyFilters() {
    this.loadMenuItems();
  }

  /**
   * Guarda la orden en el backend.
   */
  saveOrder() {
    if (!this.selectedMesaId) {
      this.snackBar.open('Debes seleccionar una mesa.', 'Cerrar', { duration: 3000 });
      return;
    }
    if (this.orderItems.length === 0) {
      this.snackBar.open('Debes agregar al menos un item.', 'Cerrar', { duration: 3000 });
      return;
    }
    const request = {
      mesaId: this.selectedMesaId,
      items: this.orderItems.map((line) => ({
        menuItemId: line.menuItem.id,
        quantity: line.quantity
      }))
    };
    this.ordersApi.create(request).subscribe({
      next: (order) => {
        this.orderNumber = order.orderNumber;
        this.snackBar.open('Orden creada', 'Cerrar', { duration: 3000 });
        this.resetOrder();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo crear la orden', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Texto formateado para el numero de orden.
   */
  get orderNumberDisplay() {
    if (!this.orderNumber) {
      return '-----';
    }
    return String(this.orderNumber).padStart(5, '0');
  }

  /**
   * Etiqueta de propina segun configuracion.
   */
  get tipLabel() {
    if (this.config.tipType === 'FIXED') {
      return 'Propina fija';
    }
    return `Propina (${this.config.tipValue}%)`;
  }

  /**
   * Subtotal calculado.
   */
  get subtotal() {
    return this.round(this.orderItems.reduce((sum, item) => sum + item.total, 0));
  }

  /**
   * Impuesto calculado.
   */
  get taxAmount() {
    return this.round((this.subtotal * this.config.taxRate) / 100);
  }

  /**
   * Propina calculada.
   */
  get tipAmount() {
    if (this.config.tipType === 'FIXED') {
      return this.round(this.config.tipValue);
    }
    return this.round(((this.subtotal + this.taxAmount) * this.config.tipValue) / 100);
  }

  /**
   * Descuento aplicado (por ahora fijo en cero).
   */
  get discountAmount() {
    return 0;
  }

  /**
   * Total calculado.
   */
  get totalAmount() {
    return this.round(this.subtotal + this.taxAmount + this.tipAmount - this.discountAmount);
  }

  /**
   * Formatea valores monetarios.
   */
  formatMoney(value: number) {
    const formatted = new Intl.NumberFormat('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value);
    return `${this.config.currencySymbol}${formatted}`;
  }

  /**
   * Resuelve la URL publica de la imagen.
   */
  imageUrl(imagePath: string) {
    if (!imagePath) {
      return '';
    }
    if (imagePath.startsWith('http')) {
      return imagePath;
    }
    return `${environment.apiUrl}${imagePath}`;
  }

  private loadConfig() {
    this.ordersApi.getConfig().subscribe({
      next: (config) => (this.config = config),
      error: () => this.snackBar.open('No se pudo cargar la configuracion', 'Cerrar', { duration: 3000 })
    });
  }

  private loadMesas() {
    this.mesasApi.list(false).subscribe({
      next: (mesas) => {
        this.mesas = mesas.filter((mesa) => mesa.activo && !mesa.deleted);
      },
      error: () => this.snackBar.open('No se pudo cargar mesas', 'Cerrar', { duration: 3000 })
    });
  }

  private loadMenuItems() {
    this.menuApi
      .list({ dishType: this.selectedDishType || undefined, activo: true, search: this.searchTerm || undefined })
      .subscribe({
        next: (items) => {
          this.menuItems = items.filter((item) => item.activo && !item.deleted);
          this.filteredMenuItems = [...this.menuItems];
        },
        error: () => this.snackBar.open('No se pudo cargar menu', 'Cerrar', { duration: 3000 })
      });
  }

  private resetOrder() {
    this.selectedMesaId = '';
    this.orderItems = [];
  }

  private round(value: number) {
    return Math.round(value * 100) / 100;
  }
}
