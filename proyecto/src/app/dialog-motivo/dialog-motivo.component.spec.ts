import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogMotivoComponent } from './dialog-motivo.component';

describe('DialogMotivoComponent', () => {
  let component: DialogMotivoComponent;
  let fixture: ComponentFixture<DialogMotivoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogMotivoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogMotivoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
