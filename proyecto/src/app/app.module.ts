import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { RouterModule } from '@angular/router'; 


import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { OverpassService } from './services/overpass.service';

import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { InicioComponent } from './inicio/inicio.component';
import { LaCasaComponent } from './la-casa/la-casa.component';
import { ReservasComponent } from './reservas/reservas.component';

import { ComoLlegarComponent } from './como-llegar/como-llegar.component';
import { RecomendacionesComponent } from './recomendaciones/recomendaciones.component';
import { EntornoComponent } from './entorno/entorno.component';
import { MatDialogActions } from '@angular/material/dialog';
import { GoogleMapsModule } from '@angular/google-maps';


import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CarouselModule } from 'ngx-owl-carousel-o';

import { MatExpansionModule } from '@angular/material/expansion';
import { ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LayoutModule } from '@angular/cdk/layout';
import { FormsModule } from '@angular/forms';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { baseURL } from './compartido/baseurl';
import { HttpClientModule } from '@angular/common/http';
import {MatSpinner} from '@angular/material/progress-spinner';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { StarRatingComponent } from './star-rating/star-rating.component';
import localeEs from '@angular/common/locales/es';
import { registerLocaleData } from '@angular/common';
import { AdministradorComponent } from './administrador/administrador.component';
import { DialogMotivoComponent } from './dialog-motivo/dialog-motivo.component';
import { MatDialogModule } from '@angular/material/dialog';
import { ResumenReservaComponent } from './resumen-reserva/resumen-reserva.component';
import { FooterComponent } from './footer/footer.component';

registerLocaleData(localeEs);
@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    InicioComponent,
    ResumenReservaComponent,
    LaCasaComponent,
    ReservasComponent,
    LoginComponent,
    RegisterComponent,
    AdministradorComponent,
    ComoLlegarComponent,
    RecomendacionesComponent,
    StarRatingComponent,
    EntornoComponent,
    DialogMotivoComponent,
    FooterComponent,
   
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
   MatTableModule,
    MatExpansionModule,
    CalendarModule, 
    BrowserAnimationsModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    GoogleMapsModule,
    AppRoutingModule,
    MatSpinner,
    CarouselModule,
    MatDialogActions,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatIconModule,
    MatListModule,
    MatCardModule,
    MatDividerModule,
    FontAwesomeModule,
    MatDatepickerModule,
    MatNativeDateModule,
    NoopAnimationsModule,
    LayoutModule,
    MatSelectModule,
    MatRadioModule,
    MatGridListModule,
  ],
  providers: [{ provide: 'LOCALE_ID', useValue: 'es' },{ provide: 'baseURL', useValue: baseURL },OverpassService],
  bootstrap: [AppComponent],
})
export class AppModule {}


