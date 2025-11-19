import dayjs from 'dayjs/esm';

import { IFacture, NewFacture } from './facture.model';

export const sampleWithRequiredData: IFacture = {
  id: 28182,
  numFacture: 1798,
  dateFacture: dayjs('2025-11-18'),
  montantTotal: 30771.42,
  typeFacture: 'AVOIR',
  statut: 'IMPAYEE',
};

export const sampleWithPartialData: IFacture = {
  id: 30762,
  numFacture: 22745,
  dateFacture: dayjs('2025-11-18'),
  montantTotal: 6260.58,
  tva: 52.71,
  observations: '../fake-data/blob/hipster.txt',
  typeFacture: 'ACOMPTE',
  statut: 'ANNULEE',
  dateEcheance: dayjs('2025-11-18'),
  dateCreation: dayjs('2025-11-17T21:16'),
};

export const sampleWithFullData: IFacture = {
  id: 4844,
  numFacture: 29719,
  dateFacture: dayjs('2025-11-18'),
  montantTotal: 7167.2,
  montantTTC: 5060.22,
  tva: 38.62,
  observations: '../fake-data/blob/hipster.txt',
  ancienneRef: 'apparemment hésiter',
  typeFacture: 'ACOMPTE',
  statut: 'EMISE',
  dateEcheance: dayjs('2025-11-18'),
  dateCreation: dayjs('2025-11-17T20:07'),
};

export const sampleWithNewData: NewFacture = {
  numFacture: 30106,
  dateFacture: dayjs('2025-11-18'),
  montantTotal: 8954.3,
  typeFacture: 'AVOIR',
  statut: 'BROUILLON',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
