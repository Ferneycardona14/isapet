import { Component, DestroyRef, inject, OnInit } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged } from 'rxjs';
import { AfiliadoListaItem, Pagina } from '../../models/afiliado.models';
import { AfiliadoApiService } from '../../services/afiliado-api.service';

@Component({
  selector: 'app-lista-afiliados',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './lista-afiliados.component.html',
  styleUrl: './lista-afiliados.component.scss'
})
export class ListaAfiliadosComponent implements OnInit {
  private readonly api = inject(AfiliadoApiService);
  private readonly destroyRef = inject(DestroyRef);

  readonly busqueda = new FormControl('', { nonNullable: true });
  pagina: Pagina<AfiliadoListaItem> | null = null;
  cargando = false;
  error = '';
  readonly size = 10;

  ngOnInit(): void {
    this.busqueda.valueChanges.pipe(
      debounceTime(250),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => this.cargar(0));
    this.cargar(0);
  }

  cargar(page: number): void {
    this.cargando = true;
    this.error = '';
    this.api.listarAfiliados(this.busqueda.value, page, this.size).subscribe({
      next: pagina => {
        this.pagina = pagina;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo cargar la lista de afiliados. Inténtalo nuevamente.';
        this.cargando = false;
      }
    });
  }

  limpiarBusqueda(): void {
    this.busqueda.setValue('');
    this.cargar(0);
  }
}
