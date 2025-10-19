import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateFormPageAdmin } from './certificate-form-page-admin';

describe('CertificateFormPageAdmin', () => {
  let component: CertificateFormPageAdmin;
  let fixture: ComponentFixture<CertificateFormPageAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateFormPageAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateFormPageAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
