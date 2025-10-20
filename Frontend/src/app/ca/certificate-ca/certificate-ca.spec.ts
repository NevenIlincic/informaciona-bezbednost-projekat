import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateCa } from './certificate-ca';

describe('CertificateCa', () => {
  let component: CertificateCa;
  let fixture: ComponentFixture<CertificateCa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateCa]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateCa);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
