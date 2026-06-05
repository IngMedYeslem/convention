export const EtapeApprobation = {
  REDACTION: 'REDACTION',
  REVUE_JURIDIQUE: 'REVUE_JURIDIQUE',
  VISA_FINANCIER: 'VISA_FINANCIER',
  SIGNATURE_DIRECTION: 'SIGNATURE_DIRECTION',
  PUBLIE: 'PUBLIE',
  REJETE: 'REJETE',
} as const;

export type EtapeApprobation = (typeof EtapeApprobation)[keyof typeof EtapeApprobation];
