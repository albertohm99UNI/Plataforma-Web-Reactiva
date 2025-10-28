import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OverpassService {
  private apiUrl = 'https://overpass-api.de/api/interpreter?data=';

  constructor(private http: HttpClient) {}

  
  getRoutes(latitude: number, longitude: number, radius: number = 3000): Observable<any> {
    const query = `
      [out:json];
      (
        way["highway"~"footway|path|cycleway|track"]["name"](around:${radius},${latitude},${longitude});

      );
      out body;
      >;
      out skel qt;
    `;

    const url = this.apiUrl + encodeURIComponent(query);
    return this.http.get<any>(url);
  }
}
