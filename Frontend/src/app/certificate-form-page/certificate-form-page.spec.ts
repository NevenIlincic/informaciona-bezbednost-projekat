import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateFormPage } from './certificate-form-page';

describe('CertificateFormPage', () => {
  let component: CertificateFormPage;
  let fixture: ComponentFixture<CertificateFormPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateFormPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateFormPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
