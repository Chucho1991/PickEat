import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { OrderBillingFieldDto, OrderChannelDto, OrderResponse, OrdersApiService, OrderStatus } from '../../core/services/orders-api.service';
import { AuthService } from '../../core/services/auth.service';
import { MesasApiService, MesaDto } from '../../core/services/mesas-api.service';
import { MenuApiService, MenuItemDto } from '../../core/services/menu-api.service';
import { DiscountsApiService, DiscountItemDto } from '../../core/services/discounts-api.service';
import { OrderVoucherService } from '../../core/services/order-voucher.service';

/**
 * Pagina con el listado de ordenes.
 */
@Component({
  selector: 'app-orders-list-page',
  standalone: true,
  imports: [CommonModule, MatSnackBarModule],
  template: `
    <div class="card">
      <div class="table-toolbar">
        <span class="table-title">Listado</span>
      </div>

      <div class="table-wrapper">
        <table class="table">
          <thead>
            <tr>
              <th>Orden</th>
              <th>Mesa</th>
              <th>Canal</th>
              <th>Items</th>
              <th>Total</th>
              <th>Estado</th>
              <th>Fecha</th>
              <th class="text-right">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <ng-container *ngFor="let order of orders">
              <tr>
                <td>#{{ orderNumberDisplay(order.orderNumber) }}</td>
                <td>{{ mesaLabel(order.mesaId) }}</td>
                <td>{{ channelLabel(order.channelId) }}</td>
                <td>{{ totalItems(order) }}</td>
                <td>{{ formatMoney(order.totalAmount, order.currencySymbol) }}</td>
                <td>
                  <span class="badge badge-danger" *ngIf="order.deleted; else statusBadge">Eliminada</span>
                  <ng-template #statusBadge>
                    <span class="badge" [class]="statusBadgeClass(order.status)">{{ statusLabel(order.status) }}</span>
                  </ng-template>
                </td>
                <td>{{ order.createdAt | date: 'short' }}</td>
                <td class="text-right">
                  <button
                    class="btn btn-ghost btn-sm icon-btn"
                    type="button"
                    (click)="regenerateVoucher(order)"
                    title="Regenerar voucher"
                    aria-label="Regenerar voucher"
                  >
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M4 12C4 7.58172 7.58172 4 12 4C13.9576 4 15.7416 4.70456 17.1189 5.875M20 12C20 16.4183 16.4183 20 12 20C10.0424 20 8.2584 19.2954 6.88111 18.125" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                      <path d="M7 6V10H3" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                      <path d="M17 14V18H21" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                  </button>
                  <button
                    class="btn btn-ghost btn-sm icon-btn"
                    type="button"
                    (click)="editOrder(order)"
                    [disabled]="order.deleted"
                    title="Editar"
                    aria-label="Editar"
                  >
                    <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M4 17.25V20h2.75L18.5 8.25L15.75 5.5L4 17.25Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                      <path d="M13.5 6.5L17.5 10.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                  </button>
                  <button
                    class="btn btn-ghost btn-sm icon-btn"
                    type="button"
                    (click)="toggleDelete(order)"
                    [title]="order.deleted ? 'Restaurar' : 'Eliminar'"
                    [attr.aria-label]="order.deleted ? 'Restaurar' : 'Eliminar'"
                  >
                    <svg *ngIf="order.deleted; else deleteIcon" viewBox="0 0 24 24" fill="none" aria-hidden="true">
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
                  <button
                    class="btn btn-ghost btn-sm icon-btn"
                    type="button"
                    (click)="toggleDetails(order)"
                    [title]="isExpanded(order) ? 'Ocultar detalle' : 'Ver detalle'"
                    [attr.aria-label]="isExpanded(order) ? 'Ocultar detalle' : 'Ver detalle'"
                  >
                    <svg *ngIf="!isExpanded(order); else collapseIcon" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                      <path d="M12 5V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      <path d="M5 12H19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                    </svg>
                    <ng-template #collapseIcon>
                      <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                        <path d="M5 12H19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                      </svg>
                    </ng-template>
                  </button>
                </td>
              </tr>
              <tr *ngIf="isExpanded(order)">
                <td colspan="8">
                  <div class="order-detail">
                    <div>
                      <p class="detail-label">Subtotal</p>
                      <p class="detail-value">{{ formatMoney(order.subtotal, order.currencySymbol) }}</p>
                    </div>
                    <div>
                      <p class="detail-label">Impuesto</p>
                      <p class="detail-value">{{ formatMoney(order.taxAmount, order.currencySymbol) }}</p>
                    </div>
                    <div>
                      <p class="detail-label">Propina</p>
                      <p class="detail-value">{{ formatMoney(order.tipAmount, order.currencySymbol) }}</p>
                    </div>
                    <div>
                      <p class="detail-label">Descuento</p>
                      <p class="detail-value">{{ formatMoney(order.discountAmount, order.currencySymbol) }}</p>
                    </div>
                    <div>
                      <p class="detail-label">Descuentos posteriores</p>
                      <p class="detail-value">Pendiente de implementacion.</p>
                    </div>
                  </div>
                  <div class="order-status-actions" *ngIf="canManageStatus && !order.deleted">
                    <p class="detail-label">Cambiar estado</p>
                    <div class="status-actions">
                      <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus(order, 'CREADA')" title="Creada" aria-label="Creada">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                          <path d="M12 6V18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                          <path d="M6 12H18" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                        </svg>
                      </button>
                      <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus(order, 'PREPARANDOSE')" title="Preparandose" aria-label="Preparandose">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                          <path d="M12 8V12L14.5 13.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                          <path d="M3.5 12C3.5 7.30558 7.30558 3.5 12 3.5C16.6944 3.5 20.5 7.30558 20.5 12C20.5 16.6944 16.6944 20.5 12 20.5C7.30558 20.5 3.5 16.6944 3.5 12Z" stroke="currentColor" stroke-width="1.5"></path>
                        </svg>
                      </button>
                      <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus(order, 'DESPACHADA')" title="Despachada" aria-label="Despachada">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                          <path d="M3 7H15V17H3V7Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                          <path d="M15 10H19L21 12V17H15V10Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                          <path d="M7 17V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                          <path d="M17 17V19" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"></path>
                        </svg>
                      </button>
                      <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus(order, 'PAGADA')" title="Pagada" aria-label="Pagada">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                          <path d="M5 13L9 17L19 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                        </svg>
                      </button>
                      <button class="btn btn-ghost btn-sm icon-btn" type="button" (click)="setStatus(order, 'INACTIVA')" title="Inactiva" aria-label="Inactiva">
                        <svg viewBox="0 0 24 24" fill="none" aria-hidden="true">
                          <path d="M6 6H8V18H6V6Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                          <path d="M16 6H18V18H16V6Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                        </svg>
                      </button>
                    </div>
                  </div>
                </td>
              </tr>
            </ng-container>
            <tr *ngIf="orders.length === 0">
              <td colspan="8">
                <div class="empty-state">
                  <p>No hay ordenes registradas.</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  `
})
export class OrdersListPageComponent implements OnInit {
  private ordersApi = inject(OrdersApiService);
  private mesasApi = inject(MesasApiService);
  private menuApi = inject(MenuApiService);
  private discountsApi = inject(DiscountsApiService);
  private snackBar = inject(MatSnackBar);
  private router = inject(Router);
  private authService = inject(AuthService);
  private voucherService = inject(OrderVoucherService);
  orders: OrderResponse[] = [];
  channels: OrderChannelDto[] = [];
  mesas: MesaDto[] = [];
  menuItems: MenuItemDto[] = [];
  discountItems: DiscountItemDto[] = [];
  billingFields: OrderBillingFieldDto[] = [];
  expandedOrderId: string | null = null;
  isSuperadmin = this.authService.hasRole(['SUPERADMINISTRADOR']);
  canManageStatus = this.authService.hasRole(['SUPERADMINISTRADOR', 'ADMINISTRADOR']);

