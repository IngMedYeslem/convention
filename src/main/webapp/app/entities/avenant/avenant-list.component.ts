import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { IAvenant } from './avenant.model';
import { AvenantService } from './avenant.service';
import { AvenantFormComponent } from './avenant-form.component';

@Component({
  selector: 'jhi-avenant-list',
  standalone: true,
  imports: [CommonModule, RouterModule, AvenantFormComponent],
  template: `
    <div class="card mt-3">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h6 class="mb-0">Avenants ({{ avenants.length }})</h6>
        <button class="btn btn-sm btn-primary" (click)="showForm = !showForm">
          <span>+ Nouvel avenant</span>
        </button>
      </div>
      <div class="card-body p-0">
        @if (showForm) {
          <div class="p-3 border-bottom bg-light">
            <jhi-avenant-form [conventionId]="conventionId" (saved)="onSaved($event)" (cancelled)="showForm = false" />
          </div>
        }
        @if (avenants.length === 0) {
          <p class="text-muted p-3 mb-0">Aucun avenant enregistré.</p>
        }
        <table class="table table-sm table-hover mb-0">
          @if (avenants.length > 0) {
            <thead class="table-light">
              <tr>
                <th>N°</th>
                <th>Date signature</th>
                <th>Type</th>
                <th>Objet</th>
                <th>Nouvelle échéance</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              @for (av of avenants; track av.id) {
                <tr>
                  <td>{{ av.numeroAvenant }}</td>
                  <td>{{ av.dateSignature | date: 'dd/MM/yyyy' }}</td>
                  <td>
                    <span class="badge bg-info">{{ av.typeAvenant }}</span>
                  </td>
                  <td>{{ av.objet }}</td>
                  <td>{{ av.nouvelleEcheance | date: 'dd/MM/yyyy' }}</td>
                  <td>
                    <span [class]="getStatutClass(av.statut)">{{ av.statut }}</span>
                  </td>
                  <td>
                    @if (av.statut === 'BROUILLON' || av.statut === 'EN_VALIDATION') {
                      <button class="btn btn-xs btn-success me-1" (click)="signer(av.id)">Signer</button>
                    }
                    @if (av.statut === 'BROUILLON') {
                      <button class="btn btn-xs btn-danger" (click)="supprimer(av.id)">Suppr.</button>
                    }
                  </td>
                </tr>
              }
            </tbody>
          }
        </table>
      </div>
    </div>
  `,
})
export class AvenantListComponent implements OnInit {
  @Input() conventionId!: number;
  avenants: IAvenant[] = [];
  showForm = false;

  constructor(private avenantService: AvenantService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.avenantService.findByConvention(this.conventionId).subscribe(data => (this.avenants = data));
  }

  onSaved(avenant: IAvenant): void {
    this.avenants = [...this.avenants, avenant];
    this.showForm = false;
  }

  signer(id: number): void {
    this.avenantService.signer(id).subscribe(() => this.load());
  }

  supprimer(id: number): void {
    if (confirm('Supprimer cet avenant ?')) {
      this.avenantService.delete(id).subscribe(() => this.load());
    }
  }

  getStatutClass(statut: string | null | undefined): string {
    switch (statut) {
      case 'SIGNE':
        return 'badge bg-success';
      case 'EN_VALIDATION':
        return 'badge bg-warning text-dark';
      case 'ANNULE':
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  }
}
