import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { DecimalPipe } from '@angular/common';

import { IFacture } from '../facture.model';
import { IClient } from 'app/entities/client/client.model';
import { IConvention } from 'app/entities/convention/convention.model';

// ── Montant en lettres (français) ─────────────────────────────────────────────
const ONES = [
  '',
  'un',
  'deux',
  'trois',
  'quatre',
  'cinq',
  'six',
  'sept',
  'huit',
  'neuf',
  'dix',
  'onze',
  'douze',
  'treize',
  'quatorze',
  'quinze',
  'seize',
  'dix-sept',
  'dix-huit',
  'dix-neuf',
];
function belowHundred(n: number): string {
  if (n < 20) return ONES[n];
  const t = Math.floor(n / 10);
  const u = n % 10;
  if (t === 7) return u === 1 ? 'soixante-et-onze' : 'soixante-' + ONES[10 + u];
  if (t === 8) return u === 0 ? 'quatre-vingts' : 'quatre-vingt-' + ONES[u];
  if (t === 9) return 'quatre-vingt-' + ONES[10 + u];
  const liaison = u === 1 ? '-et-un' : u === 0 ? '' : '-' + ONES[u];
  return ['', '', 'vingt', 'trente', 'quarante', 'cinquante', 'soixante'][t] + liaison;
}
function belowThousand(n: number): string {
  if (n < 100) return belowHundred(n);
  const h = Math.floor(n / 100);
  const r = n % 100;
  const c = h === 1 ? 'cent' : ONES[h] + ' cent' + (r === 0 ? 's' : '');
  return r === 0 ? c : c + ' ' + belowHundred(r);
}
function intToFrench(n: number): string {
  if (n === 0) return 'zéro';
  let r = '';
  if (n >= 1_000_000) {
    const m = Math.floor(n / 1_000_000);
    r += (m === 1 ? 'un million' : belowThousand(m) + ' millions') + ' ';
    n %= 1_000_000;
  }
  if (n >= 1000) {
    const k = Math.floor(n / 1000);
    r += (k === 1 ? 'mille' : belowThousand(k) + ' mille') + ' ';
    n %= 1000;
  }
  if (n > 0) r += belowThousand(n);
  return r.trim();
}
function montantEnLettres(amount: number): string {
  if (!amount || amount <= 0) return '—';
  const i = Math.floor(amount);
  const d = Math.round((amount - i) * 100);
  let r = intToFrench(i) + (i > 1 ? ' ouguiyas' : ' ouguiya');
  if (d > 0) r += ' et ' + intToFrench(d) + (d > 1 ? ' centimes' : ' centime');
  return r.charAt(0).toUpperCase() + r.slice(1);
}
// ─────────────────────────────────────────────────────────────────────────────

@Component({
  selector: 'jhi-facture-lettre-paiement',
  templateUrl: './facture-lettre-paiement.component.html',
  imports: [DecimalPipe],
})
export class FactureLettreComponent implements OnInit {
  facture: IFacture | null = null;
  client: IClient | null = null;
  convention: IConvention | null = null;
  loading = true;
  today = new Date();

  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;

    this.http
      .get<IFacture>(`/api/factures/${id}`)
      .pipe(
        switchMap(facture => {
          this.facture = facture;
          const calls: Record<string, any> = {};
          if (facture.client?.id) {
            calls['client'] = this.http.get<IClient>(`/api/clients/${facture.client.id}`);
          }
          if (facture.convention?.id) {
            calls['convention'] = this.http.get<IConvention>(`/api/conventions/${facture.convention.id}`);
          }
          return forkJoin(Object.keys(calls).length ? calls : { _: this.http.get('/api/account') });
        }),
      )
      .subscribe({
        next: (data: any) => {
          this.client = data['client'] ?? null;
          this.convention = data['convention'] ?? null;
          this.loading = false;
          setTimeout(() => window.print(), 400);
        },
        error: () => {
          this.loading = false;
        },
      });
  }

  get montant(): number {
    return Number(this.facture?.montantTTC ?? this.facture?.montantTotal ?? 0);
  }

  get montantLettres(): string {
    return montantEnLettres(this.montant);
  }

  fmtDate(d: any): string {
    if (!d) return '—';
    return typeof d.format === 'function' ? d.format('DD/MM/YYYY') : new Date(d).toLocaleDateString('fr-FR');
  }

  fmtDateLong(d: any): string {
    if (!d) return '—';
    const dt = typeof d.toDate === 'function' ? d.toDate() : new Date(d);
    return dt.toLocaleDateString('fr-FR', { day: '2-digit', month: 'long', year: 'numeric' });
  }

  goBack(): void {
    window.history.back();
  }
}
