import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarCaUserComponent } from './navbar-ca-user.component';

describe('NavbarCaUserComponent', () => {
  let component: NavbarCaUserComponent;
  let fixture: ComponentFixture<NavbarCaUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarCaUserComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarCaUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
