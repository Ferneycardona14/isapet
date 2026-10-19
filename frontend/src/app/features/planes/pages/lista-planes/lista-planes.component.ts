import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  ApiErrorResponse,
  CrearPlanRequest,
  CrearVersionPlanRequest,
  Plan,
  VersionPlan
} from '../../models/plan.models';
import { PlanApiService } from '../../services/plan-api.service';

@Component({
  selector: 'app-lista-planes',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './lista-planes.component.html'
})
export class ListaPlanesComponent implements OnInit {
  private readonly api = inject(PlanApiService);

  readonly crearPlanForm = new FormGroup({
    codigo: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(30), Validators.pattern(/^[A-Za-z0-9_-]+$/)]
    }),
    nombre: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    }),
    precioMensual: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9]\d{0,13}$/)]
    }),
    maximoMascotas: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9]\d*$/), Validators.max(2147483647)]
    }),
    limiteAtenciones: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^[1-9]\d{0,9}$/), Validators.max(2147483647)]
    }),
    vigenteDesde: new FormControl(this.fechaLocalActual(), {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  readonly publicarVersionForm = new FormGroup({
    precioMensual: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9]\d{0,13}$/)]
    }),
    maximoMascotas: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.pattern(/^[1-9]\d*$/), Validators.max(2147483647)]
    }),
    limiteAtenciones: new FormControl('', {
      nonNullable: true,
      validators: [Validators.pattern(/^[1-9]\d{0,9}$/), Validators.max(2147483647)]
    }),
    vigenteDesde: new FormControl(this.fechaLocalActual(), {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  readonly fechaConsulta = new FormControl(this.fechaLocalActual(), {
    nonNullable: true,
    validators: [Validators.required]
  });

  planes: Plan[] = [];
  planSeleccionado: Plan | null = null;
  versionAplicable: VersionPlan | null = null;
  cargando = false;
  cargandoDetalle = false;
  consultandoVersion = false;
  creandoPlan = false;
  publicandoVersion = false;
  desactivandoPlan = false;
  error = '';
  errorConsulta = '';
  exito = '';

  ngOnInit(): void {
    this.cargarPlanes();
  }

  cargarPlanes(): void {
    this.cargando = true;
    this.error = '';
    this.api.listarPlanes().subscribe({
      next: planes => {
        this.planes = planes;
        if (this.planSeleccionado) {
          this.planSeleccionado = planes.find(plan => plan.id === this.planSeleccionado?.id) ?? null;
        }
        this.cargando = false;
      },
      error: (error: unknown) => {
        this.error = this.mensajeError(error);
        this.cargando = false;
      }
    });
  }

  consultarDetalle(planId: number): void {
    this.cargandoDetalle = true;
    this.error = '';
    this.exito = '';
    this.planSeleccionado = null;
    this.versionAplicable = null;
    this.api.obtenerPlan(planId).subscribe({
      next: plan => {
        this.planSeleccionado = plan;
        this.cargandoDetalle = false;
      },
      error: (error: unknown) => {
        this.error = this.mensajeError(error);
        this.cargandoDetalle = false;
      }
    });
  }

  crearPlan(): void {
    this.error = '';
    this.exito = '';
    if (this.crearPlanForm.invalid) {
      this.crearPlanForm.markAllAsTouched();
      return;
    }

    const form = this.crearPlanForm.getRawValue();
    const request: CrearPlanRequest = {
      codigo: form.codigo.trim().toUpperCase(),
      nombre: form.nombre.trim(),
      precioMensual: Number(form.precioMensual),
      maximoMascotas: Number(form.maximoMascotas),
      limiteAtenciones: form.limiteAtenciones === '' ? null : Number(form.limiteAtenciones),
      vigenteDesde: form.vigenteDesde
    };

    this.creandoPlan = true;
    this.api.crearPlan(request).subscribe({
      next: plan => {
        this.agregarOActualizarPlan(plan);
        this.planSeleccionado = plan;
        this.crearPlanForm.reset({
          codigo: '',
          nombre: '',
          precioMensual: '',
          maximoMascotas: '',
          limiteAtenciones: '',
          vigenteDesde: this.fechaLocalActual()
        });
        this.exito = `El plan ${plan.nombre} se creó correctamente.`;
        this.creandoPlan = false;
      },
      error: (error: unknown) => {
        this.error = this.mensajeError(error);
        this.creandoPlan = false;
      }
    });
  }

  publicarVersion(): void {
    if (!this.planSeleccionado || !this.planSeleccionado.activo) {
      return;
    }

    this.error = '';
    this.exito = '';
    if (this.publicarVersionForm.invalid) {
      this.publicarVersionForm.markAllAsTouched();
      return;
    }

    const form = this.publicarVersionForm.getRawValue();
    const request: CrearVersionPlanRequest = {
      precioMensual: Number(form.precioMensual),
      maximoMascotas: Number(form.maximoMascotas),
      limiteAtenciones: form.limiteAtenciones === '' ? null : Number(form.limiteAtenciones),
      vigenteDesde: form.vigenteDesde
    };

    this.publicandoVersion = true;
    this.api.publicarVersion(this.planSeleccionado.id, request).subscribe({
      next: plan => {
        this.agregarOActualizarPlan(plan);
        this.planSeleccionado = plan;
        this.versionAplicable = null;
        this.publicarVersionForm.reset({
          precioMensual: '',
          maximoMascotas: '',
          limiteAtenciones: '',
          vigenteDesde: this.fechaLocalActual()
        });
        this.exito = `La nueva versión de ${plan.nombre} se publicó correctamente.`;
        this.publicandoVersion = false;
      },
      error: (error: unknown) => {
        this.error = this.mensajeError(error);
        this.publicandoVersion = false;
      }
    });
  }

  consultarVersionAplicable(): void {
    if (!this.planSeleccionado || this.fechaConsulta.invalid) {
      this.fechaConsulta.markAsTouched();
      return;
    }

    this.errorConsulta = '';
    this.versionAplicable = null;
    this.consultandoVersion = true;
    this.api.obtenerVersionAplicable(this.planSeleccionado.id, this.fechaConsulta.value).subscribe({
      next: version => {
        this.versionAplicable = version;
        this.consultandoVersion = false;
      },
      error: (error: unknown) => {
        this.errorConsulta = this.mensajeError(error);
        this.consultandoVersion = false;
      }
    });
  }

  desactivarPlan(): void {
    if (!this.planSeleccionado?.activo || this.desactivandoPlan) {
      return;
    }

    const nombrePlan = this.planSeleccionado.nombre;
    if (!window.confirm(`¿Desactivar el plan ${nombrePlan}? Se conservará su historial y no podrá publicar nuevas versiones.`)) {
      return;
    }

    this.error = '';
    this.exito = '';
    this.desactivandoPlan = true;
    this.api.desactivarPlan(this.planSeleccionado.id).subscribe({
      next: plan => {
        this.agregarOActualizarPlan(plan);
        this.planSeleccionado = plan;
        this.exito = `El plan ${plan.nombre} se desactivó. Su historial comercial se conservó.`;
        this.desactivandoPlan = false;
      },
      error: (error: unknown) => {
        this.error = this.mensajeError(error);
        this.desactivandoPlan = false;
      }
    });
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0
    }).format(precio);
  }

  private agregarOActualizarPlan(plan: Plan): void {
    const index = this.planes.findIndex(item => item.id === plan.id);
    if (index === -1) {
      this.planes = [...this.planes, plan].sort((first, second) => first.codigo.localeCompare(second.codigo));
      return;
    }
    this.planes = this.planes.map(item => item.id === plan.id ? plan : item);
  }

  private mensajeError(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      if (error.status === 401) {
        return 'La sesión expiró. Inicia sesión nuevamente para continuar.';
      }
      if (error.status === 403) {
        return 'Acceso denegado. Verifica que la sesión tenga el rol ADMIN y que el token CSRF sea válido.';
      }
      if (error.status === 0) {
        return 'No se pudo conectar con el servidor. Inténtalo nuevamente.';
      }

      const response = error.error as ApiErrorResponse | null;
      const message = typeof response?.message === 'string' ? response.message : '';
      const details = Array.isArray(response?.errors)
        ? response.errors.filter((item): item is string => typeof item === 'string')
        : [];
      if (message && details.length) {
        return `${message} ${details.join(' ')}`;
      }
      if (message) {
        return message;
      }
      if (details.length) {
        return details.join(' ');
      }
    }
    return 'Ocurrió un error inesperado. Inténtalo nuevamente.';
  }

  private fechaLocalActual(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}
