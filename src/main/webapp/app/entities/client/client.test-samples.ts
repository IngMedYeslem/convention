import dayjs from 'dayjs/esm';

import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 16289,
  numClient: 28235,
  nomClient: 'afin de',
  actif: true,
};

export const sampleWithPartialData: IClient = {
  id: 12862,
  numClient: 27864,
  nomClient: 'personnel professionnel selon',
  adresseClient: 'concernant',
  emailClient: 'URTE@EG.ywqlO.pr',
  whatsClient: 'commissionnaire alor',
  obsClient: '../fake-data/blob/hipster.txt',
  dateCreation: dayjs('2025-11-17T20:16'),
  actif: true,
};

export const sampleWithFullData: IClient = {
  id: 31496,
  numClient: 32616,
  nomClient: 'au-dessus',
  adresseClient: 'cependant timide pschitt',
  emailClient: 'N8@i_J0y1.sdLL8H.ZP43',
  whatsClient: 'mettre',
  obsClient: '../fake-data/blob/hipster.txt',
  dateCreation: dayjs('2025-11-18T10:50'),
  actif: true,
};

export const sampleWithNewData: NewClient = {
  numClient: 14393,
  nomClient: "bien que aujourd'hui infime",
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
