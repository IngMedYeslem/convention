import dayjs from 'dayjs/esm';
import { IConvention } from 'app/entities/convention/convention.model';
import { TypeAvenant } from 'app/entities/enumerations/type-avenant.model';
import { StatutAvenant } from 'app/entities/enumerations/statut-avenant.model';

export interface IAvenant {
  id: number;
  numeroAvenant?: number | null;
  dateSignature?: dayjs.Dayjs | null;
  typeAvenant?: keyof typeof TypeAvenant | null;
  objet?: string | null;
  nouvelleEcheance?: dayjs.Dayjs | null;
  montantAdditionnel?: number | null;
  modificationsClauses?: string | null;
  statut?: keyof typeof StatutAvenant | null;
  dateCreation?: dayjs.Dayjs | null;
  convention?: Pick<IConvention, 'id' | 'numConvention'> | null;
}

export type NewAvenant = Omit<IAvenant, 'id'> & { id: null };
