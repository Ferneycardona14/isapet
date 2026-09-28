export interface CrearAfiliadoRequest {
  numeroIdentificacion: string;
  nombres: string;
  apellidos: string;
  celular: string;
  correo: string;
  direccion: string;
}

export interface CrearAfiliadoResponse {
  id: number;
  numeroIdentificacion: string;
  nombres: string;
  apellidos: string;
  celular: string;
  correo: string;
  direccion: string;
}

export interface ActualizarAfiliadoRequest {
  nombres: string;
  apellidos: string;
  celular: string;
  correo: string;
  direccion: string;
}

export interface AfiliadoDetalle extends CrearAfiliadoResponse {
}

export interface AfiliadoListaItem extends AfiliadoDetalle {
  cantidadMascotas: number;
}

export interface Pagina<T> {
  content: T[];
  page: {
    number: number;
    size: number;
    totalElements: number;
    totalPages: number;
  };
}

export interface CrearMascotaRequest {
  nombre: string;
  especie: string;
  raza: string;
  edad: number;
  color: string;
}

export interface CrearMascotaResponse extends CrearMascotaRequest {
  id: number;
  afiliadoId: number;
}

export interface ApiErrorResponse {
  status: number;
  message: string;
  errors: string[];
}

export const ESPECIES_MASCOTA = [
  'Perro',
  'Gato',
  'Ave',
  'Conejo',
  'Hámster',
  'Pez',
  'Tortuga',
  'Otro'
] as const;