import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  imports: [CommonModule, FontAwesomeModule],
})
export class PaymentComponent implements OnInit {
  payments: any[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments(): void {
    this.http.get<any[]>('/api/payments').subscribe(data => {
      this.payments = data;
    });
  }

  createPayment(): void {
    this.router.navigate(['/payment/new']);
  }

  viewPayment(id: number): void {
    this.router.navigate(['/payment', id, 'view']);
  }

  editPayment(id: number): void {
    this.router.navigate(['/payment', id, 'edit']);
  }

  deletePayment(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce paiement ?')) {
      this.http.delete(`/api/payments/${id}`).subscribe(() => {
        this.loadPayments();
      });
    }
  }
}
