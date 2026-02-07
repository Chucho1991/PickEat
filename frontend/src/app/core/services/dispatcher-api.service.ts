import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Elemento del tablero de despachador.
 */
export interface DispatcherItemDto {
  orderId: string;
  mesa: string;
  plato: string;
  cantidad: number;
  addedAt: string | null;
}

/**
 * Servicio para consultar el tablero de despachador.
 */
@Injectable({ providedIn: 'root' })
export class DispatcherApiService {
  constructor(private http: HttpClient) {}

  /**
   * Lista los platos registrados en MongoDB para el tablero del despachador.
   */
  listDispatcherItems(): Observable<DispatcherItemDto[]> {
    return this.http.get<DispatcherItemDto[]>(`${environment.apiUrl}/despachador/comandas`);
  }
}
