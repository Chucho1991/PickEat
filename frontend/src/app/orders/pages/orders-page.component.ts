import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuApiService, MenuItemDto } from '../../core/services/menu-api.service';
import { DiscountsApiService, DiscountItemDto } from '../../core/services/discounts-api.service';
import { MesasApiService, MesaDto } from '../../core/services/mesas-api.service';
import { OrderChannelDto, OrderConfigDto, OrderResponse, OrdersApiService, OrderStatus } from '../../core/services/orders-api.service';
import { environment } from '../../../environments/environment';
import { AuthService } from '../../core/services/auth.service';

interface OrderLine {
  menuItem: MenuItemDto;
  quantity: number;
  unitPrice: number;
  total: number;
}

interface DiscountLine {
  discountItem: DiscountItemDto;
  quantity: number;
  unitValue: number;
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
    <div class="card order-header-card" *ngIf="hasMesaSelected">
      <div class="order-header">
        <div>
          <p class="order-label">Orden No.</p>
          <h3 class="order-number">{{ orderNumberDisplay }}</h3>
        </div>
        <div class="field">
          <span>Mesa seleccionada</span>
          <p class="mesa-selected-label">{{ selectedMesaLabel || 'Sin seleccionar' }}</p>
        </div>
        <div class="field channel-field">
          <span>Canal</span>
          <div class="channel-options">
            <label class="checkbox channel-option" *ngFor="let channel of channels">
              <input
                type="checkbox"
                [checked]="selectedChannelId === channel.id"
                (change)="selectChannel(channel.id)"
              />
              <span>{{ channel.name }}</span>
            </label>
          </div>
        </div>
        <button class="btn btn-primary" type="button" (click)="saveOrder()">{{ isEditMode ? 'Actualizar orden' : 'Guardar orden' }}</button>
      </div>
    </div>

    <div class="card mesa-dashboard" *ngIf="!hasMesaSelected">
      <div class="mesa-dashboard-header">
        <div>
          <h3 class="section-title">Selecciona una mesa libre</h3>
          <p class="section-subtitle">Al elegirla podras continuar con la orden.</p>
        </div>
      </div>
      <div class="mesa-grid">
        <button
          class="mesa-card"
          type="button"
          *ngFor="let mesa of mesasDisponibles"
          [class.mesa-selected]="selectedMesaId === mesa.id"
          [ngStyle]="mesaStyle(mesa)"
          (click)="selectMesa(mesa)"
        >
          <span class="mesa-name">{{ mesa.description }}</span>
          <span class="mesa-seats">{{ mesa.seats }} puestos</span>
        </button>
      </div>
      <div class="empty-state" *ngIf="mesasDisponibles.length === 0">
        <p>No hay mesas libres disponibles.</p>
      </div>
    </div>

    <div class="orders-layout" *ngIf="hasMesaSelected">
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

        <div class="order-menu-header">
          <div>
            <h3 class="section-title">Descuentos disponibles</h3>
            <p class="section-subtitle">Selecciona descuentos para aplicar al total.</p>
          </div>
          <label class="field inline-field">
            <span>Buscar</span>
            <input type="search" placeholder="Buscar descuento" [(ngModel)]="discountSearchTerm" (input)="applyDiscountFilters()" />
          </label>
        </div>

        <div class="menu-grid">
          <button class="menu-card" type="button" *ngFor="let item of filteredDiscountItems" (click)="addDiscount(item)">
            <div class="menu-thumb" [class.menu-thumb-empty]="!item.imagePath">
              <img *ngIf="item.imagePath" [src]="imageUrl(item.imagePath)" [alt]="item.nickname" />
              <span *ngIf="!item.imagePath">Sin imagen</span>
            </div>
            <div class="menu-info">
              <p class="menu-title">{{ item.nickname }}</p>
              <p class="menu-subtitle">{{ item.shortDescription }}</p>
            </div>
            <div class="menu-price">{{ formatDiscountValue(item) }}</div>
          </button>
        </div>
      </section>

