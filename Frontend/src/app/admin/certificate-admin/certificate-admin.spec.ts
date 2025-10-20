import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateAdmin } from './certificate-admin';

describe('CertificateAdmin', () => {
  let component: CertificateAdmin;
  let fixture: ComponentFixture<CertificateAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
