import { Component, OnInit, inject, input, effect } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IConvention } from '../convention.model';
import { IFacture } from 'app/entities/facture/facture.model';
import { IDetailConvention } from 'app/entities/detail-convention/detail-convention.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-convention-detail',
  templateUrl: './convention-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class ConventionDetailComponent implements OnInit {
  convention = input<IConvention | null>(null);

  factures: IFacture[] = [];
  detailConventions: IDetailConvention[] = [];

  protected http = inject(HttpClient);
  private accountService = inject(AccountService);
  readonly currentAccount = inject(AccountService).trackCurrentAccount();

  get canEdit(): boolean {
    const account = this.currentAccount();
    if (!account) return false;
    if (account.authorities.includes('ROLE_ADMIN')) return true;
    const conv = this.convention();
    if (!conv) return false;
    // SERVICE: can only edit conventions created by their own unit
    if (account.niveauHierarchique === 'SERVICE') {
      return conv.createdByUniteId === account.uniteOrgId;
    }
    // DEPARTEMENT and DIRECTION: read-only (approval workflow, not direct edit)
    return false;
  }

  constructor() {
    effect(() => {
      const conv = this.convention();
      if (conv?.id) {
        this.loadRelatedData(conv.id);
      }
    });
  }

  ngOnInit(): void {}

  get totalDetailConvention(): number {
    return this.detailConventions.reduce((s, d) => s + Number(d.montantTotal ?? 0), 0);
  }

  get totalFactures(): number {
    return this.factures.reduce((s, f) => s + Number(f.montantTotal ?? 0), 0);
  }

  get totalPaye(): number {
    return this.factures.filter(f => f.statut === 'PAYEE').reduce((s, f) => s + Number(f.montantTotal ?? 0), 0);
  }

  get totalImpaye(): number {
    return this.totalFactures - this.totalPaye;
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

  statutLabel(statut: string | null | undefined): string {
    switch (statut) {
      case 'PAYEE':
        return 'Payée';
      case 'EMISE':
        return 'Émise';
      case 'IMPAYEE':
        return 'Impayée';
      case 'ANNULEE':
        return 'Annulée';
      case 'BROUILLON':
        return 'Brouillon';
      default:
        return statut ?? '—';
    }
  }

  previousState(): void {
    window.history.back();
  }

  private loadRelatedData(conventionId: number): void {
    this.http
      .get<IDetailConvention[]>(`/api/detail-conventions/by-convention/${conventionId}`)
      .subscribe(details => (this.detailConventions = details));

    this.http
      .get<IFacture[]>(`/api/factures?conventionId.equals=${conventionId}&size=200&sort=dateFacture,asc`)
      .subscribe(factures => (this.factures = factures));
  }
}
