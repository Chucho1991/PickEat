import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';

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

export interface UserPage {
  content: UserDto[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({ providedIn: 'root' })
export class UsersService {
  constructor(private http: HttpClient) {}

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

  getById(id: string) {
    console.info('[UsersService] GET /users/:id', { id });
    return this.http.get<UserDto>(`${environment.apiUrl}/users/${id}`);
  }

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

  softDelete(id: string) {
    console.info('[UsersService] POST /users/:id/soft-delete', { id });
    return this.http.post<UserDto>(`${environment.apiUrl}/users/${id}/soft-delete`, {});
  }

  restore(id: string) {
    console.info('[UsersService] POST /users/:id/restore', { id });
    return this.http.post<UserDto>(`${environment.apiUrl}/users/${id}/restore`, {});
  }

  delete(id: string) {
    console.info('[UsersService] DELETE /users/:id', { id });
    return this.http.delete<void>(`${environment.apiUrl}/users/${id}`);
  }

  getMe() {
    console.info('[UsersService] GET /users/me');
    return this.http.get<UserDto>(`${environment.apiUrl}/users/me`);
  }

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
