import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogEmpresaComponent } from './dialog-empresa.component';

describe('DialogEmpresaComponent', () => {
  let component: DialogEmpresaComponent;
  let fixture: ComponentFixture<DialogEmpresaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogEmpresaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogEmpresaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
