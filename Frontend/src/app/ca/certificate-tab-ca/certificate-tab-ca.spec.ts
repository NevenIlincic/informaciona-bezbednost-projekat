import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateTabCa } from './certificate-tab-ca';

describe('CertificateTabCa', () => {
  let component: CertificateTabCa;
  let fixture: ComponentFixture<CertificateTabCa>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateTabCa]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateTabCa);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