  /**
   * Carga la lista inicial.
   */
  ngOnInit() {
    this.loadOrders();
    this.loadMesas();
    this.loadChannels();
    this.loadMenuItems();
    this.loadDiscountItems();
    this.loadBillingFields();
  }

  /**
   * Alterna la vista de detalle para una orden.
   *
   * @param order orden objetivo.
   */
  toggleDetails(order: OrderResponse) {
    this.expandedOrderId = this.expandedOrderId === order.id ? null : order.id;
  }

  /**
   * Edita una orden existente.
   *
   * @param order orden objetivo.
   */
  editOrder(order: OrderResponse) {
    this.router.navigate(['/ordenes', order.id, 'edit']);
  }

  /**
   * Cambia el estado de una orden.
   *
   * @param order orden objetivo.
   * @param status estado destino.
   */
  setStatus(order: OrderResponse, status: OrderStatus) {
    this.ordersApi.changeStatus(order.id, status).subscribe({
      next: () => {
        this.snackBar.open('Estado actualizado', 'Cerrar', { duration: 3000 });
        this.loadOrders();
      },
      error: (error) => this.snackBar.open(error?.error?.error || 'No se pudo actualizar el estado', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Elimina o restaura una orden.
   *
   * @param order orden objetivo.
   */
  toggleDelete(order: OrderResponse) {
    if (!order.deleted && !confirm(`Eliminar la orden #${this.orderNumberDisplay(order.orderNumber)}?`)) {
      return;
    }
    if (order.deleted) {
      this.ordersApi.restore(order.id).subscribe({
        next: () => {
          this.snackBar.open('Orden restaurada', 'Cerrar', { duration: 3000 });
          this.loadOrders();
        },
        error: (error: unknown) =>
          this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo restaurar la orden', 'Cerrar', { duration: 3000 })
      });
      return;
    }

    this.ordersApi.delete(order.id).subscribe({
      next: () => {
        this.snackBar.open('Orden eliminada', 'Cerrar', { duration: 3000 });
        this.loadOrders();
      },
      error: (error: unknown) =>
        this.snackBar.open((error as { error?: { error?: string } })?.error?.error || 'No se pudo eliminar la orden', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Indica si la orden esta expandida.
   *
   * @param order orden objetivo.
   */
  isExpanded(order: OrderResponse) {
    return this.expandedOrderId === order.id;
  }

  /**
   * Formatea el numero de orden.
   *
   * @param orderNumber numero base.
   */
  orderNumberDisplay(orderNumber?: number) {
    if (!orderNumber) {
      return '-----';
    }
    return String(orderNumber).padStart(5, '0');
  }

  /**
   * Formatea valores monetarios.
   *
   * @param value valor bruto.
   * @param symbol simbolo de moneda.
   */
  formatMoney(value: number, symbol: string) {
    const formatted = new Intl.NumberFormat('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value);
    return `${symbol}${formatted}`;
  }

  /**
   * Obtiene la descripcion de la mesa.
   *
   * @param mesaId id de la mesa.
   */
  mesaLabel(mesaId: string) {
    return this.mesas.find((mesa) => mesa.id === mesaId)?.description ?? mesaId;
  }

  /**
   * Obtiene el nombre del canal.
   *
   * @param channelId id del canal.
   */
  channelLabel(channelId: string) {
    return this.channels.find((channel) => channel.id === channelId)?.name ?? channelId;
  }

  /**
   * Totaliza los items solicitados.
   *
   * @param order orden objetivo.
   */
  totalItems(order: OrderResponse) {
    return order.items?.reduce((sum, item) => sum + (item.quantity || 0), 0) ?? 0;
  }

  /**
   * Regenera el voucher PDF desde el listado.
   *
   * @param order orden objetivo.
   */
  regenerateVoucher(order: OrderResponse) {
    const mesero = this.authService.getUser()?.nombres ?? 'Mesero';
    const billingFields = this.billingFields
      .filter((field) => field.active && !field.deleted)
      .sort((a, b) => a.sortOrder - b.sortOrder)
      .map((field) => field.label);

    this.ordersApi.listOrderCoupons(order.id).subscribe({
      next: (coupons) => {
        this.voucherService.generateVoucher({
          date: new Date(),
          orderNumber: this.orderNumberDisplay(order.orderNumber),
          mesa: this.mesaLabel(order.mesaId),
          mesero,
          canal: this.channelLabel(order.channelId),
          currencySymbol: order.currencySymbol,
          items: (order.items ?? []).map((line) => ({
            name: this.menuLabel(line.menuItemId),
            unitPrice: Number(line.unitPrice),
            quantity: line.quantity,
            total: Number(line.totalPrice)
          })),
          discountsApplied: (order.discountItems ?? []).map((line) => ({
            name: this.discountLabel(line.discountItemId),
            type: line.discountType === 'PERCENTAGE' ? 'Porcentaje' : 'Fijo',
            unitValue: line.discountType === 'PERCENTAGE' ? `${line.unitValue}%` : this.formatMoney(line.unitValue, order.currencySymbol),
            total: Number(line.totalValue)
          })),
          coupons,
          subtotal: order.subtotal,
          discountAmount: order.discountAmount,
          tipAmount: order.tipAmount,
          totalAmount: order.totalAmount,
          billingFields
        });
      },
      error: () => {
        this.voucherService.generateVoucher({
          date: new Date(),
          orderNumber: this.orderNumberDisplay(order.orderNumber),
          mesa: this.mesaLabel(order.mesaId),
          mesero,
          canal: this.channelLabel(order.channelId),
          currencySymbol: order.currencySymbol,
          items: (order.items ?? []).map((line) => ({
            name: this.menuLabel(line.menuItemId),
            unitPrice: Number(line.unitPrice),
            quantity: line.quantity,
            total: Number(line.totalPrice)
          })),
          discountsApplied: (order.discountItems ?? []).map((line) => ({
            name: this.discountLabel(line.discountItemId),
            type: line.discountType === 'PERCENTAGE' ? 'Porcentaje' : 'Fijo',
            unitValue: line.discountType === 'PERCENTAGE' ? `${line.unitValue}%` : this.formatMoney(line.unitValue, order.currencySymbol),
            total: Number(line.totalValue)
          })),
          subtotal: order.subtotal,
          discountAmount: order.discountAmount,
          tipAmount: order.tipAmount,
          totalAmount: order.totalAmount,
          billingFields
        });
      }
    });
  }

  /**
   * Convierte el estado a etiqueta.
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
   * Devuelve la clase CSS del estado.
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

  /**
   * Solicita el listado de ordenes.
   */
  private loadOrders() {
    this.ordersApi.list().subscribe({
      next: (orders) => (this.orders = orders),
      error: () => this.snackBar.open('No se pudo cargar ordenes', 'Cerrar', { duration: 3000 })
    });
  }

  private loadMesas() {
    this.mesasApi.list(this.isSuperadmin).subscribe({
      next: (mesas) => (this.mesas = mesas),
      error: () => this.snackBar.open('No se pudo cargar mesas', 'Cerrar', { duration: 3000 })
    });
  }

  private loadChannels() {
    this.ordersApi.listChannels(this.isSuperadmin).subscribe({
      next: (channels) => (this.channels = channels),
      error: () => this.snackBar.open('No se pudo cargar canales', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Carga el catalogo de menu para etiquetas.
   */
  private loadMenuItems() {
    this.menuApi.list({ includeDeleted: true }).subscribe({
      next: (items) => (this.menuItems = items),
      error: () => this.snackBar.open('No se pudo cargar menu', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Carga el catalogo de descuentos para etiquetas.
   */
  private loadDiscountItems() {
    this.discountsApi.list({ includeDeleted: true }).subscribe({
      next: (items) => (this.discountItems = items),
      error: () => this.snackBar.open('No se pudo cargar descuentos', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Carga los campos configurados de facturacion.
   */
  private loadBillingFields() {
    this.ordersApi.listBillingFields(false).subscribe({
      next: (fields) => (this.billingFields = fields),
      error: () => this.snackBar.open('No se pudo cargar campos de facturacion', 'Cerrar', { duration: 3000 })
    });
  }

  /**
   * Resuelve la etiqueta del menu.
   *
   * @param menuItemId id del item.
   */
  private menuLabel(menuItemId: string) {
    return this.menuItems.find((item) => item.id === menuItemId)?.nickname ?? 'Item eliminado';
  }

  /**
   * Resuelve la etiqueta del descuento.
   *
   * @param discountItemId id del descuento.
   */
  private discountLabel(discountItemId: string) {
    return this.discountItems.find((item) => item.id === discountItemId)?.nickname ?? 'Descuento eliminado';
  }
}
