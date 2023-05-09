import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogGestionComponent } from './dialog-gestion.component';

describe('DialogGestionComponent', () => {
  let component: DialogGestionComponent;
  let fixture: ComponentFixture<DialogGestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogGestionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogGestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
