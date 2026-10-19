import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, switchMap, tap, throwError } from 'rxjs';

export interface AuthSession {
  username: string;
  roles: string[];
}

export interface LoginCredentials {
  username: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly authEndpoint = '/api/v1/auth';
  private readonly sessionSubject = new BehaviorSubject<AuthSession | null>(null);

  readonly session$ = this.sessionSubject.asObservable();

  checkSession(): Observable<AuthSession | null> {
    return this.http.get<AuthSession>(`${this.authEndpoint}/session`, { withCredentials: true }).pipe(
      tap(session => this.sessionSubject.next(session)),
      catchError((error: unknown) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          this.clearSession();
          return of(null);
        }
        this.clearSession();
        return throwError(() => error);
      })
    );
  }

  login(credentials: LoginCredentials): Observable<AuthSession> {
    return this.obtainCsrfToken().pipe(
      switchMap(() => this.http.post<AuthSession>(
        `${this.authEndpoint}/login`,
        credentials,
        { withCredentials: true }
      )),
      switchMap(() => this.obtainCsrfToken()),
      switchMap(() => this.checkSession()),
      switchMap(session => session
        ? of(session)
        : throwError(() => new Error('No se pudo confirmar la sesión iniciada.'))),
      tap(session => this.sessionSubject.next(session))
    );
  }

  logout(): Observable<void> {
    return this.obtainCsrfToken().pipe(
      switchMap(() => this.http.post<void>(
        `${this.authEndpoint}/logout`,
        {},
        { withCredentials: true }
      )),
      catchError((error: unknown) => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return of(undefined);
        }
        return throwError(() => error);
      }),
      tap(() => this.clearSession()),
      map(() => undefined)
    );
  }

  clearSession(): void {
    this.sessionSubject.next(null);
  }

  private obtainCsrfToken(): Observable<void> {
    return this.http.get<void>(`${this.authEndpoint}/csrf`, { withCredentials: true });
  }
}
