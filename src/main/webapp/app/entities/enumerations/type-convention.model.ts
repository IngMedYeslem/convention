export const TypeConvention = {
  COOPERATION: 'COOPERATION',
  PARTENARIAT: 'PARTENARIAT',
  PRESTATION_SERVICE: 'PRESTATION_SERVICE',
  FINANCEMENT: 'FINANCEMENT',
  ECHANGE: 'ECHANGE',
  CADRE: 'CADRE',
  AUTRE: 'AUTRE',
} as const;

export type TypeConvention = (typeof TypeConvention)[keyof typeof TypeConvention];
