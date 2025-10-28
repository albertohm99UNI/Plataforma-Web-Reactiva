import { Component, ViewEncapsulation } from '@angular/core';
import { Media } from '../compartido/media';
import { Inject } from '@angular/core';
import { MediaService } from '../services/media.service';
@Component({
  selector: 'app-la-casa',

  templateUrl: './la-casa.component.html',
  styleUrl: './la-casa.component.scss'
})
export class LaCasaComponent {
  
  slides : Media[] = [];
  videoUrl: string | null = null;
 
  currentSlide: number =0;
  imagesPerSlide: number =2;
 
  constructor( @Inject('baseURL') public BaseURL: string, private mediaService: MediaService) {}

  ngOnInit() {
    this.videoUrl = this.BaseURL + 'video';
    //this.fetchMediaByHashtag('habitacionprincipal');
    //this.fetchMediaByHashtag('jardin');
    this.fetchMedia();
  }
  fetchMedia() {
    this.mediaService.getMedia().subscribe({
      next: (data: Media[]) => {
        const filtrados = data.filter(media => media.hashtag !== 'gastronomia');
        this.slides.push(...filtrados);
      },
      error: (err: any) => {
        console.error('Error al obtener media:', err);
      }
    });
  }
  fetchMediaByHashtag(hashtag: string): void {
    this.mediaService.getMediaByHashtag(hashtag).subscribe({
      next: (data: Media[]) => {
      
        this.slides.push(...data);
      },
      error: (err: any) => {
        console.error('Error al obtener media:', err);
      }
    });
  }



  prevSlide() {
    this.currentSlide =
    this.currentSlide > 0
      ? this.currentSlide - this.imagesPerSlide
      : Math.max(this.slides.length - this.imagesPerSlide, 0);
  }

  nextSlide() {
    this.currentSlide =
    this.currentSlide <
    this.slides.length - this.imagesPerSlide
      ? this.currentSlide + this.imagesPerSlide
      : 0;
  }

  getTransformStyle(): string {
  const percent = this.currentSlide * (100 / this.imagesPerSlide);
  return `translateX(-${percent}%)`;
}

getFlexStyle(): string {
  return `0 0 ${100 / this.imagesPerSlide}%`;
}
}
