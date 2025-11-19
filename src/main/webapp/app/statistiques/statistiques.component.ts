import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'jhi-statistiques',
  templateUrl: './statistiques.component.html',
  styleUrls: ['./statistiques.component.scss'],
  imports: [CurrencyPipe],
})
export class StatistiquesComponent implements OnInit {
  statistics: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadStatistics();
  }

  loadStatistics(): void {
    this.http.get('/api/statistiques/dashboard').subscribe(data => {
      this.statistics = data;
      this.createCharts();
    });
  }

  createCharts(): void {
    // Graphiques seront implémentés quand chart.js sera installé
  }
}
