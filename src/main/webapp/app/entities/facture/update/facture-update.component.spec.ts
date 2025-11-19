import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IConvention } from 'app/entities/convention/convention.model';
import { ConventionService } from 'app/entities/convention/service/convention.service';
import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';
import { FactureFormService } from './facture-form.service';

import { FactureUpdateComponent } from './facture-update.component';

describe('Facture Management Update Component', () => {
  let comp: FactureUpdateComponent;
  let fixture: ComponentFixture<FactureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let factureFormService: FactureFormService;
  let factureService: FactureService;
  let clientService: ClientService;
  let conventionService: ConventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FactureUpdateComponent],
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
      .overrideTemplate(FactureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FactureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    factureFormService = TestBed.inject(FactureFormService);
    factureService = TestBed.inject(FactureService);
    clientService = TestBed.inject(ClientService);
    conventionService = TestBed.inject(ConventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Client query and add missing value', () => {
      const facture: IFacture = { id: 30124 };
      const client: IClient = { id: 26282 };
      facture.client = client;

      const clientCollection: IClient[] = [{ id: 26282 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Convention query and add missing value', () => {
      const facture: IFacture = { id: 30124 };
      const convention: IConvention = { id: 2390 };
      facture.convention = convention;

      const conventionCollection: IConvention[] = [{ id: 2390 }];
      jest.spyOn(conventionService, 'query').mockReturnValue(of(new HttpResponse({ body: conventionCollection })));
      const additionalConventions = [convention];
      const expectedCollection: IConvention[] = [...additionalConventions, ...conventionCollection];
      jest.spyOn(conventionService, 'addConventionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(conventionService.query).toHaveBeenCalled();
      expect(conventionService.addConventionToCollectionIfMissing).toHaveBeenCalledWith(
        conventionCollection,
        ...additionalConventions.map(expect.objectContaining),
      );
      expect(comp.conventionsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const facture: IFacture = { id: 30124 };
      const client: IClient = { id: 26282 };
      facture.client = client;
      const convention: IConvention = { id: 2390 };
      facture.convention = convention;

      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      expect(comp.clientsSharedCollection).toContainEqual(client);
      expect(comp.conventionsSharedCollection).toContainEqual(convention);
      expect(comp.facture).toEqual(facture);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 29649 };
      jest.spyOn(factureFormService, 'getFacture').mockReturnValue(facture);
      jest.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facture }));
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(factureService.update).toHaveBeenCalledWith(expect.objectContaining(facture));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 29649 };
      jest.spyOn(factureFormService, 'getFacture').mockReturnValue({ id: null });
      jest.spyOn(factureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: facture }));
      saveSubject.complete();

      // THEN
      expect(factureFormService.getFacture).toHaveBeenCalled();
      expect(factureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFacture>>();
      const facture = { id: 29649 };
      jest.spyOn(factureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ facture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(factureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClient', () => {
      it('should forward to clientService', () => {
        const entity = { id: 26282 };
        const entity2 = { id: 16836 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
