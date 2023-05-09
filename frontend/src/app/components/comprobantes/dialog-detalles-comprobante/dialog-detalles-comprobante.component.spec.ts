import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogDetallesComprobanteComponent } from './dialog-detalles-comprobante.component';

describe('DialogDetallesComprobanteComponent', () => {
  let component: DialogDetallesComprobanteComponent;
  let fixture: ComponentFixture<DialogDetallesComprobanteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogDetallesComprobanteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogDetallesComprobanteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
