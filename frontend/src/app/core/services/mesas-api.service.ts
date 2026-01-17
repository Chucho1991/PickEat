import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Representa una mesa.
 */
export interface MesaDto {
  id: string;
  description: string;
  seats: number;
  activo: boolean;
  deleted: boolean;
}

/**
 * Solicitud para crear o actualizar mesas.
 */
export interface MesaRequest {
  description: string;
  seats: number;
  activo: boolean;
}

/**
 * Servicio de API para el modulo de mesas.
 */
@Injectable({ providedIn: 'root' })
export class MesasApiService {
  private baseUrl = `${environment.apiUrl}/mesas`;

  constructor(private http: HttpClient) {}

  list(includeDeleted?: boolean): Observable<MesaDto[]> {
    const params = includeDeleted ? { includeDeleted: 'true' } : undefined;
    return this.http.get<MesaDto[]>(this.baseUrl, { params });
  }

  getById(id: string): Observable<MesaDto> {
    return this.http.get<MesaDto>(`${this.baseUrl}/${id}`);
  }

  create(request: MesaRequest): Observable<MesaDto> {
    return this.http.post<MesaDto>(this.baseUrl, request);
  }

  update(id: string, request: MesaRequest): Observable<MesaDto> {
    return this.http.put<MesaDto>(`${this.baseUrl}/${id}`, request);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  /**
   * Restaura una mesa eliminada lógicamente.
   */
  restore(id: string): Observable<MesaDto> {
    return this.http.post<MesaDto>(`${this.baseUrl}/${id}/restore`, {});
  }

  changeActive(id: string, activo: boolean): Observable<MesaDto> {
    return this.http.post<MesaDto>(`${this.baseUrl}/${id}/active`, { activo });
  }
}
