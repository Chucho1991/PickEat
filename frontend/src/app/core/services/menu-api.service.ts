import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Representa un item del menu.
 */
export interface MenuItemDto {
  id: string;
  longDescription: string;
  shortDescription: string;
  nickname: string;
  dishType: string;
  activo: boolean;
  deleted: boolean;
  price: number;
  imagePath?: string | null;
  createdAt: string;
  updatedAt: string;
}

/**
 * Solicitud para crear o actualizar items.
 */
export interface MenuItemRequest {
  longDescription: string;
  shortDescription: string;
  nickname: string;
  dishType: string;
  activo: boolean;
  price: number;
}

/**
 * Servicio de API para el modulo de menu.
 */
@Injectable({ providedIn: 'root' })
export class MenuApiService {
  private baseUrl = `${environment.apiUrl}/menu-items`;

  /** Crea el servicio con HTTP. */
  constructor(private http: HttpClient) {}

  /**
   * Lista items del menu con filtros.
   */
  list(filters: { dishType?: string; activo?: boolean; search?: string; includeDeleted?: boolean }): Observable<MenuItemDto[]> {
    let params = new HttpParams();
    if (filters.dishType) {
      params = params.set('dishType', filters.dishType);
    }
    if (filters.activo !== undefined) {
      params = params.set('activo', String(filters.activo));
    }
    if (filters.search) {
      params = params.set('search', filters.search);
    }
    if (filters.includeDeleted) {
      params = params.set('includeDeleted', 'true');
    }
    return this.http.get<MenuItemDto[]>(this.baseUrl, { params });
  }

  /**
   * Obtiene un item por id.
   */
  getById(id: string): Observable<MenuItemDto> {
    return this.http.get<MenuItemDto>(`${this.baseUrl}/${id}`);
  }

  /**
   * Crea un nuevo item.
   */
  create(request: MenuItemRequest): Observable<MenuItemDto> {
    return this.http.post<MenuItemDto>(this.baseUrl, request);
  }

  /**
   * Actualiza un item.
   */
  update(id: string, request: MenuItemRequest): Observable<MenuItemDto> {
    return this.http.put<MenuItemDto>(`${this.baseUrl}/${id}`, request);
  }

  /**
   * Cambia el estado activo del item.
   */
  changeActive(id: string, activo: boolean): Observable<MenuItemDto> {
    return this.http.post<MenuItemDto>(`${this.baseUrl}/${id}/active`, { activo });
  }

  /**
   * Elimina un item de forma logica.
   */
  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /**
   * Sube una imagen para el item.
   */
  uploadImage(id: string, file: File): Observable<MenuItemDto> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<MenuItemDto>(`${this.baseUrl}/${id}/image`, formData);
  }
}
