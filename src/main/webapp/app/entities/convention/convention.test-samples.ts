import dayjs from 'dayjs/esm';

import { IConvention, NewConvention } from './convention.model';

export const sampleWithRequiredData: IConvention = {
  id: 12057,
  numConvention: 11607,
  dateSignConv: dayjs('2025-11-18'),
  dateDebutConv: dayjs('2025-11-18'),
  echeanceConv: dayjs('2025-11-18'),
  redevance: 31695.7,
  nomResponsable: 'pendant que sans vide',
  statut: 'ACTIVE',
};

export const sampleWithPartialData: IConvention = {
  id: 10821,
  numConvention: 22221,
  dateSignConv: dayjs('2025-11-17'),
  dateDebutConv: dayjs('2025-11-18'),
  echeanceConv: dayjs('2025-11-18'),
  redevance: 14718.15,
  nomResponsable: 'badaboum comme',
  statut: 'SUSPENDUE',
  dateModification: dayjs('2025-11-18T17:35'),
};

export const sampleWithFullData: IConvention = {
  id: 8499,
  numConvention: 16938,
  dateSignConv: dayjs('2025-11-18'),
  dateDebutConv: dayjs('2025-11-18'),
  echeanceConv: dayjs('2025-11-18'),
  redevance: 872.61,
  nomResponsable: 'groin groin commis plouf',
  statut: 'ANNULEE',
  dateCreation: dayjs('2025-11-18T04:58'),
  dateModification: dayjs('2025-11-18T11:49'),
};

export const sampleWithNewData: NewConvention = {
  numConvention: 23776,
  dateSignConv: dayjs('2025-11-18'),
  dateDebutConv: dayjs('2025-11-18'),
  echeanceConv: dayjs('2025-11-18'),
  redevance: 24246.81,
  nomResponsable: "aujourd'hui téméraire",
  statut: 'SUSPENDUE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
