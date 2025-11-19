import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-payment-detail',
  templateUrl: './payment-detail.component.html',
  imports: [CommonModule, FontAwesomeModule],
})
export class PaymentDetailComponent implements OnInit {
  payment: any = null;
  paymentId: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
  ) {
    this.paymentId = Number(this.route.snapshot.paramMap.get('id'));
  }

  ngOnInit(): void {
    this.loadPayment();
  }

  loadPayment(): void {
    this.http.get(`/api/payments/${this.paymentId}`).subscribe({
      next: data => {
        this.payment = data;
      },
      error: error => {
        console.error('Erreur chargement paiement:', error);
        this.router.navigate(['/payment']);
      },
    });
  }

  edit(): void {
    this.router.navigate(['/payment', this.paymentId, 'edit']);
  }

  delete(): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce paiement ?')) {
      this.http.delete(`/api/payments/${this.paymentId}`).subscribe({
        next: () => {
          this.showSuccessNotification('Paiement supprimé avec succès');
          this.router.navigate(['/payment']);
        },
        error: error => {
          console.error('Erreur suppression:', error);
          alert('Erreur lors de la suppression');
        },
      });
    }
  }

  previousState(): void {
    this.router.navigate(['/payment']);
  }

  private showSuccessNotification(message: string): void {
    const notification = document.createElement('div');
    notification.className = 'alert alert-success alert-dismissible fade show position-fixed';
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    notification.innerHTML = `
      <strong>Succès!</strong> ${message}
      <button type="button" class="btn-close" onclick="this.parentElement.remove()"></button>
    `;
    document.body.appendChild(notification);

    setTimeout(() => {
      if (notification.parentElement) {
        notification.remove();
      }
    }, 3000);
  }
}
