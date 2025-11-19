import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConventionDetailComponent } from './convention-detail.component';

describe('Convention Management Detail Component', () => {
  let comp: ConventionDetailComponent;
  let fixture: ComponentFixture<ConventionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConventionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./convention-detail.component').then(m => m.ConventionDetailComponent),
              resolve: { convention: () => of({ id: 2390 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ConventionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConventionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load convention on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ConventionDetailComponent);

      // THEN
      expect(instance.convention()).toEqual(expect.objectContaining({ id: 2390 }));
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
