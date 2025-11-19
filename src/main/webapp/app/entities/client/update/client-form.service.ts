import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IClient | NewClient> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

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
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = this.convertClientToClientRawValue({
      ...this.getFormDefaults(),
      ...client,
    });
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numClient: new FormControl(clientRawValue.numClient, {
        validators: [Validators.required],
      }),
      nomClient: new FormControl(clientRawValue.nomClient, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      adresseClient: new FormControl(clientRawValue.adresseClient, {
        validators: [Validators.maxLength(255)],
      }),
      emailClient: new FormControl(clientRawValue.emailClient, {
        validators: [Validators.pattern('^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$')],
      }),
      whatsClient: new FormControl(clientRawValue.whatsClient, {
        validators: [Validators.maxLength(20)],
      }),
      obsClient: new FormControl(clientRawValue.obsClient),
      dateCreation: new FormControl(clientRawValue.dateCreation),
      actif: new FormControl(clientRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return this.convertClientRawValueToClient(form.getRawValue() as ClientFormRawValue | NewClientFormRawValue);
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateCreation: currentTime,
      actif: false,
    };
  }

  private convertClientRawValueToClient(rawClient: ClientFormRawValue | NewClientFormRawValue): IClient | NewClient {
    return {
      ...rawClient,
      dateCreation: dayjs(rawClient.dateCreation, DATE_TIME_FORMAT),
    };
  }

  private convertClientToClientRawValue(
    client: IClient | (Partial<NewClient> & ClientFormDefaults),
  ): ClientFormRawValue | PartialWithRequiredKeyOf<NewClientFormRawValue> {
    return {
      ...client,
      dateCreation: client.dateCreation ? client.dateCreation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
