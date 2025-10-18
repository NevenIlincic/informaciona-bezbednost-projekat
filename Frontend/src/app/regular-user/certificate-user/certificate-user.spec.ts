import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificateUser } from './certificate-user';

describe('CertificateUser', () => {
  let component: CertificateUser;
  let fixture: ComponentFixture<CertificateUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificateUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificateUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
