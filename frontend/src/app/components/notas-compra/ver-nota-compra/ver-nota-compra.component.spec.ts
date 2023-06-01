import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerNotaCompraComponent } from './ver-nota-compra.component';

describe('VerNotaCompraComponent', () => {
  let component: VerNotaCompraComponent;
  let fixture: ComponentFixture<VerNotaCompraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerNotaCompraComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerNotaCompraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
