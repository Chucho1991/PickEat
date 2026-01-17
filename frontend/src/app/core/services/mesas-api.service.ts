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
  status: string;
}

/**
 * Servicio de API para el módulo de mesas.
 */
@Injectable({ providedIn: 'root' })
export class MesasApiService {
  private baseUrl = `${environment.apiUrl}/mesas`;

  /**
   * Crea el servicio con HTTP.
   *
   * @param http cliente HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Lista las mesas disponibles.
   */
  list(): Observable<MesaDto[]> {
    return this.http.get<MesaDto[]>(this.baseUrl);
  }
}
