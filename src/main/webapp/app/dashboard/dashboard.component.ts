import { AfterViewInit, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import Chart from 'chart.js/auto';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule, CurrencyPipe, RouterModule, FontAwesomeModule],
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  readonly account = inject(AccountService).trackCurrentAccount();

  statistics: any = {};
  conventionsAlerte: any[] = [];
  conventionsPendingApproval: any[] = [];
  chartsReady = false;
  today = new Date();

  private convChart?: Chart;
  private caChart?: Chart;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
    this.loadConventionsAlerte();
    this.loadConventionsPendingApproval();
  }

  ngAfterViewInit(): void {
    this.chartsReady = true;
    if (Object.keys(this.statistics).length > 0) {
      this.buildCharts();
    }
  }

  ngOnDestroy(): void {
    this.convChart?.destroy();
    this.caChart?.destroy();
  }

  loadStatistics(): void {
    this.http.get('/api/statistiques/dashboard').subscribe((data: any) => {
      this.statistics = data;
      if (this.chartsReady) this.buildCharts();
    });
  }

  loadConventionsAlerte(): void {
    this.http.get<any[]>('/api/conventions?statut.equals=SUSPENDUE&size=10').subscribe({
      next: data => (this.conventionsAlerte = data ?? []),
      error: () => (this.conventionsAlerte = []),
    });
  }

  loadConventionsPendingApproval(): void {
    const niveau = this.account()?.niveauHierarchique;
    let statut: string | null = null;
    if (niveau === 'DEPARTEMENT') statut = 'SOUMIS';
    else if (niveau === 'DIRECTION') statut = 'APPROUVE_DEPT';
    if (!statut) return;

    this.http.get<any[]>(`/api/conventions?statut.equals=${statut}&size=50`).subscribe({
      next: data => (this.conventionsPendingApproval = data ?? []),
      error: () => (this.conventionsPendingApproval = []),
    });
  }

  // ─── Actions workflow ────────────────────────────────────────────────────────

  approuverDept(id: number): void {
    this.http.put(`/api/convention-workflow/${id}/approuver-dept`, {}).subscribe({
      next: () => {
        this.loadConventionsPendingApproval();
        this.loadStatistics();
      },
    });
  }

  approuverDirection(id: number): void {
    this.http.put(`/api/convention-workflow/${id}/approuver-direction`, {}).subscribe({
      next: () => {
        this.loadConventionsPendingApproval();
        this.loadStatistics();
      },
    });
  }

  rejeter(id: number): void {
    const commentaire = prompt('Motif du rejet (optionnel) :') ?? '';
    this.http.put(`/api/convention-workflow/${id}/rejeter?commentaire=${encodeURIComponent(commentaire)}`, {}).subscribe({
      next: () => {
        this.loadConventionsPendingApproval();
        this.loadStatistics();
      },
    });
  }

  // ─── Autres actions ──────────────────────────────────────────────────────────

  generateAllInvoices(): void {
    this.http.post('/api/facture-generation/active-conventions', {}).subscribe({
      next: (response: any) => {
        const count = Array.isArray(response) ? response.length : 'Toutes les';
        this.showToast(`${count} facture(s) générée(s) avec succès !`, 'success');
        this.loadStatistics();
      },
      error: () => this.showToast('Erreur lors de la génération des factures', 'danger'),
    });
  }

  periodeLabel(periode: string): string {
    if (periode === 'MENSUEL') return 'Mensuel';
    if (periode === 'ANNUEL') return 'Annuel';
    return '—';
  }

  private buildCharts(): void {
    this.buildConvChart();
    this.buildCaChart();
  }

  private buildConvChart(): void {
    const canvas = document.getElementById('miniConvChart');
    if (!(canvas instanceof HTMLCanvasElement) || !this.statistics.conventionsParMois) return;
    this.convChart?.destroy();
    this.convChart = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: Object.keys(this.statistics.conventionsParMois),
        datasets: [
          {
            label: 'Conventions',
            data: Object.values(this.statistics.conventionsParMois).map(v => Number(v)),
            backgroundColor: 'rgba(54, 162, 235, 0.7)',
            borderRadius: 4,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } },
      },
    });
  }

  private buildCaChart(): void {
    const canvas = document.getElementById('miniCaChart');
    if (!(canvas instanceof HTMLCanvasElement) || !this.statistics.chiffresAffairesParMois) return;
    this.caChart?.destroy();
    this.caChart = new Chart(canvas, {
      type: 'line',
      data: {
        labels: Object.keys(this.statistics.chiffresAffairesParMois),
        datasets: [
          {
            label: 'CA (MRU)',
            data: Object.values(this.statistics.chiffresAffairesParMois).map(v => Number(v)),
            borderColor: 'rgba(40, 167, 69, 1)',
            backgroundColor: 'rgba(40, 167, 69, 0.12)',
            fill: true,
            tension: 0.4,
            pointRadius: 3,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { y: { beginAtZero: true } },
      },
    });
  }

  private showToast(message: string, type: 'success' | 'danger'): void {
    const toast = document.createElement('div');
    toast.className = 'toast show position-fixed';
    toast.style.cssText = 'top:20px;right:20px;z-index:9999;min-width:300px';
    toast.innerHTML = `
      <div class="toast-header bg-${type} text-white">
        <strong class="me-auto">${type === 'success' ? '✓ Succès' : '✗ Erreur'}</strong>
        <button type="button" class="btn-close btn-close-white" onclick="this.closest('.toast').remove()"></button>
      </div>
      <div class="toast-body">${message}</div>`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
  }
}
