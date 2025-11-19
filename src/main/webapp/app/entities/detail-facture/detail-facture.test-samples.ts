import { IDetailFacture, NewDetailFacture } from './detail-facture.model';

export const sampleWithRequiredData: IDetailFacture = {
  id: 17947,
  designation: 'auparavant athlète triathlète',
  prixUnitaire: 27665.54,
  quantite: 8472,
};

export const sampleWithPartialData: IDetailFacture = {
  id: 2790,
  designation: 'quasi',
  prixUnitaire: 22865.9,
  quantite: 25885,
  montantHT: 5545.17,
  tauxTVA: 64.86,
  montantTTC: 7216.91,
};

export const sampleWithFullData: IDetailFacture = {
  id: 27137,
  designation: 'oh résigner',
  prixUnitaire: 5491.41,
  quantite: 32754,
  montantHT: 13037.33,
  tauxTVA: 67.7,
  montantTVA: 7462.18,
  montantTTC: 19424.17,
  observations: 'avex',
};

export const sampleWithNewData: NewDetailFacture = {
  designation: 'aussitôt que incarner toc-toc',
  prixUnitaire: 26415.31,
  quantite: 4778,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
