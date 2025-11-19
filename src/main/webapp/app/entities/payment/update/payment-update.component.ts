import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
})
export class PaymentUpdateComponent implements OnInit {
  editForm: FormGroup;
  factures: any[] = [];
  isSaving = false;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.editForm = this.fb.group({
      numPaiement: ['', [Validators.required, Validators.maxLength(50)]],
      datePaiement: ['', Validators.required],
      montant: ['', [Validators.required, Validators.min(0)]],
      modePaiement: ['', Validators.required],
      reference: ['', Validators.maxLength(100)],
      facture: ['', Validators.required],
      observations: ['', Validators.maxLength(500)],
    });
  }

  ngOnInit(): void {
    this.loadFactures();

    // Si c'est une édition, charger le paiement
    const paymentId = this.getPaymentIdFromRoute();
    if (paymentId) {
      this.loadPayment(paymentId);
    } else {
      this.generatePaymentNumber();
    }
  }

  loadFactures(): void {
    this.http.get<any[]>('/api/factures').subscribe(data => {
      // Filtrer les factures non payées
      this.factures = data.filter(f => f.statut === 'EMISE');
    });
  }

  generatePaymentNumber(): void {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const time = String(now.getTime()).slice(-4);

    const paymentNumber = `PAY-${year}${month}${day}-${time}`;
    this.editForm.patchValue({ numPaiement: paymentNumber });
  }

  save(): void {
    if (this.editForm.valid) {
      this.isSaving = true;
      const payment = this.editForm.value;
      const paymentId = this.getPaymentIdFromRoute();

      const request = paymentId
        ? this.http.put(`/api/payments/${paymentId}`, { ...payment, id: paymentId })
        : this.http.post('/api/payments', payment);

      request.subscribe({
        next: () => {
          this.showSuccessNotification();
          this.router.navigate(['/payment']);
        },
        error: error => {
          console.error('Erreur sauvegarde paiement:', error);
          alert('Erreur lors de la sauvegarde du paiement');
          this.isSaving = false;
        },
      });
    }
  }

  previousState(): void {
    this.router.navigate(['/payment']);
  }

  private getPaymentIdFromRoute(): number | null {
    const id = this.route.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  private loadPayment(id: number): void {
    this.http.get(`/api/payments/${id}`).subscribe({
      next: (payment: any) => {
        this.editForm.patchValue({
          numPaiement: payment.numPaiement,
          datePaiement: payment.datePaiement,
          montant: payment.montant,
          modePaiement: payment.modePaiement,
          reference: payment.reference,
          facture: payment.facture,
          observations: payment.observations,
        });
      },
      error: error => {
        console.error('Erreur chargement paiement:', error);
        this.router.navigate(['/payment']);
      },
    });
  }

  private showSuccessNotification(): void {
    const isEdit = this.getPaymentIdFromRoute() !== null;
    const message = isEdit ? 'Paiement modifié avec succès' : 'Paiement créé avec succès';

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
