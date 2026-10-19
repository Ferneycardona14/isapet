import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  CrearPlanRequest,
  CrearVersionPlanRequest,
  Plan,
  VersionPlan
} from '../models/plan.models';

@Injectable({ providedIn: 'root' })
export class PlanApiService {
  private readonly http = inject(HttpClient);
  private readonly endpoint = '/api/v1/admin/planes';

  listarPlanes(): Observable<Plan[]> {
    return this.http.get<Plan[]>(this.endpoint);
  }

  obtenerPlan(planId: number): Observable<Plan> {
    return this.http.get<Plan>(`${this.endpoint}/${planId}`);
  }

  obtenerVersionAplicable(planId: number, fecha: string): Observable<VersionPlan> {
    const params = new HttpParams().set('fecha', fecha);
    return this.http.get<VersionPlan>(`${this.endpoint}/${planId}/version-aplicable`, { params });
  }

  crearPlan(request: CrearPlanRequest): Observable<Plan> {
    return this.http.post<Plan>(this.endpoint, request);
  }

  publicarVersion(planId: number, request: CrearVersionPlanRequest): Observable<Plan> {
    return this.http.post<Plan>(`${this.endpoint}/${planId}/versiones`, request);
  }

  desactivarPlan(planId: number): Observable<Plan> {
    return this.http.patch<Plan>(`${this.endpoint}/${planId}/desactivar`, {});
  }
}
