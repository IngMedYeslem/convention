import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../detail-facture.test-samples';

import { DetailFactureFormService } from './detail-facture-form.service';

describe('DetailFacture Form Service', () => {
  let service: DetailFactureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetailFactureFormService);
  });

  describe('Service methods', () => {
    describe('createDetailFactureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDetailFactureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            prixUnitaire: expect.any(Object),
            quantite: expect.any(Object),
            montantHT: expect.any(Object),
            tauxTVA: expect.any(Object),
            montantTVA: expect.any(Object),
            montantTTC: expect.any(Object),
            observations: expect.any(Object),
            facture: expect.any(Object),
          }),
        );
      });

      it('passing IDetailFacture should create a new form with FormGroup', () => {
        const formGroup = service.createDetailFactureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            prixUnitaire: expect.any(Object),
            quantite: expect.any(Object),
            montantHT: expect.any(Object),
            tauxTVA: expect.any(Object),
            montantTVA: expect.any(Object),
            montantTTC: expect.any(Object),
            observations: expect.any(Object),
            facture: expect.any(Object),
          }),
        );
      });
    });

    describe('getDetailFacture', () => {
      it('should return NewDetailFacture for default DetailFacture initial value', () => {
        const formGroup = service.createDetailFactureFormGroup(sampleWithNewData);

        const detailFacture = service.getDetailFacture(formGroup) as any;

        expect(detailFacture).toMatchObject(sampleWithNewData);
      });

      it('should return NewDetailFacture for empty DetailFacture initial value', () => {
        const formGroup = service.createDetailFactureFormGroup();

        const detailFacture = service.getDetailFacture(formGroup) as any;

        expect(detailFacture).toMatchObject({});
      });

      it('should return IDetailFacture', () => {
        const formGroup = service.createDetailFactureFormGroup(sampleWithRequiredData);

        const detailFacture = service.getDetailFacture(formGroup) as any;

        expect(detailFacture).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDetailFacture should not enable id FormControl', () => {
        const formGroup = service.createDetailFactureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDetailFacture should disable id FormControl', () => {
        const formGroup = service.createDetailFactureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
