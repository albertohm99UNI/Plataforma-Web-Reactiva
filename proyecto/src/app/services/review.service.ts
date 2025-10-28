import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { baseURL } from '../compartido/baseurl';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { MatSnackBar } from '@angular/material/snack-bar'; 
@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private reviewsSubject = new BehaviorSubject<any[]>([]);
  reviews$ = this.reviewsSubject.asObservable();

  constructor(private http: HttpClient, private router: Router, private authService: AuthService, private snackBar : MatSnackBar) {}

  
  fetchReviews(): Observable<any[]> {
    return this.http.get<any[]>(`${baseURL}reviews`).pipe(
      tap((reviews) => this.reviewsSubject.next(reviews))
    );
  }

  addReview(review: any): Observable<any> {
    const headers = this.authService.getAuthHeaders();
  
    if (headers) {
    
      return this.http.post<any>(baseURL + 'addReview', review,  headers ).pipe(
        tap(() => {
          this.fetchReviews().subscribe(); 
          this.snackBar.open('!Tomamos nota!', 'Cerrar', {
            duration: 3000, 
            horizontalPosition: 'center', 
            verticalPosition: 'top', 
           
          });
        }),
        catchError((error) => {
       
        
          this.authService.sessionExpired(error);

         
          return throwError(() => error);
        })
      );
    } else {

      return throwError(() => new Error('No autorizado'));
    }
  }
  
}
