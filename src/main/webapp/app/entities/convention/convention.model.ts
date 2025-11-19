import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { StatutConvention } from 'app/entities/enumerations/statut-convention.model';

export interface IConvention {
  id: number;
  numConvention?: number | null;
  dateSignConv?: dayjs.Dayjs | null;
  dateDebutConv?: dayjs.Dayjs | null;
  echeanceConv?: dayjs.Dayjs | null;
  redevance?: number | null;
  nomResponsable?: string | null;
  statut?: keyof typeof StatutConvention | null;
  dateCreation?: dayjs.Dayjs | null;
  dateModification?: dayjs.Dayjs | null;
  client?: Pick<IClient, 'id'> | null;
}

export type NewConvention = Omit<IConvention, 'id'> & { id: null };
