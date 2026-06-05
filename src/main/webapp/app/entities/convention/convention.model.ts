import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { StatutConvention } from 'app/entities/enumerations/statut-convention.model';
import { TypeConvention } from 'app/entities/enumerations/type-convention.model';
import { EtapeApprobation } from 'app/entities/enumerations/etape-approbation.model';

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
  client?: Pick<IClient, 'id' | 'nomClient'> | null;
  typeConvention?: keyof typeof TypeConvention | null;
  objet?: string | null;
  directionResponsable?: string | null;
  referenceJuridique?: string | null;
  numeroEngagement?: string | null;
  dateVisaControleur?: dayjs.Dayjs | null;
  valeurTotale?: number | null;
  renouvelable?: boolean | null;
  nombreRenouvellements?: number | null;
  conditionsResiliation?: string | null;
  penalites?: string | null;
  etapeApprobation?: keyof typeof EtapeApprobation | null;
  commentaireRejet?: string | null;
}

export type NewConvention = Omit<IConvention, 'id'> & { id: null };
