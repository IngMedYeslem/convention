import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

import SharedModule from 'app/shared/shared.module';
import { IClient } from 'app/entities/client/client.model';
import { IConvention } from 'app/entities/convention/convention.model';
import { IFacture } from 'app/entities/facture/facture.model';
import { IDetailConvention } from 'app/entities/detail-convention/detail-convention.model';

interface IPayment {
  id: number;
  montant?: number | null;
  facture?: { id?: number; client?: { id?: number } | null } | null;
}

export interface LigneEtat {
  client: IClient;
  nbConventions: number;
  nbFactures: number;
  montantDu: number;
  totalFacture: number;
  ecart: number;
  totalPaye: number;
  totalImpaye: number;
}

function nbPeriodes(conv: IConvention): number {
  if (!conv.dateDebutConv || !conv.redevance) return 0;
  const start = new Date(conv.dateDebutConv as unknown as string);
  const today = new Date();
  if (conv.periodeEcheance === 'ANNUEL') {
    return Math.max(0, today.getFullYear() - start.getFullYear());
  }
  // MENSUEL (default)
  return Math.max(0, (today.getFullYear() - start.getFullYear()) * 12 + (today.getMonth() - start.getMonth()));
}

@Component({
  selector: 'jhi-rapport-etat-facturation',
  templateUrl: './rapport-etat-facturation.component.html',
  imports: [SharedModule],
})
export class RapportEtatFacturationComponent implements OnInit {
  lignes: LigneEtat[] = [];
  loading = false;
  today = new Date().toLocaleDateString('fr-FR');

  protected http = inject(HttpClient);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    forkJoin({
      clients: this.http.get<IClient[]>('/api/clients?size=200&sort=nomClient,asc'),
      conventions: this.http.get<IConvention[]>('/api/conventions?size=500'),
      factures: this.http.get<IFacture[]>('/api/factures?size=1000'),
      details: this.http.get<IDetailConvention[]>('/api/detail-conventions?size=2000'),
      payments: this.http.get<IPayment[]>('/api/payments?size=5000'),
    }).subscribe({
      next: ({ clients, conventions, factures, details, payments }) => {
        this.lignes = clients
          .map(client => {
            const convsCli = conventions.filter(c => c.client?.id === client.id);
            const factsCli = factures.filter(f => f.client?.id === client.id);

            const montantDu = convsCli.reduce((sum, conv) => {
              const redevance = Number(conv.redevance ?? 0);
              const periodes = nbPeriodes(conv);
              const detailsConv = details.filter(d => d.convention?.id === conv.id);
              const autresMontants = detailsConv.reduce((s, d) => s + Number(d.montantTotal ?? 0), 0);
              return sum + redevance * periodes + autresMontants;
            }, 0);

            const totalFacture = factsCli.reduce((s, f) => s + Number(f.montantTotal ?? 0), 0);
            const factureIds = new Set(factsCli.map(f => f.id));
            const totalPaye = payments
              .filter(p => p.facture?.id != null && factureIds.has(p.facture.id as number))
              .reduce((s, p) => s + Number(p.montant ?? 0), 0);

            return {
              client,
              nbConventions: convsCli.length,
              nbFactures: factsCli.length,
              montantDu,
              totalFacture,
              ecart: montantDu - totalFacture,
              totalPaye,
              totalImpaye: totalFacture - totalPaye,
            };
          })
          .filter(l => l.nbConventions > 0 || l.nbFactures > 0);
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  get grandMontantDu(): number {
    return this.lignes.reduce((s, l) => s + l.montantDu, 0);
  }
  get grandTotalFacture(): number {
    return this.lignes.reduce((s, l) => s + l.totalFacture, 0);
  }
  get grandEcart(): number {
    return this.lignes.reduce((s, l) => s + l.ecart, 0);
  }
  get grandTotalPaye(): number {
    return this.lignes.reduce((s, l) => s + l.totalPaye, 0);
  }
  get grandTotalImpaye(): number {
    return this.lignes.reduce((s, l) => s + l.totalImpaye, 0);
  }
  get grandNbFactures(): number {
    return this.lignes.reduce((s, l) => s + l.nbFactures, 0);
  }

  print(): void {
    window.print();
  }
}
