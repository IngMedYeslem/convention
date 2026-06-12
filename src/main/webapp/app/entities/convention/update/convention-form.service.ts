import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConvention, NewConvention } from '../convention.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };
type ConventionFormGroupInput = IConvention | PartialWithRequiredKeyOf<NewConvention>;
type FormValueOf<T extends IConvention | NewConvention> = Omit<
  T,
  'dateSignConv' | 'dateDebutConv' | 'dateVisaControleur' | 'dateCreation' | 'dateModification'
> & {
  dateSignConv?: string | null;
  dateDebutConv?: string | null;
  dateVisaControleur?: string | null;
  dateCreation?: string | null;
  dateModification?: string | null;
};
type ConventionFormRawValue = FormValueOf<IConvention>;
type NewConventionFormRawValue = FormValueOf<NewConvention>;
type ConventionFormDefaults = Pick<NewConvention, 'id' | 'dateCreation' | 'dateModification' | 'renouvelable'>;

type ConventionFormGroupContent = {
  id: FormControl<ConventionFormRawValue['id'] | NewConvention['id']>;
  numConvention: FormControl<ConventionFormRawValue['numConvention']>;
  dateSignConv: FormControl<ConventionFormRawValue['dateSignConv']>;
  dateDebutConv: FormControl<ConventionFormRawValue['dateDebutConv']>;
  periodeEcheance: FormControl<ConventionFormRawValue['periodeEcheance']>;
  redevance: FormControl<ConventionFormRawValue['redevance']>;
  nomResponsable: FormControl<ConventionFormRawValue['nomResponsable']>;
  statut: FormControl<ConventionFormRawValue['statut']>;
  dateCreation: FormControl<ConventionFormRawValue['dateCreation']>;
  dateModification: FormControl<ConventionFormRawValue['dateModification']>;
  client: FormControl<ConventionFormRawValue['client']>;
  typeConvention: FormControl<ConventionFormRawValue['typeConvention']>;
  objet: FormControl<ConventionFormRawValue['objet']>;
  directionResponsable: FormControl<ConventionFormRawValue['directionResponsable']>;
  referenceJuridique: FormControl<ConventionFormRawValue['referenceJuridique']>;
  numeroEngagement: FormControl<ConventionFormRawValue['numeroEngagement']>;
  dateVisaControleur: FormControl<ConventionFormRawValue['dateVisaControleur']>;
  valeurTotale: FormControl<ConventionFormRawValue['valeurTotale']>;
  renouvelable: FormControl<ConventionFormRawValue['renouvelable']>;
  nombreRenouvellements: FormControl<ConventionFormRawValue['nombreRenouvellements']>;
  conditionsResiliation: FormControl<ConventionFormRawValue['conditionsResiliation']>;
  penalites: FormControl<ConventionFormRawValue['penalites']>;
  etapeApprobation: FormControl<ConventionFormRawValue['etapeApprobation']>;
  commentaireRejet: FormControl<ConventionFormRawValue['commentaireRejet']>;
};

export type ConventionFormGroup = FormGroup<ConventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConventionFormService {
  createConventionFormGroup(convention: ConventionFormGroupInput = { id: null }): ConventionFormGroup {
    const raw = this.convertConventionToConventionRawValue({ ...this.getFormDefaults(), ...convention });
    return new FormGroup<ConventionFormGroupContent>({
      id: new FormControl({ value: raw.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      numConvention: new FormControl(raw.numConvention, { validators: [Validators.required] }),
      dateSignConv: new FormControl(raw.dateSignConv, { validators: [Validators.required] }),
      dateDebutConv: new FormControl(raw.dateDebutConv, { validators: [Validators.required] }),
      periodeEcheance: new FormControl(raw.periodeEcheance, { validators: [Validators.required] }),
      redevance: new FormControl(raw.redevance, { validators: [Validators.required, Validators.min(0)] }),
      nomResponsable: new FormControl(raw.nomResponsable, { validators: [Validators.required, Validators.maxLength(100)] }),
      statut: new FormControl(raw.statut, { validators: [Validators.required] }),
      dateCreation: new FormControl(raw.dateCreation),
      dateModification: new FormControl(raw.dateModification),
      client: new FormControl(raw.client, { validators: [Validators.required] }),
      typeConvention: new FormControl(raw.typeConvention),
      objet: new FormControl(raw.objet, { validators: [Validators.maxLength(500)] }),
      directionResponsable: new FormControl(raw.directionResponsable, { validators: [Validators.maxLength(200)] }),
      referenceJuridique: new FormControl(raw.referenceJuridique, { validators: [Validators.maxLength(200)] }),
      numeroEngagement: new FormControl(raw.numeroEngagement, { validators: [Validators.maxLength(100)] }),
      dateVisaControleur: new FormControl(raw.dateVisaControleur),
      valeurTotale: new FormControl(raw.valeurTotale, { validators: [Validators.min(0)] }),
      renouvelable: new FormControl(raw.renouvelable),
      nombreRenouvellements: new FormControl(raw.nombreRenouvellements),
      conditionsResiliation: new FormControl(raw.conditionsResiliation),
      penalites: new FormControl(raw.penalites),
      etapeApprobation: new FormControl(raw.etapeApprobation),
      commentaireRejet: new FormControl(raw.commentaireRejet, { validators: [Validators.maxLength(500)] }),
    });
  }

  getConvention(form: ConventionFormGroup): IConvention | NewConvention {
    return this.convertConventionRawValueToConvention(form.getRawValue() as ConventionFormRawValue | NewConventionFormRawValue);
  }

  resetForm(form: ConventionFormGroup, convention: ConventionFormGroupInput): void {
    const raw = this.convertConventionToConventionRawValue({ ...this.getFormDefaults(), ...convention });
    form.reset({ ...raw, id: { value: raw.id, disabled: true } } as any);
  }

  private getFormDefaults(): ConventionFormDefaults {
    const now = dayjs();
    return { id: null, dateCreation: now, dateModification: now, renouvelable: false };
  }

  private convertConventionRawValueToConvention(raw: ConventionFormRawValue | NewConventionFormRawValue): IConvention | NewConvention {
    return {
      ...raw,
      dateSignConv: raw.dateSignConv ? dayjs(raw.dateSignConv) : null,
      dateDebutConv: raw.dateDebutConv ? dayjs(raw.dateDebutConv) : null,
      dateVisaControleur: raw.dateVisaControleur ? dayjs(raw.dateVisaControleur) : null,
      dateCreation: dayjs(raw.dateCreation, DATE_TIME_FORMAT),
      dateModification: dayjs(raw.dateModification, DATE_TIME_FORMAT),
    };
  }

  private convertConventionToConventionRawValue(
    convention: IConvention | (Partial<NewConvention> & ConventionFormDefaults),
  ): ConventionFormRawValue | PartialWithRequiredKeyOf<NewConventionFormRawValue> {
    return {
      ...convention,
      dateSignConv: convention.dateSignConv ? convention.dateSignConv.format(DATE_FORMAT) : null,
      dateDebutConv: convention.dateDebutConv ? convention.dateDebutConv.format(DATE_FORMAT) : null,
      dateVisaControleur: convention.dateVisaControleur ? convention.dateVisaControleur.format(DATE_FORMAT) : null,
      dateCreation: convention.dateCreation ? convention.dateCreation.format(DATE_TIME_FORMAT) : undefined,
      dateModification: convention.dateModification ? convention.dateModification.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
