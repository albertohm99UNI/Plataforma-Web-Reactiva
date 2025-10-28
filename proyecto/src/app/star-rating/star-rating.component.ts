import { Component, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  template: `<div class="star-rating">
      <mat-icon
        *ngFor="let star of stars; let index = index"
        [ngClass]="{'selected': index < rating}"
        (click)="setRating(index + 1)"
        >{{ index < rating ? 'star' : 'star_border' }}</mat-icon
      >
    </div>`,
    styleUrls: ['./star-rating.component.scss'],

})
export class StarRatingComponent {
  @Input() rating: number = 0; // Puntuación inicial
  @Input() maxStars: number = 5; // Número máximo de estrellas
  @Output() ratingChange = new EventEmitter<number>();

  get stars(): number[] {
    return Array(this.maxStars).fill(0);
  }

  setRating(value: number) {
    this.rating = value;
    this.ratingChange.emit(this.rating);
  }
}
