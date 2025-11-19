import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../facture.test-samples';

import { FactureFormService } from './facture-form.service';

describe('Facture Form Service', () => {
  let service: FactureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FactureFormService);
  });

  describe('Service methods', () => {
    describe('createFactureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFactureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numFacture: expect.any(Object),
            dateFacture: expect.any(Object),
            montantTotal: expect.any(Object),
            montantTTC: expect.any(Object),
            tva: expect.any(Object),
            observations: expect.any(Object),
            ancienneRef: expect.any(Object),
            typeFacture: expect.any(Object),
            statut: expect.any(Object),
            dateEcheance: expect.any(Object),
            dateCreation: expect.any(Object),
            client: expect.any(Object),
            convention: expect.any(Object),
          }),
        );
      });

      it('passing IFacture should create a new form with FormGroup', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numFacture: expect.any(Object),
            dateFacture: expect.any(Object),
            montantTotal: expect.any(Object),
            montantTTC: expect.any(Object),
            tva: expect.any(Object),
            observations: expect.any(Object),
            ancienneRef: expect.any(Object),
            typeFacture: expect.any(Object),
            statut: expect.any(Object),
            dateEcheance: expect.any(Object),
            dateCreation: expect.any(Object),
            client: expect.any(Object),
            convention: expect.any(Object),
          }),
        );
      });
    });

    describe('getFacture', () => {
      it('should return NewFacture for default Facture initial value', () => {
        const formGroup = service.createFactureFormGroup(sampleWithNewData);

        const facture = service.getFacture(formGroup) as any;

        expect(facture).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacture for empty Facture initial value', () => {
        const formGroup = service.createFactureFormGroup();

        const facture = service.getFacture(formGroup) as any;

        expect(facture).toMatchObject({});
      });

      it('should return IFacture', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);

        const facture = service.getFacture(formGroup) as any;

        expect(facture).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacture should not enable id FormControl', () => {
        const formGroup = service.createFactureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacture should disable id FormControl', () => {
        const formGroup = service.createFactureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
