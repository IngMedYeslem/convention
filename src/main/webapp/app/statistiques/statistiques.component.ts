import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import Chart from 'chart.js/auto';
import * as XLSX from 'xlsx';

interface MonthlyEntry {
  mois: string;
  conventions: number;
  ca: number;
}

@Component({
  selector: 'jhi-statistiques',
  templateUrl: './statistiques.component.html',
  styleUrls: ['./statistiques.component.scss'],
  imports: [CommonModule, CurrencyPipe, RouterModule],
})
export class StatistiquesComponent implements OnInit, AfterViewInit, OnDestroy {
  statistics: any = {};
  monthlyData: MonthlyEntry[] = [];
  chartsReady = false;

  private conventionsChart?: Chart;
  private caChart?: Chart;
  private statutChart?: Chart;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  ngAfterViewInit(): void {
    this.chartsReady = true;
    if (Object.keys(this.statistics).length > 0) {
      this.createCharts();
    }
  }

  ngOnDestroy(): void {
    this.conventionsChart?.destroy();
    this.caChart?.destroy();
    this.statutChart?.destroy();
  }

  loadStatistics(): void {
    this.http.get('/api/statistiques/dashboard').subscribe((data: any) => {
      this.statistics = data;
      this.buildMonthlyData();
      if (this.chartsReady) {
        this.createCharts();
      }
    });
  }

  exportExcel(): void {
    const data = [
      ['Indicateur', 'Valeur'],
      ['Total conventions', this.statistics.totalConventions ?? 0],
      ['Conventions actives', this.statistics.conventionsActives ?? 0],
      ['Conventions suspendues', this.statistics.conventionsSuspendues ?? 0],
      ["Chiffre d'affaires (MRU)", this.statistics.chiffresAffaires ?? 0],
      ['Montant impayé (MRU)', this.statistics.montantImpaye ?? 0],
      ['Total factures', this.statistics.totalFactures ?? 0],
      [],
      ['Mois', 'Conventions signées', 'CA (MRU)'],
      ...this.monthlyData.map(e => [e.mois, e.conventions, e.ca]),
    ];
    const ws = XLSX.utils.aoa_to_sheet(data);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Statistiques');
    XLSX.writeFile(wb, `statistiques_${new Date().toISOString().slice(0, 10)}.xlsx`);
  }

  private buildMonthlyData(): void {
    const conv = this.statistics.conventionsParMois ?? {};
    const ca = this.statistics.chiffresAffairesParMois ?? {};
    const mois = Array.from(new Set([...Object.keys(conv), ...Object.keys(ca)])).sort();
    this.monthlyData = mois.map(m => ({
      mois: m,
      conventions: Number(conv[m] ?? 0),
      ca: Number(ca[m] ?? 0),
    }));
  }

  private createCharts(): void {
    this.createConventionsChart();
    this.createCAChart();
    this.createStatutChart();
  }

  private createConventionsChart(): void {
    const canvas = document.getElementById('conventionsChart');
    if (!(canvas instanceof HTMLCanvasElement) || !this.statistics.conventionsParMois) return;
    this.conventionsChart?.destroy();
    this.conventionsChart = new Chart(canvas, {
      type: 'bar',
      data: {
        labels: Object.keys(this.statistics.conventionsParMois),
        datasets: [
          {
            label: 'Conventions signées',
            data: Object.values(this.statistics.conventionsParMois).map(v => Number(v)),
            backgroundColor: 'rgba(54, 162, 235, 0.7)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
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

  private createCAChart(): void {
    const canvas = document.getElementById('chiffresAffairesChart');
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
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            fill: true,
            tension: 0.4,
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

  private createStatutChart(): void {
    const canvas = document.getElementById('statutChart');
    if (!(canvas instanceof HTMLCanvasElement)) return;
    this.statutChart?.destroy();
    const actives = this.statistics.conventionsActives ?? 0;
    const suspendues = this.statistics.conventionsSuspendues ?? 0;
    const total = this.statistics.totalConventions ?? 0;
    const autres = Math.max(0, total - actives - suspendues);
    this.statutChart = new Chart(canvas, {
      type: 'doughnut',
      data: {
        labels: ['Actives', 'Suspendues', 'Autres'],
        datasets: [
          {
            data: [actives, suspendues, autres],
            backgroundColor: ['#28a745', '#ffc107', '#6c757d'],
          },
        ],
      },
      options: { responsive: true, plugins: { legend: { position: 'bottom' } } },
    });
  }
}
