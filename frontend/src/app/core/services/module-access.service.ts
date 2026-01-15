import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Representa la configuración de un módulo para un rol.
 */
export interface ModuleAccess {
  key: string;
  label: string;
  enabled: boolean;
}

/**
 * Servicio para consultar y actualizar módulos disponibles por rol.
 */
@Injectable({ providedIn: 'root' })
export class ModuleAccessService {
  /**
   * Crea el servicio con el cliente HTTP requerido.
   *
   * @param http cliente HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene los módulos habilitados para el rol autenticado.
   */
  getModulesForCurrentRole(): Observable<ModuleAccess[]> {
    return this.http.get<ModuleAccess[]>(`${environment.apiUrl}/role-modules/me`);
  }

  /**
   * Obtiene la configuración de módulos para un rol específico.
   *
   * @param role rol a consultar.
   */
  getModulesForRole(role: string): Observable<ModuleAccess[]> {
    return this.http.get<ModuleAccess[]>(`${environment.apiUrl}/role-modules/${role}`);
  }

  /**
   * Actualiza la configuración de módulos para un rol específico.
   *
   * @param role rol a actualizar.
   * @param modules módulos configurados.
   */
  updateRoleModules(role: string, modules: ModuleAccess[]): Observable<void> {
    return this.http.put<void>(`${environment.apiUrl}/role-modules/${role}`, { modules });
  }
}
