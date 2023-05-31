import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotasCompraComponent } from './notas-compra.component';

describe('NotasCompraComponent', () => {
  let component: NotasCompraComponent;
  let fixture: ComponentFixture<NotasCompraComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotasCompraComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotasCompraComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
