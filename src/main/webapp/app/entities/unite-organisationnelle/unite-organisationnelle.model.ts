import { NiveauHierarchique } from 'app/entities/enumerations/niveau-hierarchique.model';

export interface IUniteOrganisationnelle {
  id: number;
  nom?: string | null;
  code?: string | null;
  niveau?: keyof typeof NiveauHierarchique | null;
  parentId?: number | null;
  parentNom?: string | null;
}

export type NewUniteOrganisationnelle = Omit<IUniteOrganisationnelle, 'id'> & { id: null };
