import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditarArticuloModalComponent } from './editar-articulo-modal.component';

describe('EditarArticuloModalComponent', () => {
  let component: EditarArticuloModalComponent;
  let fixture: ComponentFixture<EditarArticuloModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditarArticuloModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditarArticuloModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
