import { Routes } from '@angular/router';

import { InicioComponent } from './inicio/inicio.component';

import { LaCasaComponent } from './la-casa/la-casa.component';
import { ReservasComponent } from './reservas/reservas.component';
import { ComoLlegarComponent } from './como-llegar/como-llegar.component';

import { EntornoComponent } from './entorno/entorno.component';
import { RecomendacionesComponent } from './recomendaciones/recomendaciones.component';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AdministradorComponent } from './administrador/administrador.component';
export const rutas: Routes = [
  { path: 'inicio', component: InicioComponent },
  { path: 'la-casa', component: LaCasaComponent },
  { path: 'reservas', component: ReservasComponent },

  { path: 'como-llegar', component: ComoLlegarComponent },
  { path: 'administrador', component: AdministradorComponent },

  { path: 'recomendaciones', component: RecomendacionesComponent },
  { path: 'entorno', component: EntornoComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }, 
  { path: '', redirectTo: 'inicio', pathMatch: 'full' }, 
];
