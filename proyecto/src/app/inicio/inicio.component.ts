import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReviewService } from '../services/review.service';
import { MailService } from '../services/mail.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth.service';
import { switchMap } from 'rxjs';
@Component({
  selector: 'app-inicio',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.scss']
})
export class InicioComponent implements OnInit {
  contactForm: FormGroup;
  reviewForm: FormGroup;
  reviews: any[] = [];
  expandedIndex: number | null = null;
  isClientRole: boolean = false;
  visibleReviewsCount = 3;  
  constructor(private fb: FormBuilder, private reviewService: ReviewService, private mailService: MailService, private authService: AuthService,
    private snackBar: MatSnackBar
  ) {
    // Formulario de contacto
    this.contactForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      message: ['', [Validators.required]],
    });

    // Formulario de reseñas
    this.reviewForm = this.fb.group({
      review: ['', [Validators.required, Validators.minLength(10)]],
      rate: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      rateUbicacion: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      rateServicios: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
      rateLimpieza: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
 
    });
  }

  ngOnInit() {
    this.getReviews();
    this.checkUserRole();
  }

  checkUserRole() {
    const userRolesString = sessionStorage.getItem('Roles')?.replace(/[\[\]]/g, '') || '';
    const userRoles = userRolesString.split(',').map(role => role.trim());
    this.isClientRole = userRoles.includes('ROLE_CLIENTE');
  }

  getReviews() {
    this.reviewService.fetchReviews().subscribe({
      next: (reviews: any[]) => {
        this.reviews = reviews.map(review => {
          if (review.creationDate && Array.isArray(review.creationDate)) {
            const [year, month, day ] = review.creationDate;
            const creationDate = new Date(year, month - 1, day, 0, 0, 0);
            if (!isNaN(creationDate.getTime())) {
              const opciones: Intl.DateTimeFormatOptions = {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
              };
              review.creationDate = creationDate.toLocaleDateString('es-ES', opciones);
            } else {
              console.warn('Fecha no válida:', review.creationDate);
              review.creationDate = 'Fecha no válida'; 
            }
          } else {
            review.creationDate = 'Fecha no disponible'; 
          }
          return review;
        });
      },
      error: (error: any) => console.error('Error al cargar reseñas:', error),
    });
    
  }

  toggleExpanded(index: number): void {
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }

  getStars(rate: number): ('filled' | 'empty')[] {
    return Array.from({ length: 5 }, (_, i) => (i < rate ? 'filled' : 'empty'));
  }
  loadMoreReviews(): void {
  
    this.visibleReviewsCount = Math.min(this.visibleReviewsCount + 3, this.reviews.length);
  }
  hideReviews(): void {
 
    this.visibleReviewsCount = 3;
  }
  onSubmitReview() {
  
    if (this.reviewForm.valid) {
      this.reviewService.addReview(this.reviewForm.value).pipe(
        switchMap((response) => {
          console.log('Reseña enviada correctamente:', response);
          this.snackBar.open('Reseña enviada correctamente', 'Cerrar');
          this.reviewForm.reset();
          return this.authService.checkRoles(); 
        })
      ).subscribe({
        next: () => {
          console.log('Sesión actualizada correctamente');
          this.checkUserRole();
          this.getReviews(); 
        },
        error: (error) => console.error('Error al procesar la reseña o actualizar la sesión:', error)
      });
    } else {
      console.warn('Formulario de reseñas no válido');
    }
  }    

  onSubmit(form: FormGroup) {
    if (form.valid) {
      this.mailService.addContact(this.contactForm.value).subscribe({
        next: () => {
          console.log('Contacto enviado correctamente');
          this.contactForm.reset();
       
        },
        error: (error) => console.error('Error al enviar el formulario:', error),
      });
    } else {
      console.warn('Formulario de contacto no válido');
    }
  }
}
