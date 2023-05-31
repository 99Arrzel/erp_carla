import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CuentasIntegracionComponent } from './cuentas-integracion.component';

describe('CuentasIntegracionComponent', () => {
  let component: CuentasIntegracionComponent;
  let fixture: ComponentFixture<CuentasIntegracionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CuentasIntegracionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CuentasIntegracionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
