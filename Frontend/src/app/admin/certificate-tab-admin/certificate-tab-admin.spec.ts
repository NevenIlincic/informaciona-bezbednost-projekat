import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateTabAdmin } from './certificate-tab-admin';

describe('CertificateTabAdmin', () => {
  let component: CertificateTabAdmin;
  let fixture: ComponentFixture<CertificateTabAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateTabAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateTabAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
