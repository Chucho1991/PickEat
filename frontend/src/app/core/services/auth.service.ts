import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

/**
 * Representa un usuario autenticado persistido en el cliente.
 */
export interface AuthUser {
  id: string;
  nombres: string;
  username: string;
  rol: string;
}

/**
 * Respuesta esperada del endpoint de autenticación.
 */
export interface AuthResponse {
  token: string;
  id: string;
  nombres: string;
  username: string;
  rol: string;
}

/**
 * Servicio para autenticación y manejo de sesión en el cliente.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'pickeat_token';
  private userKey = 'pickeat_user';
  private authSignal = signal(this.getToken() !== null);

  /**
   * Crea el servicio de autenticación.
   *
   * @param http cliente HTTP para consumir la API.
   * @param router router para redirigir en cierre de sesión.
   */
  constructor(private http: HttpClient, private router: Router) {}

  /**
   * Solicita el inicio de sesión con credenciales básicas.
   *
   * @param usernameOrEmail nombre de usuario o correo.
   * @param password contraseña en texto plano.
   */
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

  /**
   * Persiste el token y los datos del usuario autenticado.
   *
   * @param response respuesta del servicio de autenticación.
   */
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

  /**
   * Elimina la sesión local y redirige al login.
   */
  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    this.authSignal.set(false);
    this.router.navigate(['/login']);
  }

  /**
   * Obtiene el token almacenado localmente.
   */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /**
   * Obtiene los datos del usuario almacenados localmente.
   */
  getUser(): AuthUser | null {
    const data = localStorage.getItem(this.userKey);
    return data ? JSON.parse(data) as AuthUser : null;
  }

  /**
   * Indica si existe una sesión válida en el cliente.
   */
  isAuthenticated(): boolean {
    return this.authSignal();
  }

  /**
   * Verifica si el usuario autenticado tiene alguno de los roles indicados.
   *
   * @param roles roles permitidos.
   */
  hasRole(roles: string[]): boolean {
    const user = this.getUser();
    return user ? roles.includes(user.rol) : false;
  }
}
