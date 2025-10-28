import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { EntornoService } from '../services/entorno.service';
import { Entorno } from '../compartido/entorno';

@Component({
  selector: 'app-entorno',
  templateUrl: './entorno.component.html',
  styleUrls: ['./entorno.component.scss']
})
export class EntornoComponent implements OnInit {
  tuejarPlaces: Entorno[] = [];
  chelvaPlaces: Entorno[] = [];

  constructor(private entornoService: EntornoService) {}

  ngOnInit(): void {
    this.entornoService.getEntorno().subscribe((places: Entorno[]) => {
    
      this.tuejarPlaces = places.filter(place => place.localizacion === 'tuejar' && place.categoria !== 'fiestas');
      this.chelvaPlaces = places.filter(place => place.localizacion === 'chelva' && place.categoria !== 'fiestas');
    });
  }
}
