import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDetailFacture, NewDetailFacture } from '../detail-facture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetailFacture for edit and NewDetailFactureFormGroupInput for create.
 */
type DetailFactureFormGroupInput = IDetailFacture | PartialWithRequiredKeyOf<NewDetailFacture>;

type DetailFactureFormDefaults = Pick<NewDetailFacture, 'id'>;

type DetailFactureFormGroupContent = {
  id: FormControl<IDetailFacture['id'] | NewDetailFacture['id']>;
  designation: FormControl<IDetailFacture['designation']>;
  prixUnitaire: FormControl<IDetailFacture['prixUnitaire']>;
  quantite: FormControl<IDetailFacture['quantite']>;
  montantHT: FormControl<IDetailFacture['montantHT']>;
  tauxTVA: FormControl<IDetailFacture['tauxTVA']>;
  montantTVA: FormControl<IDetailFacture['montantTVA']>;
  montantTTC: FormControl<IDetailFacture['montantTTC']>;
  observations: FormControl<IDetailFacture['observations']>;
  facture: FormControl<IDetailFacture['facture']>;
};

export type DetailFactureFormGroup = FormGroup<DetailFactureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetailFactureFormService {
  createDetailFactureFormGroup(detailFacture: DetailFactureFormGroupInput = { id: null }): DetailFactureFormGroup {
    const detailFactureRawValue = {
      ...this.getFormDefaults(),
      ...detailFacture,
    };
    return new FormGroup<DetailFactureFormGroupContent>({
      id: new FormControl(
        { value: detailFactureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      designation: new FormControl(detailFactureRawValue.designation, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      prixUnitaire: new FormControl(detailFactureRawValue.prixUnitaire, {
        validators: [Validators.required, Validators.min(0)],
      }),
      quantite: new FormControl(detailFactureRawValue.quantite, {
        validators: [Validators.required, Validators.min(1)],
      }),
      montantHT: new FormControl(detailFactureRawValue.montantHT),
      tauxTVA: new FormControl(detailFactureRawValue.tauxTVA, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      montantTVA: new FormControl(detailFactureRawValue.montantTVA),
      montantTTC: new FormControl(detailFactureRawValue.montantTTC),
      observations: new FormControl(detailFactureRawValue.observations, {
        validators: [Validators.maxLength(500)],
      }),
      facture: new FormControl(detailFactureRawValue.facture, {
        validators: [Validators.required],
      }),
    });
  }

  getDetailFacture(form: DetailFactureFormGroup): IDetailFacture | NewDetailFacture {
    return form.getRawValue() as IDetailFacture | NewDetailFacture;
  }

  resetForm(form: DetailFactureFormGroup, detailFacture: DetailFactureFormGroupInput): void {
    const detailFactureRawValue = { ...this.getFormDefaults(), ...detailFacture };
    form.reset(
      {
        ...detailFactureRawValue,
        id: { value: detailFactureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DetailFactureFormDefaults {
    return {
      id: null,
    };
  }
}
