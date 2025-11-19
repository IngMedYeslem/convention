import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { IConvention } from 'app/entities/convention/convention.model';
import { TypeFacture } from 'app/entities/enumerations/type-facture.model';
import { StatutFacture } from 'app/entities/enumerations/statut-facture.model';

export interface IFacture {
  id: number;
  numFacture?: number | null;
  dateFacture?: dayjs.Dayjs | null;
  montantTotal?: number | null;
  montantTTC?: number | null;
  tva?: number | null;
  observations?: string | null;
  ancienneRef?: string | null;
  typeFacture?: keyof typeof TypeFacture | null;
  statut?: keyof typeof StatutFacture | null;
  dateEcheance?: dayjs.Dayjs | null;
  dateCreation?: dayjs.Dayjs | null;
  client?: Pick<IClient, 'id'> | null;
  convention?: Pick<IConvention, 'id'> | null;
}

export type NewFacture = Omit<IFacture, 'id'> & { id: null };
