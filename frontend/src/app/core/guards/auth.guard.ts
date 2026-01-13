import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard que exige autenticación para acceder a rutas protegidas.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  /**
   * Crea el guard de autenticación.
   *
   * @param authService servicio de autenticación.
   * @param router router para redirecciones.
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Determina si se permite el acceso según la sesión actual.
   */
  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
