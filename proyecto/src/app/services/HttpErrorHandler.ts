import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HttpErrorHandler {
  constructor() {}

  public handleError(error: HttpErrorResponse): any {
    let errorMessage = 'Ocurrió un error desconocido';

    if (error.error instanceof ErrorEvent) {
      
      errorMessage = `Error del cliente o red: ${error.error.message}`;
    } else {
      
      errorMessage = `Error del servidor (Código: ${error.status}): ${error.message}`;
    }

    console.error('Error detectado:', errorMessage); 
    
  }
}
