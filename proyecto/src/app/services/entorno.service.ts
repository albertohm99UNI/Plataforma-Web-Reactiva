import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { baseURL } from '../compartido/baseurl';
import { Entorno } from '../compartido/entorno';
import { HttpErrorHandler } from './HttpErrorHandler';
import { Media } from '../compartido/media';

@Injectable({
  providedIn: 'root'
})
export class EntornoService {

  constructor(private http: HttpClient, private errorHandler: HttpErrorHandler ) {}

 
  getEntorno(): Observable<Entorno[]> {
    return this.http.get<Entorno[]>(`${baseURL}entorno`, {
      headers: { 'Content-Type': 'application/json' },
    }).pipe(
      catchError(err => {
        console.error('Error al obtener media por hashtag', err);
        return of([]);
      })
    );
  }

  getFiestas(): Observable<Entorno[]> {
    return this.http.get<Entorno[]>(`${baseURL}fiestas`, {
      headers: { 'Content-Type': 'application/json' },
    }).pipe(
      catchError(err => {
        console.error('Error al obtener fiestas', err);
        return of([]);
      })
    );
  }
 
}
