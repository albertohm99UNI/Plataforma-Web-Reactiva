import { Injectable } from '@angular/core';
import { baseURL } from '../compartido/baseurl';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class RegistroService {

 
  private apiUrl = baseURL+'signup';  

  constructor(private http: HttpClient) { }

 
  registerUser(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data, { observe: 'response' }).pipe(
      map(response => {
       
        if (response.status === 200) {
          return response.body;
        } else {
          throw new Error('Error inesperado: ' + response.status);
        }
      }),
      catchError(error => {
       
        if (error.status === 400 && error.error) {
          return throwError(() => new Error(error.error.error || 'Error en la solicitud'));
        }
        
        return throwError(() => new Error('Error desconocido'));
      })
    );
  }

  
}
