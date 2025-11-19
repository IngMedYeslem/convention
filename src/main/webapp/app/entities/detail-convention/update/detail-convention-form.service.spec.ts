import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../detail-convention.test-samples';

import { DetailConventionFormService } from './detail-convention-form.service';

describe('DetailConvention Form Service', () => {
  let service: DetailConventionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetailConventionFormService);
  });

  describe('Service methods', () => {
    describe('createDetailConventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetailConventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            prixUnitaire: expect.any(Object),
            quantite: expect.any(Object),
            montantTotal: expect.any(Object),
            observations: expect.any(Object),
            dateCreation: expect.any(Object),
            convention: expect.any(Object),
          }),
        );
      });

      it('passing IDetailConvention should create a new form with FormGroup', () => {
        const formGroup = service.createDetailConventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            prixUnitaire: expect.any(Object),
            quantite: expect.any(Object),
            montantTotal: expect.any(Object),
            observations: expect.any(Object),
            dateCreation: expect.any(Object),
            convention: expect.any(Object),
          }),
        );
      });
    });

    describe('getDetailConvention', () => {
      it('should return NewDetailConvention for default DetailConvention initial value', () => {
        const formGroup = service.createDetailConventionFormGroup(sampleWithNewData);

        const detailConvention = service.getDetailConvention(formGroup) as any;

        expect(detailConvention).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetailConvention for empty DetailConvention initial value', () => {
        const formGroup = service.createDetailConventionFormGroup();

        const detailConvention = service.getDetailConvention(formGroup) as any;

        expect(detailConvention).toMatchObject({});
      });

      it('should return IDetailConvention', () => {
        const formGroup = service.createDetailConventionFormGroup(sampleWithRequiredData);

        const detailConvention = service.getDetailConvention(formGroup) as any;

        expect(detailConvention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetailConvention should not enable id FormControl', () => {
        const formGroup = service.createDetailConventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetailConvention should disable id FormControl', () => {
        const formGroup = service.createDetailConventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
