import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateListAdmin } from './certificate-list-admin';

describe('CertificateListAdmin', () => {
  let component: CertificateListAdmin;
  let fixture: ComponentFixture<CertificateListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
