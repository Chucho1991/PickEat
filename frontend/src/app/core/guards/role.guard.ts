import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Guard que restringe el acceso a rutas según roles permitidos.
 */
@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  /**
   * Crea el guard de roles.
   *
   * @param authService servicio de autenticación.
   * @param router router para redirecciones.
   */
  constructor(private authService: AuthService, private router: Router) {}

  /**
   * Valida si el usuario posee uno de los roles configurados en la ruta.
   *
   * @param route snapshot con metadata de roles.
   */
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const roles = route.data['roles'] as string[] | undefined;
    if (!roles) {
      return true;
    }
    if (!this.authService.hasRole(roles)) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
