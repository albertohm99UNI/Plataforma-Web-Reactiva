import { TestBed } from '@angular/core/testing';

import { ProcesaHttpmsjService } from './procesa-httpmsj.service';

describe('ProcesaHttpmsjService', () => {
  let service: ProcesaHttpmsjService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProcesaHttpmsjService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
