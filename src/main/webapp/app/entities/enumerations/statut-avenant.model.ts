export const StatutAvenant = {
  BROUILLON: 'BROUILLON',
  EN_VALIDATION: 'EN_VALIDATION',
  SIGNE: 'SIGNE',
  ANNULE: 'ANNULE',
} as const;

export type StatutAvenant = (typeof StatutAvenant)[keyof typeof StatutAvenant];
