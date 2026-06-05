import dayjs from 'dayjs/esm';
import { TypeInstitution } from 'app/entities/enumerations/type-institution.model';

export interface IClient {
  id: number;
  numClient?: number | null;
  nomClient?: string | null;
  adresseClient?: string | null;
  emailClient?: string | null;
  whatsClient?: string | null;
  obsClient?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  actif?: boolean | null;
  typeInstitution?: keyof typeof TypeInstitution | null;
  nif?: string | null;
  rc?: string | null;
  nomRepresentant?: string | null;
  fonctionRepresentant?: string | null;
  qualiteSignature?: string | null;
  wilaya?: string | null;
  commune?: string | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
