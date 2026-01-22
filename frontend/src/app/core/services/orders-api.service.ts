import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
 * Item solicitado para una orden.
 */
export interface OrderItemRequest {
  menuItemId: string;
  quantity: number;
}

/**
 * Solicitud de creacion de ordenes.
 */
export interface OrderCreateRequest {
  mesaId: string;
  items: OrderItemRequest[];
}

/**
 * Respuesta de una orden creada.
 */
export interface OrderResponse {
  id: string;
  orderNumber: number;
  mesaId: string;
  items: Array<{
    menuItemId: string;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
  }>;
  subtotal: number;
  taxAmount: number;
  tipAmount: number;
  discountAmount: number;
  totalAmount: number;
  currencyCode: string;
  currencySymbol: string;
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
   * Crea una orden nueva.
   */
  create(request: OrderCreateRequest): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(this.baseUrl, request);
  }
}
