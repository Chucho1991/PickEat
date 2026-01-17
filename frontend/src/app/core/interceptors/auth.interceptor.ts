import { HttpEventType, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { tap } from 'rxjs/operators';

/**
 * Interceptor que adjunta el token JWT en cada solicitud saliente.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  console.log('[HTTP] request', req.method, req.urlWithParams);
  return next(req).pipe(
    tap({
      next: (event) => {
        if (event.type === HttpEventType.Response) {
          console.log('[HTTP] response', req.method, req.urlWithParams, event.status);
        }
      },
      error: (error) => {
        console.log('[HTTP] error', req.method, req.urlWithParams, error?.status ?? 'unknown');
      }
    })
  );
};
