import dayjs from 'dayjs/esm';
import { IConvention } from 'app/entities/convention/convention.model';

export interface IDetailConvention {
  id: number;
  designation?: string | null;
  prixUnitaire?: number | null;
  quantite?: number | null;
  montantTotal?: number | null;
  observations?: string | null;
  dateCreation?: dayjs.Dayjs | null;
  convention?: Pick<IConvention, 'id'> | null;
}

export type NewDetailConvention = Omit<IDetailConvention, 'id'> & { id: null };
