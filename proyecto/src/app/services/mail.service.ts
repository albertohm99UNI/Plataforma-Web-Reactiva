import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { baseURL } from '../compartido/baseurl';
import { throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';


@Injectable({
  providedIn: 'root'
})
export class MailService {

  authService: any;

  constructor(private http: HttpClient, 
    private snackBar: MatSnackBar,
  ) { }


  addContact(contact: any): Observable<any> {
   
  
    return this.http.post<any>(baseURL + 'contact', contact, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json', 
        'Accept': 'application/json'        
      })
    }).pipe(
      tap(response  => {
    
        this.snackBar.open('¡Genial. Te daremos respuesta lo antes posible!', 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'top',
        });
        return response;
      }),
      catchError((error) => {
        return throwError(() => error);
      })
    );
    
  }

}
