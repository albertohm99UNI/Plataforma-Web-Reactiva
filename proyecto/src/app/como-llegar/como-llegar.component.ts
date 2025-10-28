import { AfterViewInit, Component, OnInit, ViewEncapsulation } from '@angular/core';
import { OverpassService } from '../services/overpass.service';
import * as L from 'leaflet';
import * as js2xmlparser from 'js2xmlparser'; 

@Component({
  selector: 'app-como-llegar',
  templateUrl: './como-llegar.component.html',
  styleUrls: ['./como-llegar.component.scss']
})
export class ComoLlegarComponent implements OnInit, AfterViewInit {
  private map: any;
  private nodeCache: { [key: number]: any } = {};

  constructor(private overpassService: OverpassService) {}

  ngAfterViewInit(): void {
    this.initMap();
    this.loadRoutes();
  }

  ngOnInit(): void {}

 
  initMap(): void {
    this.map = L.map('map', { zoomControl: false }).setView([39.7699306, -1.030388], 10);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
  }

  loadRoutes(): void {
    this.overpassService.getRoutes(39.7699306, -1.030388).subscribe((response) => {
      this.filterNodes(response.elements);

    
      let allRouteCoordinates: L.LatLng[] = [];

      response.elements.forEach((element: any) => {
        if (element.type === 'way' && element.nodes && element.nodes.length > 0) {
          const routeCoordinates = this.getRouteCoordinates(element);

    
          const polyline = L.polyline(routeCoordinates, { color: 'blue', weight: 3 }).addTo(this.map);
          
          polyline.on('click', () => {
            this.showRouteDetails(element);
             
          });

          allRouteCoordinates = allRouteCoordinates.concat(routeCoordinates);
        }
      });

     
      if (allRouteCoordinates.length > 0) {
        this.map.fitBounds(L.latLngBounds(allRouteCoordinates));
      }
    });
  }


  filterNodes(elements: any[]): void {
    elements.forEach((element: any) => {
      if (element.type === 'node' && element.lat && element.lon) {
        this.nodeCache[element.id] = { lat: element.lat, lon: element.lon };
      }
    });
  }

  
  getRouteCoordinates(route: any): L.LatLng[] {
    const coordinates: L.LatLng[] = [];
    route.nodes.forEach((nodeId: number) => {
      const node = this.nodeCache[nodeId];
      if (node) {
        coordinates.push(L.latLng(node.lat, node.lon));
      }
    });
    return coordinates;
  }


  showRouteDetails(route: any): void {
    const popupContent = `
      <strong>Detalles:</strong><br>
      ${route.tags.name}<br>
      <button id="btnVer" class="btn-ver">Ver</button>
      <button id="btnDescargar" class="btn-ver">Descargar</button>
    `;
  
    const popup = L.popup()
      .setLatLng(this.map.getCenter()) 
      .setContent(popupContent)
      .openOn(this.map);
  
  
    setTimeout(() => {
      const btnVer = document.getElementById('btnVer');
      const btnDescargar = document.getElementById('btnDescargar');
  
      btnVer?.addEventListener('click', () => {
        window.open(`https://www.openstreetmap.org/way/${route.id}`, '_blank');
      });
  
      btnDescargar?.addEventListener('click', () => {
        this.downloadGPX(route); 
      });
    }, 0);
  }

  downloadGPX(route: any): void {
    const gpxData = this.convertToGPX(route);
    const blob = new Blob([gpxData], { type: 'application/gpx+xml' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${route.id}.gpx`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }
  

  convertToGPX(route: any): string {
  
    return `<?xml version="1.0"?>
    <gpx version="1.1" creator="Leaflet">
      <trk><name>${route.tags.name}</name>
      <trkseg>
        ${route.nodes.map((nodeId: number) => {
          const node = this.nodeCache[nodeId];
          return node ? `<trkpt lat="${node.lat}" lon="${node.lon}"></trkpt>` : '';
        }).join('')}
      </trkseg>
      </trk>
    </gpx>`;
  }
}
