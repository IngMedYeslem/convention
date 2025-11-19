import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConvention, NewConvention } from '../convention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConvention for edit and NewConventionFormGroupInput for create.
 */
type ConventionFormGroupInput = IConvention | PartialWithRequiredKeyOf<NewConvention>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IConvention | NewConvention> = Omit<T, 'dateCreation' | 'dateModification'> & {
  dateCreation?: string | null;
  dateModification?: string | null;
};

type ConventionFormRawValue = FormValueOf<IConvention>;

type NewConventionFormRawValue = FormValueOf<NewConvention>;

type ConventionFormDefaults = Pick<NewConvention, 'id' | 'dateCreation' | 'dateModification'>;

type ConventionFormGroupContent = {
  id: FormControl<ConventionFormRawValue['id'] | NewConvention['id']>;
  numConvention: FormControl<ConventionFormRawValue['numConvention']>;
  dateSignConv: FormControl<ConventionFormRawValue['dateSignConv']>;
  dateDebutConv: FormControl<ConventionFormRawValue['dateDebutConv']>;
  echeanceConv: FormControl<ConventionFormRawValue['echeanceConv']>;
  redevance: FormControl<ConventionFormRawValue['redevance']>;
  nomResponsable: FormControl<ConventionFormRawValue['nomResponsable']>;
  statut: FormControl<ConventionFormRawValue['statut']>;
  dateCreation: FormControl<ConventionFormRawValue['dateCreation']>;
  dateModification: FormControl<ConventionFormRawValue['dateModification']>;
  client: FormControl<ConventionFormRawValue['client']>;
};

export type ConventionFormGroup = FormGroup<ConventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConventionFormService {
  createConventionFormGroup(convention: ConventionFormGroupInput = { id: null }): ConventionFormGroup {
    const conventionRawValue = this.convertConventionToConventionRawValue({
      ...this.getFormDefaults(),
      ...convention,
    });
    return new FormGroup<ConventionFormGroupContent>({
      id: new FormControl(
        { value: conventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numConvention: new FormControl(conventionRawValue.numConvention, {
        validators: [Validators.required],
      }),
      dateSignConv: new FormControl(conventionRawValue.dateSignConv, {
        validators: [Validators.required],
      }),
      dateDebutConv: new FormControl(conventionRawValue.dateDebutConv, {
        validators: [Validators.required],
      }),
      echeanceConv: new FormControl(conventionRawValue.echeanceConv, {
        validators: [Validators.required],
      }),
      redevance: new FormControl(conventionRawValue.redevance, {
        validators: [Validators.required, Validators.min(0)],
      }),
      nomResponsable: new FormControl(conventionRawValue.nomResponsable, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      statut: new FormControl(conventionRawValue.statut, {
        validators: [Validators.required],
      }),
      dateCreation: new FormControl(conventionRawValue.dateCreation),
      dateModification: new FormControl(conventionRawValue.dateModification),
      client: new FormControl(conventionRawValue.client, {
        validators: [Validators.required],
      }),
    });
  }

  getConvention(form: ConventionFormGroup): IConvention | NewConvention {
    return this.convertConventionRawValueToConvention(form.getRawValue() as ConventionFormRawValue | NewConventionFormRawValue);
  }

  resetForm(form: ConventionFormGroup, convention: ConventionFormGroupInput): void {
    const conventionRawValue = this.convertConventionToConventionRawValue({ ...this.getFormDefaults(), ...convention });
    form.reset(
      {
        ...conventionRawValue,
        id: { value: conventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ConventionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      dateModification: currentTime,
    };
  }

  private convertConventionRawValueToConvention(
    rawConvention: ConventionFormRawValue | NewConventionFormRawValue,
  ): IConvention | NewConvention {
    return {
      ...rawConvention,
      dateCreation: dayjs(rawConvention.dateCreation, DATE_TIME_FORMAT),
      dateModification: dayjs(rawConvention.dateModification, DATE_TIME_FORMAT),
    };
  }

  private convertConventionToConventionRawValue(
    convention: IConvention | (Partial<NewConvention> & ConventionFormDefaults),
  ): ConventionFormRawValue | PartialWithRequiredKeyOf<NewConventionFormRawValue> {
    return {
      ...convention,
      dateCreation: convention.dateCreation ? convention.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      dateModification: convention.dateModification ? convention.dateModification.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
