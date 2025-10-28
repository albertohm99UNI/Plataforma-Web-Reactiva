import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  catchError, map, Observable, of } from 'rxjs';
import { baseURL } from "../compartido/baseurl";
import { AuthService } from './auth.service';
import {MyUser} from '../compartido/MyUser';
import { Reserva } from '../compartido/Reserva';
@Injectable({
  providedIn: 'root'
})
export class ReservaService {
 
  baseURL: any;
 

  constructor(private http: HttpClient,private authService: AuthService) {  }

  obtenerDatosUsuario(): Observable<MyUser|null> {
    
    return this.http.get<MyUser>(baseURL + 'me?username=' + sessionStorage.getItem("Email"), this.authService.getAuthHeaders()).pipe(
      catchError(err => {
        console.error('Error al obtener datos del usuario', err);
   
        return of(null);
      })
    );


  }
  confirmarReserva(reservaData: Reserva): Observable<any> {
    const startDate = new Date(reservaData.startDate);
    const endDate = new Date(reservaData.endDate);
  
    
    const startDateUTC = new Date(Date.UTC(startDate.getFullYear(), startDate.getMonth(), startDate.getDate()));
    const endDateUTC = new Date(Date.UTC(endDate.getFullYear(), endDate.getMonth(), endDate.getDate()));
  
    const reservaDataConFechaUTC = {
      ...reservaData,
      startDate: startDateUTC.toISOString(),
      endDate: endDateUTC.toISOString()
    };
  
   
    return this.http.post<any>(baseURL + 'reserva', reservaDataConFechaUTC, this.authService.getAuthHeaders());
  }

  pagarReserva(reservaData: Reserva, motivo: string): Observable<any> {
    const body =  motivo ;  
    return this.http.put<any>(baseURL +`${reservaData.id}/estado?estado=PAGADA`, body,this.authService.getAuthHeaders());
  }
  
  cancelarReserva(reservaData: Reserva, motivo: string): Observable<any> {
    const body =  motivo ;  
    return this.http.put<any>(baseURL +`${reservaData.id}/estado?estado=CANCELADA`,body,this.authService.getAuthHeaders() );
  }
  getReservasUser(email: string): Observable<any[]> {
    return this.http.get<any[]>(baseURL+'reservasUser?email='+`${email}`,this.authService.getAuthHeaders());
  }
  obtenerReservas(): Observable<any[]> {
    return this.http.get<any[]>(baseURL+'reservas',this.authService.getAuthHeaders());
  }
  getReservedDates(): Observable<{ start: Date; end: Date }[]> {
    return this.http.get<{ t1: number[]; t2: number[] }[]>(baseURL +`reservasProximas`,this.authService.getAuthHeaders()).pipe(
      map(response =>
        response.map(item => ({
          start: new Date(item.t1[0], item.t1[1] - 1, item.t1[2]), 
          end: new Date(item.t2[0], item.t2[1] - 1, item.t2[2])
        }))
      )
    );
  }
}

