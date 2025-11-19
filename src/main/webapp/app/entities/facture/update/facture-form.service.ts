import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFacture, NewFacture } from '../facture.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacture for edit and NewFactureFormGroupInput for create.
 */
type FactureFormGroupInput = IFacture | PartialWithRequiredKeyOf<NewFacture>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFacture | NewFacture> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

type FactureFormRawValue = FormValueOf<IFacture>;

type NewFactureFormRawValue = FormValueOf<NewFacture>;

type FactureFormDefaults = Pick<NewFacture, 'id' | 'dateCreation'>;

type FactureFormGroupContent = {
  id: FormControl<FactureFormRawValue['id'] | NewFacture['id']>;
  numFacture: FormControl<FactureFormRawValue['numFacture']>;
  dateFacture: FormControl<FactureFormRawValue['dateFacture']>;
  montantTotal: FormControl<FactureFormRawValue['montantTotal']>;
  montantTTC: FormControl<FactureFormRawValue['montantTTC']>;
  tva: FormControl<FactureFormRawValue['tva']>;
  observations: FormControl<FactureFormRawValue['observations']>;
  ancienneRef: FormControl<FactureFormRawValue['ancienneRef']>;
  typeFacture: FormControl<FactureFormRawValue['typeFacture']>;
  statut: FormControl<FactureFormRawValue['statut']>;
  dateEcheance: FormControl<FactureFormRawValue['dateEcheance']>;
  dateCreation: FormControl<FactureFormRawValue['dateCreation']>;
  client: FormControl<FactureFormRawValue['client']>;
  convention: FormControl<FactureFormRawValue['convention']>;
};

export type FactureFormGroup = FormGroup<FactureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FactureFormService {
  createFactureFormGroup(facture: FactureFormGroupInput = { id: null }): FactureFormGroup {
    const factureRawValue = this.convertFactureToFactureRawValue({
      ...this.getFormDefaults(),
      ...facture,
    });
    return new FormGroup<FactureFormGroupContent>({
      id: new FormControl(
        { value: factureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numFacture: new FormControl(factureRawValue.numFacture, {
        validators: [Validators.required],
      }),
      dateFacture: new FormControl(factureRawValue.dateFacture, {
        validators: [Validators.required],
      }),
      montantTotal: new FormControl(factureRawValue.montantTotal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      montantTTC: new FormControl(factureRawValue.montantTTC),
      tva: new FormControl(factureRawValue.tva, {
        validators: [Validators.min(0), Validators.max(100)],
      }),
      observations: new FormControl(factureRawValue.observations),
      ancienneRef: new FormControl(factureRawValue.ancienneRef, {
        validators: [Validators.maxLength(50)],
      }),
      typeFacture: new FormControl(factureRawValue.typeFacture, {
        validators: [Validators.required],
      }),
      statut: new FormControl(factureRawValue.statut, {
        validators: [Validators.required],
      }),
      dateEcheance: new FormControl(factureRawValue.dateEcheance),
      dateCreation: new FormControl(factureRawValue.dateCreation),
      client: new FormControl(factureRawValue.client, {
        validators: [Validators.required],
      }),
      convention: new FormControl(factureRawValue.convention),
    });
  }

  getFacture(form: FactureFormGroup): IFacture | NewFacture {
    return this.convertFactureRawValueToFacture(form.getRawValue() as FactureFormRawValue | NewFactureFormRawValue);
  }

  resetForm(form: FactureFormGroup, facture: FactureFormGroupInput): void {
    const factureRawValue = this.convertFactureToFactureRawValue({ ...this.getFormDefaults(), ...facture });
    form.reset(
      {
        ...factureRawValue,
        id: { value: factureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FactureFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
    };
  }

  private convertFactureRawValueToFacture(rawFacture: FactureFormRawValue | NewFactureFormRawValue): IFacture | NewFacture {
    return {
      ...rawFacture,
      dateCreation: dayjs(rawFacture.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertFactureToFactureRawValue(
    facture: IFacture | (Partial<NewFacture> & FactureFormDefaults),
  ): FactureFormRawValue | PartialWithRequiredKeyOf<NewFactureFormRawValue> {
    return {
      ...facture,
      dateCreation: facture.dateCreation ? facture.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
