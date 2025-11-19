import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IConvention } from 'app/entities/convention/convention.model';
import { ConventionService } from 'app/entities/convention/service/convention.service';
import { DetailConventionService } from '../service/detail-convention.service';
import { IDetailConvention } from '../detail-convention.model';
import { DetailConventionFormService } from './detail-convention-form.service';

import { DetailConventionUpdateComponent } from './detail-convention-update.component';

describe('DetailConvention Management Update Component', () => {
  let comp: DetailConventionUpdateComponent;
  let fixture: ComponentFixture<DetailConventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detailConventionFormService: DetailConventionFormService;
  let detailConventionService: DetailConventionService;
  let conventionService: ConventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DetailConventionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DetailConventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailConventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detailConventionFormService = TestBed.inject(DetailConventionFormService);
    detailConventionService = TestBed.inject(DetailConventionService);
    conventionService = TestBed.inject(ConventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Convention query and add missing value', () => {
      const detailConvention: IDetailConvention = { id: 24555 };
      const convention: IConvention = { id: 2390 };
      detailConvention.convention = convention;

      const conventionCollection: IConvention[] = [{ id: 2390 }];
      jest.spyOn(conventionService, 'query').mockReturnValue(of(new HttpResponse({ body: conventionCollection })));
      const additionalConventions = [convention];
      const expectedCollection: IConvention[] = [...additionalConventions, ...conventionCollection];
      jest.spyOn(conventionService, 'addConventionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailConvention });
      comp.ngOnInit();

      expect(conventionService.query).toHaveBeenCalled();
      expect(conventionService.addConventionToCollectionIfMissing).toHaveBeenCalledWith(
        conventionCollection,
        ...additionalConventions.map(expect.objectContaining),
      );
      expect(comp.conventionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const detailConvention: IDetailConvention = { id: 24555 };
      const convention: IConvention = { id: 2390 };
      detailConvention.convention = convention;

      activatedRoute.data = of({ detailConvention });
      comp.ngOnInit();

      expect(comp.conventionsSharedCollection).toContainEqual(convention);
      expect(comp.detailConvention).toEqual(detailConvention);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailConvention>>();
      const detailConvention = { id: 29255 };
      jest.spyOn(detailConventionFormService, 'getDetailConvention').mockReturnValue(detailConvention);
      jest.spyOn(detailConventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailConvention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailConvention }));
      saveSubject.complete();

      // THEN
      expect(detailConventionFormService.getDetailConvention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detailConventionService.update).toHaveBeenCalledWith(expect.objectContaining(detailConvention));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailConvention>>();
      const detailConvention = { id: 29255 };
      jest.spyOn(detailConventionFormService, 'getDetailConvention').mockReturnValue({ id: null });
      jest.spyOn(detailConventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailConvention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailConvention }));
      saveSubject.complete();

      // THEN
      expect(detailConventionFormService.getDetailConvention).toHaveBeenCalled();
      expect(detailConventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailConvention>>();
      const detailConvention = { id: 29255 };
      jest.spyOn(detailConventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailConvention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detailConventionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareConvention', () => {
      it('should forward to conventionService', () => {
        const entity = { id: 2390 };
        const entity2 = { id: 7509 };
        jest.spyOn(conventionService, 'compareConvention');
        comp.compareConvention(entity, entity2);
        expect(conventionService.compareConvention).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
