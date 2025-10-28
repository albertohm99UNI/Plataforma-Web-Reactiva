import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { baseURL } from '../compartido/baseurl';
import { WeatherData } from '../compartido/WeatherData';
import { AuthService } from './auth.service';



export interface WeatherWithIcon extends WeatherData {
  icon: string; 

}

@Injectable({
  providedIn: 'root',
})
export class WeatherService {


  constructor(private http: HttpClient, private authService: AuthService) {}

  
  getWeather(startDate: Date, endDate: Date): Observable<WeatherWithIcon[]> {
    const formatDate = (date: Date) => {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
      const day = date.getDate().toString().padStart(2, '0');  
      return `${year}-${month}-${day}`;
    };
  
    const startDateStr = formatDate(startDate);
    const endDateStr = formatDate(endDate);
    
    const url = `${baseURL}weather?startDate=${startDateStr}&endDate=${endDateStr}`;
   

    const headers = this.getAuthHeaders();
      
        if (headers) {
        
          return this.http.get<WeatherData[]>(url,{headers}).pipe(
            map((data) => data.map((item) => this.addIconAndTemperature(item)))
          );
        } else {
            catchError((error) => {
           
            
              this.authService.sessionExpired(error);
    
             
              return throwError(() => error);
            })
          return throwError(() => new Error('No autorizado'));
          }
  }
  


 
  private addIconAndTemperature(item: WeatherData): WeatherWithIcon {
    let icon: string;
    let minTemp: number;
    let maxTemp: number;
  
    switch (item.classification) {
      case 'Lluvioso':
        icon = 'fa-cloud-showers-heavy'; 
        minTemp = item.minTemp;
        maxTemp = item.maxTemp;
        break;
      case 'Caluroso':
        icon = 'fa-sun'; 
        minTemp = item.minTemp;
        maxTemp = item.maxTemp;
        break;
      case 'Frío':
        icon = 'fa-snowflake'; 
        minTemp = item.minTemp;
        maxTemp = item.maxTemp;
        break;
      default:
        icon = 'fa-cloud';
        minTemp = item.minTemp;
        maxTemp = item.maxTemp;
        break;
    }
  
    return {
      ...item,
      icon: `fa ${icon}`,
      minTemp,
      maxTemp,
    };
  }
  

  private getAuthHeaders(): HttpHeaders | null {
      const accessToken = sessionStorage.getItem('AccessToken');
      if (accessToken) {
        return new HttpHeaders({
          Authorization: `Bearer ${accessToken}`,
        });
      }
      return null;
    }

    
}