import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  imports: [CommonModule, RouterModule, FontAwesomeModule],
})
export class DashboardComponent implements OnInit {
  statistics: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.http.get('/api/statistiques/dashboard').subscribe(data => {
      this.statistics = data;
    });
  }

  generateAllInvoices(): void {
    this.http.post('/api/facture-generation/active-conventions', {}).subscribe({
      next: (response: any) => {
        const count = Array.isArray(response) ? response.length : 'Toutes les';
        // Notification toast
        const toast = document.createElement('div');
        toast.className = 'toast show position-fixed';
        toast.style.cssText = 'top: 20px; right: 20px; z-index: 9999;';
        toast.innerHTML = `
          <div class="toast-header bg-success text-white">
            <strong class="me-auto">Génération Factures</strong>
            <button type="button" class="btn-close btn-close-white" onclick="this.parentElement.parentElement.remove()"></button>
          </div>
          <div class="toast-body">
            ${count} facture(s) générée(s) avec succès!
          </div>
        `;
        document.body.appendChild(toast);

        setTimeout(() => toast.remove(), 4000);
        this.loadStatistics();
      },
      error: error => {
        console.error('Erreur:', error);
        alert('Erreur lors de la génération des factures');
      },
    });
  }
}
