import { Injectable } from '@angular/core';

import { Media } from '../compartido/media';
import {  catchError, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { HttpErrorHandler } from './HttpErrorHandler';
import { baseURL } from '../compartido/baseurl';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor(private http: HttpClient, private errorHandler: HttpErrorHandler ) {}

  getMediaByHashtag(hashtag: string): Observable<Media[]> {
    return this.http.get<Media[]>(`${baseURL}media?hashtag=${hashtag}`, {
      headers: { 'Content-Type': 'application/json' },
    }).pipe(
      catchError(err => {
        console.error('Error al obtener media por hashtag', err);
        return of([]);
      })
    );
  }
  getMedia(): Observable<Media[]> {
    return this.http.get<Media[]>(`${baseURL}media`, {
      headers: { 'Content-Type': 'application/json' },
    }).pipe(
      catchError(err => {
        console.error('Error al obtener media por hashtag', err);
        return of([]);
      })
    );
  }
}
