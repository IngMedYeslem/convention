import dayjs from 'dayjs/esm';

import { IDetailConvention, NewDetailConvention } from './detail-convention.model';

export const sampleWithRequiredData: IDetailConvention = {
  id: 23029,
  designation: "d'après lâcher",
  prixUnitaire: 14242.67,
  quantite: 9626,
};

export const sampleWithPartialData: IDetailConvention = {
  id: 19751,
  designation: 'au-dessus de',
  prixUnitaire: 12738.1,
  quantite: 17975,
  observations: '../fake-data/blob/hipster.txt',
  dateCreation: dayjs('2025-11-18'),
};

export const sampleWithFullData: IDetailConvention = {
  id: 22046,
  designation: 'innombrable propre parce que',
  prixUnitaire: 22419.54,
  quantite: 1135,
  montantTotal: 12653.41,
  observations: '../fake-data/blob/hipster.txt',
  dateCreation: dayjs('2025-11-18'),
};

export const sampleWithNewData: NewDetailConvention = {
  designation: 'tsoin-tsoin',
  prixUnitaire: 16146.21,
  quantite: 22985,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
