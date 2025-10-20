import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarRegularUser } from './navbar-regular-user';

describe('NavbarRegularUser', () => {
  let component: NavbarRegularUser;
  let fixture: ComponentFixture<NavbarRegularUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarRegularUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarRegularUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
