import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LotesArticuloModalComponent } from './lotes-articulo-modal.component';

describe('LotesArticuloModalComponent', () => {
  let component: LotesArticuloModalComponent;
  let fixture: ComponentFixture<LotesArticuloModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LotesArticuloModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LotesArticuloModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
