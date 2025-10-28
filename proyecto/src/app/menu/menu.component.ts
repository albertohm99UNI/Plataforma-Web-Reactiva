import { Component, HostListener, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import {
  faHome,
  faHouseUser,
  faCalendarCheck,
  faDollarSign,
  faMapMarkerAlt,
  faPhone,
  faStar,
  faTree,
} from '@fortawesome/free-solid-svg-icons';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-menu',

  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  faHome = faHome;
  faHouseUser = faHouseUser;
  faCalendarCheck = faCalendarCheck;
  faDollarSign = faDollarSign;
  faMapMarkerAlt = faMapMarkerAlt;
  faPhone = faPhone;
  faStar = faStar;
  faTree = faTree;
 
  @ViewChild('sidenav') sidenav!: MatSidenav;
  isMobile!: boolean;

  user$: Observable<boolean>;
  userName$: Observable<string | null>;
  isAdmin$: Observable<boolean>;
  constructor(private authService: AuthService) {
    this.updateLayout();
    this.user$ = this.authService.user$; 
    this.userName$ = this.authService.userName$;
    this.isAdmin$ = this.authService.isAdmin$();
    
  }
  ngOnInit(): void {
    this.isAdmin$ = this.authService.isAdmin$();
    console.log(this.isAdmin$);
  }

  logout() {
   
    this.authService.logout();
   
  }
 

  @HostListener('window:resize', ['$event'])
  onResize(event: Event): void {
    this.updateLayout();
  }

  updateLayout(): void {
    this.isMobile = window.innerWidth <= 900;
  }

  toggleSidenav(): void {
    this.sidenav.toggle();
  }
}
