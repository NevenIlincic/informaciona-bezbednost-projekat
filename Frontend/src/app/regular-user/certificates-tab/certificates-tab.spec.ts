import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificatesTab } from './certificates-tab';

describe('CertificatesTab', () => {
  let component: CertificatesTab;
  let fixture: ComponentFixture<CertificatesTab>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificatesTab]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CertificatesTab);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
