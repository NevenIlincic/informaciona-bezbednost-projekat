import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificatesList } from './certificates-list';

describe('CertificatesList', () => {
  let component: CertificatesList;
  let fixture: ComponentFixture<CertificatesList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificatesList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificatesList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
