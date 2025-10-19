import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevokeDialog } from './revoke-dialog';

describe('RevokeDialog', () => {
  let component: RevokeDialog;
  let fixture: ComponentFixture<RevokeDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RevokeDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevokeDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
