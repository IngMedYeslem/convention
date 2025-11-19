import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFacture } from 'app/entities/facture/facture.model';
import { FactureService } from 'app/entities/facture/service/facture.service';
import { DetailFactureService } from '../service/detail-facture.service';
import { IDetailFacture } from '../detail-facture.model';
import { DetailFactureFormService } from './detail-facture-form.service';

import { DetailFactureUpdateComponent } from './detail-facture-update.component';

describe('DetailFacture Management Update Component', () => {
  let comp: DetailFactureUpdateComponent;
  let fixture: ComponentFixture<DetailFactureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let detailFactureFormService: DetailFactureFormService;
  let detailFactureService: DetailFactureService;
  let factureService: FactureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DetailFactureUpdateComponent],
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
      .overrideTemplate(DetailFactureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DetailFactureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    detailFactureFormService = TestBed.inject(DetailFactureFormService);
    detailFactureService = TestBed.inject(DetailFactureService);
    factureService = TestBed.inject(FactureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Facture query and add missing value', () => {
      const detailFacture: IDetailFacture = { id: 23380 };
      const facture: IFacture = { id: 29649 };
      detailFacture.facture = facture;

      const factureCollection: IFacture[] = [{ id: 29649 }];
      jest.spyOn(factureService, 'query').mockReturnValue(of(new HttpResponse({ body: factureCollection })));
      const additionalFactures = [facture];
      const expectedCollection: IFacture[] = [...additionalFactures, ...factureCollection];
      jest.spyOn(factureService, 'addFactureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ detailFacture });
      comp.ngOnInit();

      expect(factureService.query).toHaveBeenCalled();
      expect(factureService.addFactureToCollectionIfMissing).toHaveBeenCalledWith(
        factureCollection,
        ...additionalFactures.map(expect.objectContaining),
      );
      expect(comp.facturesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const detailFacture: IDetailFacture = { id: 23380 };
      const facture: IFacture = { id: 29649 };
      detailFacture.facture = facture;

      activatedRoute.data = of({ detailFacture });
      comp.ngOnInit();

      expect(comp.facturesSharedCollection).toContainEqual(facture);
      expect(comp.detailFacture).toEqual(detailFacture);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailFacture>>();
      const detailFacture = { id: 17963 };
      jest.spyOn(detailFactureFormService, 'getDetailFacture').mockReturnValue(detailFacture);
      jest.spyOn(detailFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailFacture }));
      saveSubject.complete();

      // THEN
      expect(detailFactureFormService.getDetailFacture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(detailFactureService.update).toHaveBeenCalledWith(expect.objectContaining(detailFacture));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailFacture>>();
      const detailFacture = { id: 17963 };
      jest.spyOn(detailFactureFormService, 'getDetailFacture').mockReturnValue({ id: null });
      jest.spyOn(detailFactureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailFacture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: detailFacture }));
      saveSubject.complete();

      // THEN
      expect(detailFactureFormService.getDetailFacture).toHaveBeenCalled();
      expect(detailFactureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDetailFacture>>();
      const detailFacture = { id: 17963 };
      jest.spyOn(detailFactureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ detailFacture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(detailFactureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFacture', () => {
      it('should forward to factureService', () => {
        const entity = { id: 29649 };
        const entity2 = { id: 30124 };
        jest.spyOn(factureService, 'compareFacture');
        comp.compareFacture(entity, entity2);
        expect(factureService.compareFacture).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
