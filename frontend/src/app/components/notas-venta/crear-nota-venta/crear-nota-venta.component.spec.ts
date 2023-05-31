import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearNotaVentaComponent } from './crear-nota-venta.component';

describe('CrearNotaVentaComponent', () => {
  let component: CrearNotaVentaComponent;
  let fixture: ComponentFixture<CrearNotaVentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrearNotaVentaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearNotaVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
