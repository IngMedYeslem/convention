import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IClient } from 'app/entities/client/client.model';
import { IConvention } from 'app/entities/convention/convention.model';
import { IFacture } from 'app/entities/facture/facture.model';

interface IPayment {
  id: number;
  montant?: number | null;
  datePaiement?: string | null;
  modePaiement?: string | null;
  numPaiement?: string | null;
  facture?: { id?: number } | null;
}

export interface FactureAvecPaiements {
  facture: IFacture;
  paiements: IPayment[];
}

export interface ConventionAvecFactures {
  convention: IConvention;
  factures: FactureAvecPaiements[];
}

@Component({
  selector: 'jhi-rapport-conventions-client',
  templateUrl: './rapport-conventions-client.component.html',
  imports: [SharedModule, FormsModule, FormatMediumDatePipe],
})
export class RapportConventionsClientComponent implements OnInit {
  clients: IClient[] = [];
  selectedClientId: number | null = null;
  data: ConventionAvecFactures[] = [];
  loading = false;
  today = new Date().toLocaleDateString('fr-FR');

  protected http = inject(HttpClient);

  ngOnInit(): void {
    this.http.get<IClient[]>('/api/clients?size=200&sort=nomClient,asc').subscribe(c => (this.clients = c));
  }

  load(): void {
    if (!this.selectedClientId) return;
    this.loading = true;
    this.http
      .get<IConvention[]>(`/api/conventions?clientId.equals=${this.selectedClientId}&size=200&sort=numConvention,asc`)
      .pipe(
        switchMap(conventions => {
          if (conventions.length === 0) return of([] as ConventionAvecFactures[]);
          return forkJoin(
            conventions.map(conv =>
              this.http.get<IFacture[]>(`/api/factures?conventionId.equals=${conv.id}&size=500&sort=dateFacture,asc`).pipe(
                switchMap(factures => {
                  if (factures.length === 0) return of({ convention: conv, factures: [] } as ConventionAvecFactures);
                  return forkJoin(
                    factures.map(facture =>
                      this.http
                        .get<IPayment[]>(`/api/payments/by-facture/${facture.id}`)
                        .pipe(switchMap(paiements => of({ facture, paiements } as FactureAvecPaiements))),
                    ),
                  ).pipe(switchMap(items => of({ convention: conv, factures: items } as ConventionAvecFactures)));
                }),
              ),
            ),
          );
        }),
      )
      .subscribe({
        next: result => {
          this.data = result as ConventionAvecFactures[];
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  montantPayeFacture(fp: FactureAvecPaiements): number {
    return fp.paiements.reduce((s, p) => s + Number(p.montant ?? 0), 0);
  }

  montantAPayerFacture(fp: FactureAvecPaiements): number {
    return Number(fp.facture.montantTotal ?? 0) - this.montantPayeFacture(fp);
  }

  totalFactures(item: ConventionAvecFactures): number {
    return item.factures.reduce((s, fp) => s + Number(fp.facture.montantTotal ?? 0), 0);
  }

  totalPaye(item: ConventionAvecFactures): number {
    return item.factures.reduce((s, fp) => s + this.montantPayeFacture(fp), 0);
  }

  totalImpaye(item: ConventionAvecFactures): number {
    return this.totalFactures(item) - this.totalPaye(item);
  }

  get grandTotalFacture(): number {
    return this.data.reduce((s, d) => s + this.totalFactures(d), 0);
  }
  get grandTotalPaye(): number {
    return this.data.reduce((s, d) => s + this.totalPaye(d), 0);
  }
  get grandTotalImpaye(): number {
    return this.data.reduce((s, d) => s + this.totalImpaye(d), 0);
  }

  get selectedClient(): IClient | undefined {
    return this.clients.find(c => c.id === this.selectedClientId);
  }

  statutBadge(statut: string | null | undefined): string {
    switch (statut) {
      case 'PAYEE':
        return 'bg-success';
      case 'EMISE':
        return 'bg-info text-dark';
      case 'IMPAYEE':
        return 'bg-danger';
      case 'ANNULEE':
        return 'bg-secondary';
      case 'BROUILLON':
        return 'bg-warning text-dark';
      default:
        return 'bg-light text-dark border';
    }
  }

  print(): void {
    window.print();
  }
}
