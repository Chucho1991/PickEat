import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

export interface AuthUser {
  id: string;
  nombres: string;
  username: string;
  rol: string;
}

export interface AuthResponse {
  token: string;
  id: string;
  nombres: string;
  username: string;
  rol: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'pickeat_token';
  private userKey = 'pickeat_user';
  private authSignal = signal(this.getToken() !== null);

  constructor(private http: HttpClient, private router: Router) {}

  login(usernameOrEmail: string, password: string) {
    console.info('[AuthService] POST /auth/login', {
      usernameOrEmail,
      passwordProvided: Boolean(password)
    });
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, {
      usernameOrEmail,
      password
    });
  }

  handleLogin(response: AuthResponse) {
    localStorage.setItem(this.tokenKey, response.token);
    localStorage.setItem(this.userKey, JSON.stringify({
      id: response.id,
      nombres: response.nombres,
      username: response.username,
      rol: response.rol
    }));
    this.authSignal.set(true);
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.authSignal.set(false);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getUser(): AuthUser | null {
    const data = localStorage.getItem(this.userKey);
    return data ? JSON.parse(data) as AuthUser : null;
  }

  isAuthenticated(): boolean {
    return this.authSignal();
  }

  hasRole(roles: string[]): boolean {
    const user = this.getUser();
    return user ? roles.includes(user.rol) : false;
  }
}
