import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

const authEndpoints = ['/api/v1/auth/'];

export const authHttpInterceptor: HttpInterceptorFn = (request, next) => {
  if (!request.url.startsWith('/api/')) {
    return next(request);
  }

  const auth = inject(AuthService);
  const router = inject(Router);
  const apiRequest = request.clone({ withCredentials: true });

  return next(apiRequest).pipe(
    catchError((error: unknown) => {
      const isAuthRequest = authEndpoints.some(endpoint => request.url.startsWith(endpoint));
      if (error instanceof HttpErrorResponse && error.status === 401 && !isAuthRequest) {
        auth.clearSession();
        void router.navigate(['/login'], { queryParams: { returnUrl: router.url } });
      }
      return throwError(() => error);
    })
  );
};
