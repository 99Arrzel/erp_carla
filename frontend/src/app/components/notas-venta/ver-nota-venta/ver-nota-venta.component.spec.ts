import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerNotaVentaComponent } from './ver-nota-venta.component';

describe('VerNotaVentaComponent', () => {
  let component: VerNotaVentaComponent;
  let fixture: ComponentFixture<VerNotaVentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerNotaVentaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerNotaVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
