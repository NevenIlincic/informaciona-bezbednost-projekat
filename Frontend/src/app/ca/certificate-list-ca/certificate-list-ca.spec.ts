import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateListCa } from './certificate-list-ca';

describe('CertificateListCa', () => {
  let component: CertificateListCa;
  let fixture: ComponentFixture<CertificateListCa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateListCa]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateListCa);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
