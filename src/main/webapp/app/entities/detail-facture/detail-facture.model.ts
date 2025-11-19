import { IFacture } from 'app/entities/facture/facture.model';

export interface IDetailFacture {
  id: number;
  designation?: string | null;
  prixUnitaire?: number | null;
  quantite?: number | null;
  montantHT?: number | null;
  tauxTVA?: number | null;
  montantTVA?: number | null;
  montantTTC?: number | null;
  observations?: string | null;
  facture?: Pick<IFacture, 'id'> | null;
}

export type NewDetailFacture = Omit<IDetailFacture, 'id'> & { id: null };
