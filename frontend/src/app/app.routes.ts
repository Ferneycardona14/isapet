import { Routes } from '@angular/router';
import { RegistroAfiliadoComponent } from './features/afiliados/pages/registro-afiliado/registro-afiliado.component';
import { ListaAfiliadosComponent } from './features/afiliados/pages/lista-afiliados/lista-afiliados.component';
import { DetalleAfiliadoComponent } from './features/afiliados/pages/detalle-afiliado/detalle-afiliado.component';
import { EditarAfiliadoComponent } from './features/afiliados/pages/editar-afiliado/editar-afiliado.component';

export const routes: Routes = [
	{ path: 'afiliados', pathMatch: 'full', component: ListaAfiliadosComponent },
	{ path: 'afiliados/registro', component: RegistroAfiliadoComponent },
	{ path: 'afiliados/:id/editar', component: EditarAfiliadoComponent },
	{ path: 'afiliados/:id', component: DetalleAfiliadoComponent },
	{ path: '', pathMatch: 'full', redirectTo: 'afiliados/registro' },
	{ path: '**', redirectTo: 'afiliados/registro' }
];
