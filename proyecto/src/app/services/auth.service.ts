import { HttpClient, HttpHeaders, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, tap, catchError, map, throwError } from "rxjs";
import { baseURL } from "../compartido/baseurl";
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private isAuthenticated = new BehaviorSubject<boolean>(false);
  private userNameSubject = new BehaviorSubject<string | null>(null);

  user$ = this.isAuthenticated.asObservable();
  userName$ = this.userNameSubject.asObservable();
  private accessToken: string | null = null;
  private refreshToken: string | null = null;
  private isAdmin = new BehaviorSubject<boolean>(false); 

  constructor(private http: HttpClient, private router: Router, private snackBar: MatSnackBar) {
   
    const storedToken = sessionStorage.getItem('AccessToken');
    const storedUsername = sessionStorage.getItem('Username');
    const storedRoles = sessionStorage.getItem('Roles');
    
   
    if (storedToken && storedUsername && storedRoles) {
      this.isAuthenticated.next(true);
      this.userNameSubject.next(storedUsername);
      this.checkAdminRole(storedRoles); 
     
    }
  }

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Basic ' + btoa(`${username}:${password}`),
      'Content-Type': 'application/json',
    });
  
    const body = { username, password };  
  
    return this.http.post<any>(`${baseURL}login`, body, { headers, observe: 'response' }).pipe(
      tap(response => {
        const accessToken = response.headers.get('access_token');
        const roles = response.headers.get('roles');
        const refreshToken = response.headers.get('refresh_token');
        console.log('Access Token:', accessToken);
        console.log('Roles:', roles);
        console.log('Refresh Token:', refreshToken);
        const username = response.headers.get('username');
        console.log('Username:', username);
        
        if (accessToken && roles && username && refreshToken) {
          sessionStorage.setItem('AccessToken', accessToken);
          sessionStorage.setItem('RefreshToken', refreshToken);
          sessionStorage.setItem('Roles', roles);
          
          this.http.get(baseURL + 'me?username=' + username, this.getAuthHeaders()).subscribe({
            next: (data: any) => {
              this.isAuthenticated.next(true);
              console.log('Datos del usuario:', data);
              sessionStorage.setItem('Username', data.nombre); 
              sessionStorage.setItem('Email', data.email);
              this.userNameSubject.next(data.nombre);
              this.checkAdminRole(roles); 
            
            },
            error: (error) => {
              console.error('Error al obtener los datos del usuario:', error);
              this.isAuthenticated.next(false);
              this.userNameSubject.next(null);
            },
          });
        }
      }),
      catchError(error => {
        throw error; 
      })
    );
  }
  checkRoles(): Observable<any> {
    return this.http.get<any>(baseURL + 'login/refresh', {
      ...this.getAuthHeaders(),
      observe: 'response' 
    }).pipe(
      map(response => {
        const accessToken = response.headers.get('access_token'); 
        const refreshToken = response.headers.get('refresh_token');
        const roles = response.headers.get('roles');
  
        console.log('Roles aquí:', roles);
  
        if (accessToken && refreshToken && roles) {
          sessionStorage.setItem('AccessToken', accessToken);
          sessionStorage.setItem('RefreshToken', refreshToken);
          sessionStorage.setItem('Roles', roles);
          this.checkAdminRole(roles);
        }
  
        return response.body;
      }),
      catchError(error => {
        console.error('Error en checkRoles:', error);
        return throwError(() => error);
      })
    );
  }
  
  
  


  sessionExpired(error: any): void {
    if (error.status === 403 && error.error?.error_msg?.includes('The Token has expired')) {
      this.snackBar.open('Su sesión ha expirado. Será redirigido al inicio de sesión.', 'Cerrar', {
        duration: 5000, 
        horizontalPosition: 'center', 
        verticalPosition: 'top', 
        panelClass: ['session-expired-snackbar'],
      });
      this.logout();
      setTimeout(() => {
        this.router.navigate(['/login']);
      }, 5000); 
    }
  }

  getAuthHeaders(): { headers: HttpHeaders } {
    const token = sessionStorage.getItem('AccessToken'); 
    return {
      headers: new HttpHeaders({
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      }),
    };
  }

  getAccessToken(): string | null {
    return this.accessToken;
  }

  setAccessToken(token: string) {
    this.accessToken = token;
  }

  setRefreshToken(token: string) {
    this.refreshToken = token;
  }

  logout(): void {
    sessionStorage.removeItem('AccessToken');
    sessionStorage.removeItem('Roles');
    sessionStorage.removeItem('Username');  
    sessionStorage.removeItem('Email');
    this.isAuthenticated.next(false);
    this.userNameSubject.next(null);
    this.isAdmin.next(false); 
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated.value;
  }

 
  private checkAdminRole(roles: string): void {
    const userRolesString = sessionStorage.getItem('Roles')?.replace(/[\[\]]/g, '') || '';
    const userRoles = userRolesString.split(',').map(role => role.trim());
    console.log('Roles:', userRoles);
    this.isAdmin.next(userRoles.includes('ROLE_ADMIN'));
    console.log(this.isAdmin.value);
  }

  

 
  
  isAdmin$(): Observable<boolean> {
    return this.isAdmin.asObservable();
  }
}