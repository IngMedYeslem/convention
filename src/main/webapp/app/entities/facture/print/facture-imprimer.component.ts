import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { DecimalPipe } from '@angular/common';

import { IFacture } from '../facture.model';
import { IClient } from 'app/entities/client/client.model';
import { IConvention } from 'app/entities/convention/convention.model';
import { IDetailFacture } from 'app/entities/detail-facture/detail-facture.model';

// ── Conversion montant en lettres (français) ──────────────────────────────────

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
  const centStr = h === 1 ? 'cent' : ONES[h] + ' cent' + (r === 0 ? 's' : '');
  return r === 0 ? centStr : centStr + ' ' + belowHundred(r);
}

function intToFrench(n: number): string {
  if (n === 0) return 'zéro';
  let result = '';
  if (n >= 1_000_000) {
    const m = Math.floor(n / 1_000_000);
    result += (m === 1 ? 'un million' : belowThousand(m) + ' millions') + ' ';
    n %= 1_000_000;
  }
  if (n >= 1000) {
    const k = Math.floor(n / 1000);
    result += (k === 1 ? 'mille' : belowThousand(k) + ' mille') + ' ';
    n %= 1000;
  }
  if (n > 0) result += belowThousand(n);
  return result.trim();
}

function montantEnLettres(amount: number): string {
  if (isNaN(amount) || amount <= 0) return '—';
  const intPart = Math.floor(amount);
  const decPart = Math.round((amount - intPart) * 100);
  let result = intToFrench(intPart);
  result += intPart > 1 ? ' ouguiyas' : ' ouguiya';
  if (decPart > 0) {
    result += ' et ' + intToFrench(decPart) + (decPart > 1 ? ' centimes' : ' centime');
  }
  return result.charAt(0).toUpperCase() + result.slice(1);
}

// ─────────────────────────────────────────────────────────────────────────────

@Component({
  selector: 'jhi-facture-imprimer',
  templateUrl: './facture-imprimer.component.html',
  imports: [DecimalPipe],
})
export class FactureImprimerComponent implements OnInit {
  facture: IFacture | null = null;
  client: IClient | null = null;
  convention: IConvention | null = null;
  lignes: IDetailFacture[] = [];
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
          const calls: Record<string, any> = {
            lignes: this.http.get<IDetailFacture[]>(`/api/detail-factures/by-facture/${id}`),
          };
          if (facture.client?.id) {
            calls['client'] = this.http.get<IClient>(`/api/clients/${facture.client.id}`);
          }
          if (facture.convention?.id) {
            calls['convention'] = this.http.get<IConvention>(`/api/conventions/${facture.convention.id}`);
          }
          return forkJoin(calls);
        }),
      )
      .subscribe({
        next: (data: any) => {
          this.lignes = data['lignes'] ?? [];
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

  get totalHT(): number {
    return this.lignes.reduce((s, l) => s + Number(l.montantHT ?? 0), 0);
  }
  get totalTVA(): number {
    return this.lignes.reduce((s, l) => s + Number(l.montantTVA ?? 0), 0);
  }
  get totalTTC(): number {
    return this.lignes.reduce((s, l) => s + Number(l.montantTTC ?? 0), 0);
  }

  get montantNetEnLettres(): string {
    const montant = this.totalTTC > 0 ? this.totalTTC : Number(this.facture?.montantTTC ?? this.facture?.montantTotal ?? 0);
    return montantEnLettres(montant);
  }

  fmtDate(d: any): string {
    if (!d) return '—';
    return typeof d.format === 'function' ? d.format('DD/MM/YYYY') : new Date(d).toLocaleDateString('fr-FR');
  }

  goBack(): void {
    window.history.back();
  }
}
