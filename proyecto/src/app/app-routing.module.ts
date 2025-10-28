import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { rutas } from './rutas';
const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(rutas)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
