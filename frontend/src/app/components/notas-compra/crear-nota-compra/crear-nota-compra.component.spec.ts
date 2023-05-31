import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearNotaCompraComponent } from './crear-nota-compra.component';

describe('CrearNotaCompraComponent', () => {
  let component: CrearNotaCompraComponent;
  let fixture: ComponentFixture<CrearNotaCompraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrearNotaCompraComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearNotaCompraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
