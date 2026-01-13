import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

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
