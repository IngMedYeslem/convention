import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDetailConvention, NewDetailConvention } from '../detail-convention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDetailConvention for edit and NewDetailConventionFormGroupInput for create.
 */
type DetailConventionFormGroupInput = IDetailConvention | PartialWithRequiredKeyOf<NewDetailConvention>;

type DetailConventionFormDefaults = Pick<NewDetailConvention, 'id'>;

type DetailConventionFormGroupContent = {
  id: FormControl<IDetailConvention['id'] | NewDetailConvention['id']>;
  designation: FormControl<IDetailConvention['designation']>;
  prixUnitaire: FormControl<IDetailConvention['prixUnitaire']>;
  quantite: FormControl<IDetailConvention['quantite']>;
  montantTotal: FormControl<IDetailConvention['montantTotal']>;
  observations: FormControl<IDetailConvention['observations']>;
  dateCreation: FormControl<IDetailConvention['dateCreation']>;
  convention: FormControl<IDetailConvention['convention']>;
};

export type DetailConventionFormGroup = FormGroup<DetailConventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DetailConventionFormService {
  createDetailConventionFormGroup(detailConvention: DetailConventionFormGroupInput = { id: null }): DetailConventionFormGroup {
    const detailConventionRawValue = {
      ...this.getFormDefaults(),
      ...detailConvention,
    };
    return new FormGroup<DetailConventionFormGroupContent>({
      id: new FormControl(
        { value: detailConventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      designation: new FormControl(detailConventionRawValue.designation, {
        validators: [Validators.required, Validators.maxLength(200)],
      }),
      prixUnitaire: new FormControl(detailConventionRawValue.prixUnitaire, {
        validators: [Validators.required, Validators.min(0)],
      }),
      quantite: new FormControl(detailConventionRawValue.quantite, {
        validators: [Validators.required, Validators.min(1)],
      }),
      montantTotal: new FormControl(detailConventionRawValue.montantTotal),
      observations: new FormControl(detailConventionRawValue.observations),
      dateCreation: new FormControl(detailConventionRawValue.dateCreation),
      convention: new FormControl(detailConventionRawValue.convention, {
        validators: [Validators.required],
      }),
    });
  }

  getDetailConvention(form: DetailConventionFormGroup): IDetailConvention | NewDetailConvention {
    return form.getRawValue() as IDetailConvention | NewDetailConvention;
  }

  resetForm(form: DetailConventionFormGroup, detailConvention: DetailConventionFormGroupInput): void {
    const detailConventionRawValue = { ...this.getFormDefaults(), ...detailConvention };
    form.reset(
      {
        ...detailConventionRawValue,
        id: { value: detailConventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DetailConventionFormDefaults {
    return {
      id: null,
    };
  }
}
