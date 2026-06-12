import { Component, OnInit, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IClient } from 'app/entities/client/client.model';
import { IFacture } from 'app/entities/facture/facture.model';
import { IDetailFacture } from 'app/entities/detail-facture/detail-facture.model';

interface IPayment {
  id: number;
  numPaiement?: string | null;
  datePaiement?: string | null;
  montant?: number | null;
  modePaiement?: string | null;
  reference?: string | null;
  facture?: { id?: number } | null;
}

export interface FactureAvecDetails {
  facture: IFacture;
  lignes: IDetailFacture[];
  paiements: IPayment[];
}

@Component({
  selector: 'jhi-rapport-factures-client',
  templateUrl: './rapport-factures-client.component.html',
  imports: [SharedModule, FormsModule, FormatMediumDatePipe],
})
export class RapportFacturesClientComponent implements OnInit {
  clients: IClient[] = [];
  selectedClientId: number | null = null;
  items: FactureAvecDetails[] = [];
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
      .get<IFacture[]>(`/api/factures?clientId.equals=${this.selectedClientId}&size=500&sort=dateFacture,asc`)
      .pipe(
        switchMap(factures => {
          if (factures.length === 0) return of([] as FactureAvecDetails[]);
          return forkJoin(
            factures.map(facture =>
              forkJoin({
                lignes: this.http.get<IDetailFacture[]>(`/api/detail-factures/by-facture/${facture.id}`),
                paiements: this.http.get<IPayment[]>(`/api/payments/by-facture/${facture.id}`),
              }).pipe(switchMap(({ lignes, paiements }) => of({ facture, lignes, paiements } as FactureAvecDetails))),
            ),
          );
        }),
      )
      .subscribe({
        next: result => {
          this.items = result as FactureAvecDetails[];
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  montantPaye(item: FactureAvecDetails): number {
    return item.paiements.reduce((s, p) => s + Number(p.montant ?? 0), 0);
  }

  montantAPayer(item: FactureAvecDetails): number {
    return Number(item.facture.montantTotal ?? 0) - this.montantPaye(item);
  }

  get factures(): IFacture[] {
    return this.items.map(i => i.facture);
  }
  get totalFacture(): number {
    return this.items.reduce((s, i) => s + Number(i.facture.montantTotal ?? 0), 0);
  }
  get totalTTC(): number {
    return this.items.reduce((s, i) => s + Number(i.facture.montantTTC ?? 0), 0);
  }
  get totalPaye(): number {
    return this.items.reduce((s, i) => s + this.montantPaye(i), 0);
  }
  get totalImpaye(): number {
    return this.totalFacture - this.totalPaye;
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