      <aside class="card order-summary">
        <div class="order-summary-header">
          <h3 class="section-title">Detalle de orden</h3>
          <p class="section-subtitle">{{ orderItems.length + orderDiscountItems.length }} items agregados</p>
        </div>

        <div class="order-status" *ngIf="loadedOrder">
          <div class="order-status-header">
            <span class="detail-label">Estado</span>
            <span class="badge" [class]="statusBadgeClass(orderStatus)">{{ statusLabel(orderStatus) }}</span>
          </div>
          <div class="status-actions" *ngIf="canManageStatus && !loadedOrder.deleted">
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus('CREADA')" title="Creada" aria-label="Creada">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M12 6V18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
              </svg>
            </button>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus('PREPARANDOSE')" title="Preparandose" aria-label="Preparandose">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
              </svg>
            </button>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus('DESPACHADA')" title="Despachada" aria-label="Despachada">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M3 7H15V17H3V7Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M15 10H19L21 12V17H15V10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M7 17V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                <path d="M17 17V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
              </svg>
            </button>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus('PAGADA')" title="Pagada" aria-label="Pagada">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M5 13L9 17L19 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </button>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus('INACTIVA')" title="Inactiva" aria-label="Inactiva">
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M6 6H8V18H6V6Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                <path d="M16 6H18V18H16V6Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
            </button>
          </div>
        </div>

        <div class="tip-config">
          <label class="field">
            <span>Tipo de propina</span>
            <select [(ngModel)]="tipType">
              <option value="NONE">Sin propina</option>
              <option value="PERCENTAGE">Porcentaje ({{ config.tipValue }}%)</option>
              <option value="FIXED">Valor fijo</option>
            </select>
          </label>
          <ng-container *ngIf="tipType !== 'NONE'">
            <label class="field" *ngIf="tipType === 'FIXED'; else percentTip">
              <span>Propina en dinero</span>
              <input type="number" min="0" step="0.01" [(ngModel)]="fixedTipAmount" />
            </label>
            <ng-template #percentTip>
              <label class="field">
                <span>Propina (%)</span>
                <input type="number" [value]="config.tipValue" disabled />
              </label>
            </ng-template>
          </ng-container>
        </div>

        <div class="order-items">
          <div class="order-item-row" *ngFor="let line of orderItems">
            <div class="order-item-main">
              <div class="order-item-title">
                <p class="order-item-name">{{ line.menuItem.nickname }}</p>
                <span *ngIf="line.menuItem.aplicaImpuesto" class="tax-flag" title="Aplica impuesto">I</span>
              </div>
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
          <div class="order-item-divider" *ngIf="orderDiscountItems.length > 0">Descuentos</div>
          <div class="order-item-row" *ngFor="let line of orderDiscountItems">
            <div class="order-item-main">
              <div class="order-item-title">
                <p class="order-item-name">{{ line.discountItem.nickname }}</p>
              </div>
              <p class="order-item-meta">{{ discountLabel(line.discountItem) }}</p>
            </div>
            <div class="order-item-qty">
              <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="decreaseDiscount(line.discountItem)" title="Restar" aria-label="Restar">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                  <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                </svg>
              </button>
              <span class="order-item-count">{{ line.quantity }}</span>
              <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="addDiscount(line.discountItem)" title="Sumar" aria-label="Sumar">
                <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                  <path d="M12 6V18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                  <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                </svg>
              </button>
            </div>
            <div class="order-item-prices">
              <span class="order-item-unit">{{ formatDiscountLineUnit(line) }}</span>
              <span class="order-item-total">-{{ formatMoney(line.total) }}</span>
            </div>
            <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="removeDiscount(line.discountItem)" title="Eliminar" aria-label="Eliminar">
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
  private discountsApi = inject(DiscountsApiService);
  private mesasApi = inject(MesasApiService);
  private ordersApi = inject(OrdersApiService);
  private snackBar = inject(MatSnackBar);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  menuItems: MenuItemDto[] = [];
  filteredMenuItems: MenuItemDto[] = [];
  discountItems: DiscountItemDto[] = [];
  filteredDiscountItems: DiscountItemDto[] = [];
  mesas: MesaDto[] = [];
  orderItems: OrderLine[] = [];
  orderDiscountItems: DiscountLine[] = [];
  selectedMesaId = '';
  selectedChannelId = '';
  channels: OrderChannelDto[] = [];
  selectedDishType = '';
  searchTerm = '';
  discountSearchTerm = '';
  orderNumber?: number;
  tipType: 'PERCENTAGE' | 'FIXED' | 'NONE' = 'NONE';
  fixedTipAmount = 0;
  isEditMode = false;
  editingOrderId: string | null = null;
  loadedOrder: OrderResponse | null = null;
  private hasSyncedOrderItems = false;
  private hasSyncedDiscountItems = false;
  orderStatus: OrderStatus = 'CREADA';
  canManageStatus = this.authService.hasRole(['SUPERADMINISTRADOR', 'ADMINISTRADOR']);
  private mesaPalette = ['#fde68a', '#bfdbfe', '#bbf7d0', '#fecaca', '#fbcfe8', '#ddd6fe', '#fee2e2', '#cffafe'];
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
    this.loadChannels();
    this.loadMenuItems();
    this.loadDiscountItems();
    this.route.paramMap.subscribe((params) => {
      const orderId = params.get('id');
      this.isEditMode = Boolean(orderId);
      this.editingOrderId = orderId;
      if (orderId) {
        this.loadOrder(orderId);
      } else {
        this.resetOrder();
      }
    });
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
   * Agrega un descuento al detalle de la orden.
   */
  addDiscount(item: DiscountItemDto) {
    const existing = this.orderDiscountItems.find((line) => line.discountItem.id === item.id);
    if (existing) {
      existing.quantity += 1;
      existing.total = this.calculateDiscountLineTotal(item, existing.quantity);
      return;
    }
    this.orderDiscountItems.push({
      discountItem: item,
      quantity: 1,
      unitValue: Number(item.value),
      total: this.calculateDiscountLineTotal(item, 1)
    });
  }

