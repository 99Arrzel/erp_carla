import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotasVentaComponent } from './notas-venta.component';

describe('NotasVentaComponent', () => {
  let component: NotasVentaComponent;
  let fixture: ComponentFixture<NotasVentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotasVentaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotasVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
