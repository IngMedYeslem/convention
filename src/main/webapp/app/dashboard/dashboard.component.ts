import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import Chart from 'chart.js/auto';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule, CurrencyPipe, RouterModule, FontAwesomeModule],
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  statistics: any = {};
  conventionsAlerte: any[] = [];
  chartsReady = false;
  today = new Date();

  private convChart?: Chart;
  private caChart?: Chart;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
    this.loadConventionsAlerte();
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
    this.http.get<any[]>('/api/conventions?statut=ACTIVE&size=100').subscribe({
      next: data => {
        const dans90j = new Date();
        dans90j.setDate(dans90j.getDate() + 90);
        this.conventionsAlerte = (data ?? [])
          .filter((c: any) => c.echeanceConv && new Date(c.echeanceConv) <= dans90j)
          .sort((a: any, b: any) => new Date(a.echeanceConv).getTime() - new Date(b.echeanceConv).getTime())
          .slice(0, 10);
      },
      error: () => {
        this.conventionsAlerte = [];
      },
    });
  }

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

  alertClass(echeance: string): string {
    const j = Math.ceil((new Date(echeance).getTime() - Date.now()) / 86400000);
    if (j < 0) return 'table-danger';
    if (j <= 30) return 'table-warning';
    return 'table-info';
  }

  joursLabel(echeance: string): string {
    const j = Math.ceil((new Date(echeance).getTime() - Date.now()) / 86400000);
    if (j < 0) return `Expirée (${Math.abs(j)} j)`;
    if (j === 0) return "Expire aujourd'hui";
    return `${j} j restant${j > 1 ? 's' : ''}`;
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
