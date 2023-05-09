import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearComprobanteComponent } from './crear-comprobante.component';

describe('CrearComprobanteComponent', () => {
  let component: CrearComprobanteComponent;
  let fixture: ComponentFixture<CrearComprobanteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrearComprobanteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearComprobanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
