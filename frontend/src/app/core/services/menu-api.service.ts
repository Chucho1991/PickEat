import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Representa un ítem del menú.
 */
export interface MenuItemDto {
  id: string;
  longDescription: string;
  shortDescription: string;
  nickname: string;
  dishType: string;
  status: string;
  price: number;
  imagePath?: string | null;
  createdAt: string;
  updatedAt: string;
}

/**
 * Solicitud para crear o actualizar ítems.
 */
export interface MenuItemRequest {
  longDescription: string;
  shortDescription: string;
  nickname: string;
  dishType: string;
  status: string;
  price: number;
}

/**
 * Servicio de API para el módulo de menú.
 */
@Injectable({ providedIn: 'root' })
export class MenuApiService {
  private baseUrl = `${environment.apiUrl}/menu-items`;

  /**
   * Crea el servicio con HTTP.
   *
   * @param http cliente HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Lista ítems del menú con filtros.
   */
  list(filters: { dishType?: string; status?: string; search?: string }): Observable<MenuItemDto[]> {
    let params = new HttpParams();
    if (filters.dishType) {
      params = params.set('dishType', filters.dishType);
    }
    if (filters.status) {
      params = params.set('status', filters.status);
    }
    if (filters.search) {
      params = params.set('search', filters.search);
    }
    return this.http.get<MenuItemDto[]>(this.baseUrl, { params });
  }

  /**
   * Obtiene un ítem por id.
   */
  getById(id: string): Observable<MenuItemDto> {
    return this.http.get<MenuItemDto>(`${this.baseUrl}/${id}`);
  }

  /**
   * Crea un nuevo ítem.
   */
  create(request: MenuItemRequest): Observable<MenuItemDto> {
    return this.http.post<MenuItemDto>(this.baseUrl, request);
  }

  /**
   * Actualiza un ítem.
   */
  update(id: string, request: MenuItemRequest): Observable<MenuItemDto> {
    return this.http.put<MenuItemDto>(`${this.baseUrl}/${id}`, request);
  }

  /**
   * Cambia el estado del ítem.
   */
  changeStatus(id: string, status: string): Observable<MenuItemDto> {
    return this.http.post<MenuItemDto>(`${this.baseUrl}/${id}/status`, { status });
  }

  /**
   * Sube una imagen para el ítem.
   */
  uploadImage(id: string, file: File): Observable<MenuItemDto> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<MenuItemDto>(`${this.baseUrl}/${id}/image`, formData);
  }
}
