import dayjs from 'dayjs/esm';

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
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
