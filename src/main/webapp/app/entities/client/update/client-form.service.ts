import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IClient, NewClient } from '../client.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;
type FormValueOf<T extends IClient | NewClient> = Omit<T, 'dateCreation'> & { dateCreation?: string | null };
type ClientFormRawValue = FormValueOf<IClient>;
type NewClientFormRawValue = FormValueOf<NewClient>;
type ClientFormDefaults = Pick<NewClient, 'id' | 'dateCreation' | 'actif'>;

type ClientFormGroupContent = {
  id: FormControl<ClientFormRawValue['id'] | NewClient['id']>;
  numClient: FormControl<ClientFormRawValue['numClient']>;
  nomClient: FormControl<ClientFormRawValue['nomClient']>;
  adresseClient: FormControl<ClientFormRawValue['adresseClient']>;
  emailClient: FormControl<ClientFormRawValue['emailClient']>;
  whatsClient: FormControl<ClientFormRawValue['whatsClient']>;
  obsClient: FormControl<ClientFormRawValue['obsClient']>;
  dateCreation: FormControl<ClientFormRawValue['dateCreation']>;
  actif: FormControl<ClientFormRawValue['actif']>;
  typeInstitution: FormControl<ClientFormRawValue['typeInstitution']>;
  nif: FormControl<ClientFormRawValue['nif']>;
  rc: FormControl<ClientFormRawValue['rc']>;
  nomRepresentant: FormControl<ClientFormRawValue['nomRepresentant']>;
  fonctionRepresentant: FormControl<ClientFormRawValue['fonctionRepresentant']>;
  qualiteSignature: FormControl<ClientFormRawValue['qualiteSignature']>;
  wilaya: FormControl<ClientFormRawValue['wilaya']>;
  commune: FormControl<ClientFormRawValue['commune']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl({ value: clientRawValue.id, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
      numClient: new FormControl(clientRawValue.numClient, { validators: [Validators.required] }),
      nomClient: new FormControl(clientRawValue.nomClient, { validators: [Validators.required, Validators.maxLength(100)] }),
      adresseClient: new FormControl(clientRawValue.adresseClient, { validators: [Validators.maxLength(255)] }),
      emailClient: new FormControl(clientRawValue.emailClient, {
        validators: [Validators.pattern('^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$')],
      }),
      whatsClient: new FormControl(clientRawValue.whatsClient, { validators: [Validators.maxLength(20)] }),
      obsClient: new FormControl(clientRawValue.obsClient),
      dateCreation: new FormControl(clientRawValue.dateCreation),
      actif: new FormControl(clientRawValue.actif, { validators: [Validators.required] }),
      typeInstitution: new FormControl(clientRawValue.typeInstitution),
      nif: new FormControl(clientRawValue.nif, { validators: [Validators.maxLength(50)] }),
      rc: new FormControl(clientRawValue.rc, { validators: [Validators.maxLength(50)] }),
      nomRepresentant: new FormControl(clientRawValue.nomRepresentant, { validators: [Validators.maxLength(150)] }),
      fonctionRepresentant: new FormControl(clientRawValue.fonctionRepresentant, { validators: [Validators.maxLength(100)] }),
      qualiteSignature: new FormControl(clientRawValue.qualiteSignature),
      wilaya: new FormControl(clientRawValue.wilaya, { validators: [Validators.maxLength(100)] }),
      commune: new FormControl(clientRawValue.commune, { validators: [Validators.maxLength(100)] }),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return this.convertClientRawValueToClient(form.getRawValue() as ClientFormRawValue | NewClientFormRawValue);
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    form.reset({ ...clientRawValue, id: { value: clientRawValue.id, disabled: true } } as any);
  }

  private getFormDefaults(): ClientFormDefaults {
    return { id: null, dateCreation: dayjs(), actif: false };
  }

  private convertClientRawValueToClient(rawClient: ClientFormRawValue | NewClientFormRawValue): IClient | NewClient {
    return { ...rawClient, dateCreation: dayjs(rawClient.dateCreation, DATE_TIME_FORMAT) };
  }

  private convertClientToClientRawValue(
    client: IClient | (Partial<NewClient> & ClientFormDefaults),
  ): ClientFormRawValue | PartialWithRequiredKeyOf<NewClientFormRawValue> {
    return { ...client, dateCreation: client.dateCreation ? client.dateCreation.format(DATE_TIME_FORMAT) : undefined };
  }
}
