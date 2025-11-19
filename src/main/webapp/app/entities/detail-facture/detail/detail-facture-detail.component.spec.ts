import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DetailFactureDetailComponent } from './detail-facture-detail.component';

describe('DetailFacture Management Detail Component', () => {
  let comp: DetailFactureDetailComponent;
  let fixture: ComponentFixture<DetailFactureDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailFactureDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./detail-facture-detail.component').then(m => m.DetailFactureDetailComponent),
              resolve: { detailFacture: () => of({ id: 17963 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DetailFactureDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailFactureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load detailFacture on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DetailFactureDetailComponent);

      // THEN
      expect(instance.detailFacture()).toEqual(expect.objectContaining({ id: 17963 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
