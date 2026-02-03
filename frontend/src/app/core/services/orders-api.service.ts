import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Configuracion usada para calculos de ordenes.
 */
export interface OrderConfigDto {
  taxRate: number;
  tipType: 'PERCENTAGE' | 'FIXED';
  tipValue: number;
  currencyCode: string;
  currencySymbol: string;
}

/**
 * Solicitud para actualizar configuracion de ordenes.
 */
export interface OrderConfigRequest {
  taxRate: number;
  tipValue: number;
}

export type OrderStatus = 'CREADA' | 'PREPARANDOSE' | 'DESPACHADA' | 'PAGADA' | 'INACTIVA' | 'ELIMINADA';

/**
 * Canal de pedidos.
 */
export interface OrderChannelDto {
  id: string;
  name: string;
  activo: boolean;
  deleted: boolean;
  locked: boolean;
  isDefault: boolean;
  createdAt: string;
  updatedAt: string;
}

/**
 * Solicitud para crear o actualizar canales.
 */
export interface OrderChannelRequest {
  name: string;
}

/**
 * Item solicitado para una orden.
 */
export interface OrderItemRequest {
  menuItemId: string;
  quantity: number;
}

/**
 * Descuento solicitado para una orden.
 */
export interface OrderDiscountItemRequest {
  discountItemId: string;
  quantity: number;
}

/**
 * Campo configurable para datos de facturacion.
 */
export interface OrderBillingFieldDto {
  id: string;
  label: string;
  active: boolean;
  deleted: boolean;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

/**
 * Solicitud para crear o actualizar campo de facturacion.
 */
export interface OrderBillingFieldRequest {
  label: string;
  sortOrder?: number;
  active?: boolean;
}

/**
 * Solicitud de creacion de ordenes.
 */
export interface OrderCreateRequest {
  mesaId: string;
  channelId?: string;
  items: OrderItemRequest[];
  discountItems?: OrderDiscountItemRequest[];
  tipType?: 'PERCENTAGE' | 'FIXED';
  tipValue?: number;
  tipEnabled?: boolean;
  billingData?: Record<string, string>;
}

/**
 * Respuesta de una orden creada.
 */
export interface OrderResponse {
  id: string;
  orderNumber: number;
  mesaId: string;
  channelId: string;
  items: Array<{
    menuItemId: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
  }>;
  discountItems: Array<{
    discountItemId: string;
    quantity: number;
    discountType: 'FIXED' | 'PERCENTAGE';
    unitValue: number;
    totalValue: number;
  }>;
  subtotal: number;
  taxAmount: number;
  tipAmount: number;
  discountAmount: number;
  totalAmount: number;
  currencyCode: string;
  currencySymbol: string;
  billingData?: Record<string, string>;
  status: OrderStatus;
  activo: boolean;
  deleted: boolean;
  createdAt: string;
}

/**
 * Servicio de API para ordenes.
 */
@Injectable({ providedIn: 'root' })
export class OrdersApiService {
  private baseUrl = `${environment.apiUrl}/ordenes`;

  constructor(private http: HttpClient) {}

  /**
   * Obtiene la configuracion para calculos.
   */
  getConfig(): Observable<OrderConfigDto> {
    return this.http.get<OrderConfigDto>(`${this.baseUrl}/configuracion`);
  }

  /**
   * Actualiza la configuracion de ordenes.
   */
  updateConfig(request: OrderConfigRequest): Observable<OrderConfigDto> {
    return this.http.post<OrderConfigDto>(`${this.baseUrl}/configuracion`, request);
  }

  /**
   * Crea una orden nueva.
   */
  create(request: OrderCreateRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(this.baseUrl, request);
  }

  /**
   * Actualiza una orden existente.
   */
  update(id: string, request: OrderCreateRequest): Observable<OrderResponse> {
    return this.http.put<OrderResponse>(`${this.baseUrl}/${id}`, request);
  }

  /**
   * Obtiene el listado de ordenes.
   */
  list(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(this.baseUrl);
  }

  /**
   * Obtiene una orden por id.
   */
  getById(id: string): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.baseUrl}/${id}`);
  }

  /**
   * Cambia el estado activo de una orden.
   */
  changeActive(id: string, activo: boolean): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.baseUrl}/${id}/active`, { activo });
  }

  /**
   * Cambia el estado de una orden.
   */
  changeStatus(id: string, status: OrderStatus): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.baseUrl}/${id}/status`, { status });
  }

  /**
   * Elimina una orden de forma logica.
   */
  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /**
   * Restaura una orden eliminada lógicamente.
   */
  restore(id: string): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(`${this.baseUrl}/${id}/restore`, {});
  }

  /**
   * Lista los canales disponibles.
   */
  listChannels(includeDeleted = false): Observable<OrderChannelDto[]> {
    let params = new HttpParams();
    if (includeDeleted) {
      params = params.set('includeDeleted', 'true');
    }
    return this.http.get<OrderChannelDto[]>(`${this.baseUrl}/canales`, { params });
  }

  /**
   * Lista campos de facturacion configurados.
   */
  listBillingFields(includeDeleted = false): Observable<OrderBillingFieldDto[]> {
    let params = new HttpParams();
    if (includeDeleted) {
      params = params.set('includeDeleted', 'true');
    }
    return this.http.get<OrderBillingFieldDto[]>(`${this.baseUrl}/configuracion/facturacion`, { params });
  }

  /**
   * Crea un campo de facturacion.
   */
  createBillingField(request: OrderBillingFieldRequest): Observable<OrderBillingFieldDto> {
    return this.http.post<OrderBillingFieldDto>(`${this.baseUrl}/configuracion/facturacion`, request);
  }

  /**
   * Actualiza un campo de facturacion.
   */
  updateBillingField(id: string, request: OrderBillingFieldRequest): Observable<OrderBillingFieldDto> {
    return this.http.put<OrderBillingFieldDto>(`${this.baseUrl}/configuracion/facturacion/${id}`, request);
  }

  /**
   * Elimina un campo de facturacion de forma logica.
   */
  deleteBillingField(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/configuracion/facturacion/${id}`);
  }

  /**
   * Restaura un campo de facturacion eliminado.
   */
  restoreBillingField(id: string): Observable<OrderBillingFieldDto> {
    return this.http.post<OrderBillingFieldDto>(`${this.baseUrl}/configuracion/facturacion/${id}/restore`, {});
  }

  /**
   * Crea un canal.
   */
  createChannel(request: OrderChannelRequest): Observable<OrderChannelDto> {
    return this.http.post<OrderChannelDto>(`${this.baseUrl}/canales`, request);
  }

  /**
   * Actualiza un canal.
   */
  updateChannel(id: string, request: OrderChannelRequest): Observable<OrderChannelDto> {
    return this.http.put<OrderChannelDto>(`${this.baseUrl}/canales/${id}`, request);
  }

  /**
   * Elimina un canal de forma logica.
   */
  deleteChannel(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/canales/${id}`);
  }

  /**
   * Restaura un canal eliminado.
   */
  restoreChannel(id: string): Observable<OrderChannelDto> {
    return this.http.post<OrderChannelDto>(`${this.baseUrl}/canales/${id}/restore`, {});
  }
}
