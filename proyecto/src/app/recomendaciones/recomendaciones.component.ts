import { Component, Inject } from '@angular/core';
import { Entorno } from '../compartido/entorno';
import { EntornoService } from '../services/entorno.service';
import { Media } from '../compartido/media';
import { MediaService } from '../services/media.service';
@Component({
  selector: 'app-recomendaciones',

  templateUrl: './recomendaciones.component.html',
  styleUrl: './recomendaciones.component.scss',
})
export class RecomendacionesComponent {
  videoUrl: string | null = null;
 
  slidesComida : Media[] = [];

  
  slidesFiestas : Entorno[] = [];
 
  currentSlideComida = 0;
  currentSlideFiesta = 0;
  imagesPerSlideComida = 3;
  imagesPerSlideFiesta = 3;

  constructor( @Inject('baseURL') public BaseURL: string,private entornoService: EntornoService, private mediaService:MediaService) {}
  ngOnInit(): void {
    this.videoUrl = this.BaseURL + 'gastronomia';
    
    this.entornoService.getFiestas().subscribe((places: Entorno[]) => {
    
      console.log('Fiestas obtenidas:', places);  
      this.slidesFiestas = places;
      
    });
    this.fetchMediaByHashtag('gastronomia');
    for (let i = 0; i < this.slidesComida.length; i++) {
      console.log(this.slidesComida[i].mediaurl);
    }
    

      this.slidesComida.push (
        {  id: '1',
          mediaurl: this.videoUrl||'',
          timestamp: new Date().toISOString(),
          caption: 'Un gazpacho tradicional de Tuejar',
          mediatype: 'video',
          hashtag: 'gastronomia',}
        );
    
  
  }
  prevSlideComida() {
    this.currentSlideComida =
      this.currentSlideComida > 0
        ? this.currentSlideComida - this.imagesPerSlideComida
        : Math.max(this.slidesComida.length - this.imagesPerSlideComida, 0);
  }

  nextSlideComida() {
    this.currentSlideComida =
      this.currentSlideComida <
      this.slidesComida.length - this.imagesPerSlideComida
        ? this.currentSlideComida + this.imagesPerSlideComida
        : 0;
  }

  prevSlideFiesta() {
    this.currentSlideFiesta =
      this.currentSlideFiesta > 0
        ? this.currentSlideFiesta - this.imagesPerSlideFiesta
        : Math.max(this.slidesFiestas.length - this.imagesPerSlideFiesta, 0);
  }

  nextSlideFiesta() {
    this.currentSlideFiesta =
      this.currentSlideFiesta <
      this.slidesFiestas.length - this.imagesPerSlideFiesta
        ? this.currentSlideFiesta + this.imagesPerSlideFiesta
        : 0;
  }

  fetchMediaByHashtag(hashtag: string): void {
    this.mediaService.getMediaByHashtag(hashtag).subscribe({
      next: (data: Media[]) => {
      
        this.slidesComida.push(...data);
      },
      error: (err: any) => {
        console.error('Error al obtener media:', err);
      }
    });
  }

  
}
