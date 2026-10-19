export interface VersionPlan {
  id: number;
  precioMensual: number;
  maximoMascotas: number;
  limiteAtenciones: number | null;
  atencionesIlimitadas: boolean;
  vigenteDesde: string;
  vigenteHastaExclusiva: string | null;
}

export interface Plan {
  id: number;
  codigo: string;
  nombre: string;
  activo: boolean;
  versiones: VersionPlan[];
}

export interface CrearPlanRequest {
  codigo: string;
  nombre: string;
  precioMensual: number;
  maximoMascotas: number;
  limiteAtenciones: number | null;
  vigenteDesde: string;
}

export interface CrearVersionPlanRequest {
  precioMensual: number;
  maximoMascotas: number;
  limiteAtenciones: number | null;
  vigenteDesde: string;
}

export interface ApiErrorResponse {
  status?: number;
  message?: string;
  errors?: string[];
}
