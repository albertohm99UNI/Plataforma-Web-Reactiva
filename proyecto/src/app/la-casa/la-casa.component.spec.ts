import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LaCasaComponent } from './la-casa.component';

describe('LaCasaComponent', () => {
  let component: LaCasaComponent;
  let fixture: ComponentFixture<LaCasaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LaCasaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LaCasaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
