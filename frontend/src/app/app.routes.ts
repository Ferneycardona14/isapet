import { Routes } from '@angular/router';
import { RegistroAfiliadoComponent } from './features/afiliados/pages/registro-afiliado/registro-afiliado.component';
import { ListaAfiliadosComponent } from './features/afiliados/pages/lista-afiliados/lista-afiliados.component';
import { DetalleAfiliadoComponent } from './features/afiliados/pages/detalle-afiliado/detalle-afiliado.component';
import { EditarAfiliadoComponent } from './features/afiliados/pages/editar-afiliado/editar-afiliado.component';
import { adminGuard } from './core/auth/admin.guard';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { ListaPlanesComponent } from './features/planes/pages/lista-planes/lista-planes.component';

export const routes: Routes = [
	{ path: 'login', component: LoginComponent },
	{ path: 'planes', component: ListaPlanesComponent, canActivate: [adminGuard] },
	{ path: 'afiliados', pathMatch: 'full', component: ListaAfiliadosComponent, canActivate: [adminGuard] },
	{ path: 'afiliados/registro', component: RegistroAfiliadoComponent, canActivate: [adminGuard] },
	{ path: 'afiliados/:id/editar', component: EditarAfiliadoComponent, canActivate: [adminGuard] },
	{ path: 'afiliados/:id', component: DetalleAfiliadoComponent, canActivate: [adminGuard] },
	{ path: '', pathMatch: 'full', redirectTo: 'afiliados/registro' },
	{ path: '**', redirectTo: 'afiliados/registro' }
];
