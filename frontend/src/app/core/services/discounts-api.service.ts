import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Representa un descuento.
 */
export interface DiscountItemDto {
  id: string;
  longDescription: string;
  shortDescription: string;
  nickname: string;
  discountType: 'FIXED' | 'PERCENTAGE';
  value: number;
  activo: boolean;
  deleted: boolean;
  imagePath?: string | null;
  createdAt: string;
  updatedAt: string;
}

/**
 * Solicitud para crear o actualizar un descuento.
 */
export interface DiscountItemRequest {
  longDescription: string;
  shortDescription: string;
  nickname: string;
  discountType: 'FIXED' | 'PERCENTAGE' | string;
  value: number;
  activo: boolean;
}

/**
 * Servicio de API para el modulo de descuentos.
 */
@Injectable({ providedIn: 'root' })
export class DiscountsApiService {
  private baseUrl = `${environment.apiUrl}/discount-items`;

  constructor(private http: HttpClient) {}

  /**
   * Lista descuentos con filtros.
   */
  list(filters: { activo?: boolean; search?: string; includeDeleted?: boolean }): Observable<DiscountItemDto[]> {
    let params = new HttpParams();
    if (filters.activo !== undefined) {
      params = params.set('activo', String(filters.activo));
    }
    if (filters.search) {
      params = params.set('search', filters.search);
    }
    if (filters.includeDeleted) {
      params = params.set('includeDeleted', 'true');
    }
    return this.http.get<DiscountItemDto[]>(this.baseUrl, { params });
  }

  getById(id: string): Observable<DiscountItemDto> {
    return this.http.get<DiscountItemDto>(`${this.baseUrl}/${id}`);
  }

  create(request: DiscountItemRequest): Observable<DiscountItemDto> {
    return this.http.post<DiscountItemDto>(this.baseUrl, request);
  }

  update(id: string, request: DiscountItemRequest): Observable<DiscountItemDto> {
    return this.http.put<DiscountItemDto>(`${this.baseUrl}/${id}`, request);
  }

  changeActive(id: string, activo: boolean): Observable<DiscountItemDto> {
    return this.http.post<DiscountItemDto>(`${this.baseUrl}/${id}/active`, { activo });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  restore(id: string): Observable<DiscountItemDto> {
    return this.http.post<DiscountItemDto>(`${this.baseUrl}/${id}/restore`, {});
  }

  uploadImage(id: string, file: File): Observable<DiscountItemDto> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<DiscountItemDto>(`${this.baseUrl}/${id}/image`, formData);
  }
}
