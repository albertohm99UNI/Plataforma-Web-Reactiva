import { TestBed } from '@angular/core/testing';

import { EntornoService } from './entorno.service';

describe('EntornoService', () => {
  let service: EntornoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntornoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