  /**
   * Reduce la cantidad de un descuento.
   */
  decreaseDiscount(item: DiscountItemDto) {
    const existing = this.orderDiscountItems.find((line) => line.discountItem.id === item.id);
    if (!existing) {
      return;
    }
    existing.quantity -= 1;
    if (existing.quantity <= 0) {
      this.removeDiscount(item);
      return;
    }
    existing.total = this.calculateDiscountLineTotal(item, existing.quantity);
  }

  /**
   * Elimina un descuento del detalle.
   */
  removeDiscount(item: DiscountItemDto) {
    this.orderDiscountItems = this.orderDiscountItems.filter((line) => line.discountItem.id !== item.id);
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
   * Aplica filtros de busqueda para descuentos.
   */
  applyDiscountFilters() {
    this.loadDiscountItems();
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
    if (this.tipType === 'FIXED' && this.fixedTipAmount < 0) {
      this.snackBar.open('La propina fija no puede ser negativa.', 'Cerrar', { duration: 3000 });
      return;
    }
    const request = {
      mesaId: this.selectedMesaId,
      channelId: this.selectedChannelId || undefined,
      items: this.orderItems.map((line) => ({
        menuItemId: line.menuItem.id,
        quantity: line.quantity
      })),
      discountItems: this.orderDiscountItems.length
        ? this.orderDiscountItems.map((line) => ({
            discountItemId: line.discountItem.id,
            quantity: line.quantity
          }))
        : undefined,
      tipType: this.tipType === 'NONE' ? undefined : this.tipType,
      tipValue: this.tipType === 'FIXED' ? Number(this.fixedTipAmount || 0) : undefined,
      tipEnabled: this.tipType !== 'NONE'
    };
    if (this.isEditMode && this.editingOrderId) {
      this.ordersApi.update(this.editingOrderId, request).subscribe({
        next: (order) => {
          this.orderNumber = order.orderNumber;
          this.snackBar.open('Orden actualizada', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/ordenes/lista']);
        },
        error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar la orden', 'Cerrar', { duration: 3000 })
      });
      return;
    }
    this.ordersApi.create(request).subscribe({
      next: (order) => {
        this.orderNumber = order.orderNumber;
        this.snackBar.open('Orden creada', 'Cerrar', { duration: 3000 });
        this.loadMesas();
        this.resetOrder();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo crear la orden', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Etiqueta de mesa seleccionada.
   */
  get selectedMesaLabel() {
    return this.mesas.find((mesa) => mesa.id === this.selectedMesaId)?.description ?? '';
  }

  /**
   * Determina si ya se selecciono una mesa.
   */
  get hasMesaSelected() {
    return Boolean(this.selectedMesaId);
  }

  /**
   * Lista de mesas libres para seleccionar.
   */
  get mesasDisponibles() {
    return this.mesas.filter((mesa) => !mesa.ocupada || mesa.id === this.selectedMesaId);
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
    if (this.tipType === 'FIXED') {
      return 'Propina fija';
    }
    if (this.tipType === 'NONE') {
      return 'Sin propina';
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
    return this.round((this.taxableSubtotal * this.config.taxRate) / 100);
  }

  /**
   * Propina calculada.
   */
  get tipAmount() {
    if (this.tipType === 'FIXED') {
      return this.round(this.fixedTipAmount || 0);
    }
    if (this.tipType === 'NONE') {
      return 0;
    }
    return this.round(((this.subtotal + this.taxAmount) * this.config.tipValue) / 100);
  }

  /**
   * Descuento aplicado (por ahora fijo en cero).
   */
  get discountAmount() {
    const baseTotal = this.subtotal + this.taxAmount + this.tipAmount;
    const discountTotal = this.orderDiscountItems.reduce((sum, line) => {
      const lineTotal = this.calculateDiscountLineTotal(line.discountItem, line.quantity);
      line.total = lineTotal;
      return sum + lineTotal;
    }, 0);
    return this.round(Math.min(baseTotal, discountTotal));
  }

  /**
   * Total calculado.
   */
  get totalAmount() {
    return this.round(this.subtotal + this.taxAmount + this.tipAmount - this.discountAmount);
  }

  /**
   * Subtotal solo de items con impuesto.
   */
  get taxableSubtotal() {
    return this.round(
      this.orderItems.reduce((sum, item) => (item.menuItem.aplicaImpuesto ? sum + item.total : sum), 0)
    );
  }

  /**
   * Formatea valores monetarios.
   */
  formatMoney(value: number) {
    const formatted = new Intl.NumberFormat('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value);
    return `${this.config.currencySymbol}${formatted}`;
  }

  formatDiscountValue(item: DiscountItemDto) {
    if (item.discountType === 'PERCENTAGE') {
      return `${item.value}%`;
    }
    return this.formatMoney(item.value);
  }

  discountLabel(item: DiscountItemDto) {
    if (item.discountType === 'PERCENTAGE') {
      return `Descuento ${item.value}% sobre el total`;
    }
    return `Descuento fijo ${this.formatMoney(item.value)}`;
  }

  formatDiscountLineUnit(line: DiscountLine) {
    if (line.discountItem.discountType === 'PERCENTAGE') {
      return `${line.discountItem.value}%`;
    }
    return this.formatMoney(line.unitValue);
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
      next: (config) => {
        this.config = config;
        this.updateTipSelectionFromOrder();
      },
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

  private loadChannels() {
    this.ordersApi.listChannels(false).subscribe({
      next: (channels) => {
        this.channels = channels.filter((channel) => channel.activo && !channel.deleted);
        if (!this.selectedChannelId) {
          const defaultChannel = this.channels.find((channel) => channel.isDefault) ?? this.channels.find((channel) => channel.name === 'LOCAL');
          if (defaultChannel) {
            this.selectedChannelId = defaultChannel.id;
          }
        }
      },
      error: () => this.snackBar.open('No se pudo cargar canales', 'Cerrar', { duration: 3000 })
    });
  }

  private loadMenuItems() {
    this.menuApi
      .list({ dishType: this.selectedDishType || undefined, activo: true, search: this.searchTerm || undefined })
      .subscribe({
        next: (items) => {
          this.menuItems = items.filter((item) => item.activo && !item.deleted);
          this.filteredMenuItems = [...this.menuItems];
          this.syncOrderItems();
        },
        error: () => this.snackBar.open('No se pudo cargar menu', 'Cerrar', { duration: 3000 })
      });
  }

  private loadDiscountItems() {
    this.discountsApi.list({ activo: true, search: this.discountSearchTerm || undefined }).subscribe({
      next: (items) => {
        this.discountItems = items.filter((item) => item.activo && !item.deleted);
        this.filteredDiscountItems = [...this.discountItems];
        this.syncDiscountItems();
      },
      error: () => this.snackBar.open('No se pudo cargar descuentos', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Carga la orden en modo edicion.
   *
   * @param id identificador de la orden.
   */
  private loadOrder(id: string) {
    this.ordersApi.getById(id).subscribe({
      next: (order) => {
        if (order.deleted) {
          this.snackBar.open('La orden esta eliminada.', 'Cerrar', { duration: 3000 });
          this.router.navigate(['/ordenes/lista']);
          return;
        }
        this.loadedOrder = order;
        this.hasSyncedOrderItems = false;
        this.hasSyncedDiscountItems = false;
        this.orderNumber = order.orderNumber;
        this.selectedMesaId = order.mesaId;
        this.selectedChannelId = order.channelId;
        this.orderStatus = order.status;
        this.updateTipSelectionFromOrder();
        this.syncOrderItems();
        this.syncDiscountItems();
      },
      error: () => {
        this.snackBar.open('No se pudo cargar la orden', 'Cerrar', { duration: 3000 });
        this.router.navigate(['/ordenes/lista']);
      }
    });
  }

  private updateTipSelectionFromOrder() {
    if (!this.loadedOrder) {
      return;
    }
    const tipAmount = Number(this.loadedOrder.tipAmount);
    if (tipAmount <= 0) {
      this.tipType = 'NONE';
      this.fixedTipAmount = 0;
      return;
    }
    const expectedPercent = this.round(((this.loadedOrder.subtotal + this.loadedOrder.taxAmount) * this.config.tipValue) / 100);
    if (this.config.tipValue > 0 && Math.abs(expectedPercent - tipAmount) > 0.01) {
      this.tipType = 'FIXED';
      this.fixedTipAmount = tipAmount;
      return;
    }
    this.tipType = 'PERCENTAGE';
    this.fixedTipAmount = tipAmount;
  }

  /**
   * Sincroniza los items existentes de la orden con el catalogo.
   */
  private syncOrderItems() {
    if (!this.loadedOrder || this.menuItems.length === 0 || this.hasSyncedOrderItems) {
      return;
    }
    this.orderItems = this.loadedOrder.items.map((item) => {
      const menuItem = this.menuItems.find((menu) => menu.id === item.menuItemId) ?? this.buildPlaceholderItem(item);
      return {
        menuItem,
        quantity: item.quantity,
        unitPrice: Number(item.unitPrice),
        total: Number(item.totalPrice)
      };
    });
    this.hasSyncedOrderItems = true;
  }

  private syncDiscountItems() {
    if (!this.loadedOrder || this.discountItems.length === 0 || this.hasSyncedDiscountItems) {
      return;
    }
    this.orderDiscountItems = this.loadedOrder.discountItems.map((item) => {
      const discountItem =
        this.discountItems.find((discount) => discount.id === item.discountItemId) ?? this.buildPlaceholderDiscount(item);
      return {
        discountItem,
        quantity: item.quantity,
        unitValue: Number(item.unitValue),
        total: Number(item.totalValue)
      };
    });
    this.hasSyncedDiscountItems = true;
  }

  /**
   * Construye un placeholder para items no disponibles.
   *
   * @param item item base de la orden.
   */
  private buildPlaceholderItem(item: { menuItemId: string; unitPrice: number }) {
    return {
      id: item.menuItemId,
      longDescription: 'No disponible',
      shortDescription: 'No disponible',
      nickname: 'Item eliminado',
      dishType: 'OTRO',
      activo: false,
      deleted: true,
      aplicaImpuesto: false,
      price: item.unitPrice,
      imagePath: null,
      createdAt: '',
      updatedAt: ''
    } as MenuItemDto;
  }

  private buildPlaceholderDiscount(item: { discountItemId: string; unitValue: number; discountType: string }) {
    return {
      id: item.discountItemId,
      longDescription: 'No disponible',
      shortDescription: 'No disponible',
      nickname: 'Descuento eliminado',
      discountType: item.discountType as 'FIXED' | 'PERCENTAGE',
      value: item.unitValue,
      activo: false,
      deleted: true,
      imagePath: null,
      createdAt: '',
      updatedAt: ''
    } as DiscountItemDto;
  }

  private resetOrder() {
    this.selectedMesaId = '';
    const defaultChannel = this.channels.find((channel) => channel.isDefault) ?? this.channels.find((channel) => channel.name === 'LOCAL');
    this.selectedChannelId = defaultChannel ? defaultChannel.id : '';
    this.orderItems = [];
    this.orderDiscountItems = [];
    this.orderNumber = undefined;
    this.loadedOrder = null;
    this.isEditMode = false;
    this.editingOrderId = null;
    this.orderStatus = 'CREADA';
    this.tipType = 'NONE';
    this.fixedTipAmount = 0;
    this.hasSyncedOrderItems = false;
    this.hasSyncedDiscountItems = false;
  }

  private calculateDiscountLineTotal(item: DiscountItemDto, quantity: number) {
    const baseTotal = this.subtotal + this.taxAmount + this.tipAmount;
    if (item.discountType === 'PERCENTAGE') {
      return this.round(((baseTotal * item.value) / 100) * quantity);
    }
    return this.round(item.value * quantity);
  }

  selectMesa(mesa: MesaDto) {
    this.selectedMesaId = mesa.id;
  }

  mesaStyle(mesa: MesaDto) {
    const seats = Math.max(1, mesa.seats || 1);
    const minSize = 90;
    const maxSize = 180;
    const size = Math.min(maxSize, minSize + seats * 12);
    const height = Math.min(140, 70 + seats * 6);
    return { width: `${size}px`, height: `${height}px`, background: this.mesaColor(mesa.id) };
  }

  selectChannel(channelId: string) {
    this.selectedChannelId = channelId;
  }

  /**
   * Color consistente por mesa.
   *
   * @param mesaId identificador de mesa.
   */
  mesaColor(mesaId: string) {
    const hash = mesaId.split('').reduce((sum, char) => sum + char.charCodeAt(0), 0);
    return this.mesaPalette[hash % this.mesaPalette.length];
  }

  /**
   * Cambia el estado de la orden actual.
   *
   * @param status estado objetivo.
   */
  setStatus(status: OrderStatus) {
    if (!this.loadedOrder) {
      return;
    }
    this.ordersApi.changeStatus(this.loadedOrder.id, status).subscribe({
      next: (order) => {
        this.loadedOrder = order;
        this.orderStatus = order.status;
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadMesas();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar el estado', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Devuelve la etiqueta para el estado.
   *
   * @param status estado base.
   */
  statusLabel(status: OrderStatus) {
    switch (status) {
      case 'CREADA':
        return 'Creada';
      case 'PREPARANDOSE':
        return 'Preparandose';
      case 'DESPACHADA':
        return 'Despachada';
      case 'PAGADA':
        return 'Pagada';
      case 'INACTIVA':
        return 'Inactiva';
      case 'ELIMINADA':
        return 'Eliminada';
      default:
        return status;
    }
  }

  /**
   * Devuelve la clase CSS para la etiqueta del estado.
   *
   * @param status estado base.
   */
  statusBadgeClass(status: OrderStatus) {
    switch (status) {
      case 'CREADA':
        return 'badge badge-info';
      case 'PREPARANDOSE':
        return 'badge badge-warning';
      case 'DESPACHADA':
        return 'badge badge-primary';
      case 'PAGADA':
        return 'badge badge-success';
      case 'INACTIVA':
        return 'badge badge-muted';
      case 'ELIMINADA':
        return 'badge badge-danger';
      default:
        return 'badge badge-muted';
    }
  }

  private round(value: number) {
    return Math.round(value * 100) / 100;
  }
}
