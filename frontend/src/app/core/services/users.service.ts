import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';

/**
 * Modelo de usuario expuesto por la API.
 */
export interface UserDto {
  id: string;
  nombres: string;
  correo: string;
  username: string;
  rol: string;
  activo: boolean;
  deleted: boolean;
  deletedAt?: string;
  deletedBy?: string;
}

/**
 * Estructura de paginación para la lista de usuarios.
 */
export interface UserPage {
  content: UserDto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

/**
 * Servicio para consumir endpoints relacionados con usuarios.
 */
@Injectable({ providedIn: 'root' })
export class UsersService {
  /**
   * Crea el servicio de usuarios.
   *
   * @param http cliente HTTP para consumir la API.
   */
  constructor(private http: HttpClient) {}

  /**
   * Obtiene la lista paginada de usuarios con filtros opcionales.
   *
   * @param params filtros y paginación.
   */
  list(params: { rol?: string; activo?: boolean; deleted?: boolean; page?: number; size?: number }) {
    let httpParams = new HttpParams();
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null) {
        httpParams = httpParams.set(key, value.toString());
      }
    });
    console.info('[UsersService] GET /users', { params });
    return this.http.get<UserPage>(`${environment.apiUrl}/users`, { params: httpParams });
  }

  /**
   * Obtiene un usuario por identificador.
   *
   * @param id identificador del usuario.
   */
  getById(id: string) {
    console.info('[UsersService] GET /users/:id', { id });
    return this.http.get<UserDto>(`${environment.apiUrl}/users/${id}`);
  }

  /**
   * Crea un nuevo usuario.
   *
   * @param payload datos de creación.
   */
  create(payload: {
    nombres: string;
    correo: string;
    username: string;
    password: string;
    confirmPassword: string;
    rol: string;
  }) {
    console.info('[UsersService] POST /users', {
      nombres: payload.nombres,
      correo: payload.correo,
      username: payload.username,
      rol: payload.rol,
      passwordProvided: Boolean(payload.password),
      confirmPasswordProvided: Boolean(payload.confirmPassword)
    });
    return this.http.post<UserDto>(`${environment.apiUrl}/users`, payload);
  }

  /**
   * Actualiza un usuario existente.
   *
   * @param id identificador del usuario.
   * @param payload datos de actualización.
   */
  update(id: string, payload: {
    nombres: string;
    correo: string;
    username: string;
    rol: string;
    activo: boolean;
  }) {
    console.info('[UsersService] PUT /users/:id', { id, ...payload });
    return this.http.put<UserDto>(`${environment.apiUrl}/users/${id}`, payload);
  }

  /**
   * Marca un usuario como eliminado de forma lógica.
   *
   * @param id identificador del usuario.
   */
  softDelete(id: string) {
    console.info('[UsersService] POST /users/:id/soft-delete', { id });
    return this.http.post<UserDto>(`${environment.apiUrl}/users/${id}/soft-delete`, {});
  }

  /**
   * Restaura un usuario eliminado lógicamente.
   *
   * @param id identificador del usuario.
   */
  restore(id: string) {
    console.info('[UsersService] POST /users/:id/restore', { id });
    return this.http.post<UserDto>(`${environment.apiUrl}/users/${id}/restore`, {});
  }

  /**
   * Elimina un usuario de forma definitiva.
   *
   * @param id identificador del usuario.
   */
  delete(id: string) {
    console.info('[UsersService] DELETE /users/:id', { id });
    return this.http.delete<void>(`${environment.apiUrl}/users/${id}`);
  }

  /**
   * Obtiene el perfil del usuario autenticado.
   */
  getMe() {
    console.info('[UsersService] GET /users/me');
    return this.http.get<UserDto>(`${environment.apiUrl}/users/me`);
  }

  /**
   * Actualiza el perfil del usuario autenticado.
   *
   * @param payload datos de actualización.
   */
  updateMe(payload: {
    nombres: string;
    correo: string;
    username: string;
    password?: string;
    confirmPassword?: string;
  }) {
    console.info('[UsersService] PUT /users/me', {
      nombres: payload.nombres,
      correo: payload.correo,
      username: payload.username,
      passwordProvided: Boolean(payload.password),
      confirmPasswordProvided: Boolean(payload.confirmPassword)
    });
    return this.http.put<UserDto>(`${environment.apiUrl}/users/me`, payload);
  }
}
