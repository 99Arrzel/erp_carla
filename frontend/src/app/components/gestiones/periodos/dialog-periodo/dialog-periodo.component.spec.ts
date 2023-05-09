import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogPeriodoComponent } from './dialog-periodo.component';

describe('DialogPeriodoComponent', () => {
  let component: DialogPeriodoComponent;
  let fixture: ComponentFixture<DialogPeriodoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogPeriodoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogPeriodoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
