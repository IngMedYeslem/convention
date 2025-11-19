import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ConventionService } from '../service/convention.service';
import { IConvention } from '../convention.model';
import { ConventionFormService } from './convention-form.service';

import { ConventionUpdateComponent } from './convention-update.component';

describe('Convention Management Update Component', () => {
  let comp: ConventionUpdateComponent;
  let fixture: ComponentFixture<ConventionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conventionFormService: ConventionFormService;
  let conventionService: ConventionService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConventionUpdateComponent],
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
      .overrideTemplate(ConventionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConventionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conventionFormService = TestBed.inject(ConventionFormService);
    conventionService = TestBed.inject(ConventionService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Client query and add missing value', () => {
      const convention: IConvention = { id: 7509 };
      const client: IClient = { id: 26282 };
      convention.client = client;

      const clientCollection: IClient[] = [{ id: 26282 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const convention: IConvention = { id: 7509 };
      const client: IClient = { id: 26282 };
      convention.client = client;

      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      expect(comp.clientsSharedCollection).toContainEqual(client);
      expect(comp.convention).toEqual(convention);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 2390 };
      jest.spyOn(conventionFormService, 'getConvention').mockReturnValue(convention);
      jest.spyOn(conventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: convention }));
      saveSubject.complete();

      // THEN
      expect(conventionFormService.getConvention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conventionService.update).toHaveBeenCalledWith(expect.objectContaining(convention));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 2390 };
      jest.spyOn(conventionFormService, 'getConvention').mockReturnValue({ id: null });
      jest.spyOn(conventionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: convention }));
      saveSubject.complete();

      // THEN
      expect(conventionFormService.getConvention).toHaveBeenCalled();
      expect(conventionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConvention>>();
      const convention = { id: 2390 };
      jest.spyOn(conventionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ convention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conventionService.update).toHaveBeenCalled();
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
  });
});
