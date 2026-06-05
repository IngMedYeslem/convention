import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { AvenantService } from './avenant.service';
import { IAvenant } from './avenant.model';

@Component({
  selector: 'jhi-avenant-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="save()">
      <h6>Nouvel avenant</h6>
      <div class="row">
        <div class="col-md-4 mb-2">
          <label class="form-label">Type d'avenant *</label>
          <select class="form-select form-select-sm" formControlName="typeAvenant">
            <option value="PROLONGATION">Prolongation</option>
            <option value="REVISION_MONTANT">Révision du montant</option>
            <option value="MODIFICATION_CLAUSES">Modification des clauses</option>
            <option value="RESILIATION_ANTICIPEE">Résiliation anticipée</option>
            <option value="SUSPENSION">Suspension</option>
            <option value="AUTRE">Autre</option>
          </select>
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">Date de signature *</label>
          <input type="date" class="form-control form-control-sm" formControlName="dateSignature" />
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">Nouvelle échéance</label>
          <input type="date" class="form-control form-control-sm" formControlName="nouvelleEcheance" />
        </div>
      </div>
      <div class="row">
        <div class="col-md-8 mb-2">
          <label class="form-label">Objet *</label>
          <input type="text" class="form-control form-control-sm" formControlName="objet" />
        </div>
        <div class="col-md-4 mb-2">
          <label class="form-label">Montant additionnel (MRU)</label>
          <input type="number" class="form-control form-control-sm" formControlName="montantAdditionnel" />
        </div>
      </div>
      <div class="mb-2">
        <label class="form-label">Modifications des clauses</label>
        <textarea class="form-control form-control-sm" rows="2" formControlName="modificationsClauses"></textarea>
      </div>
      <div class="d-flex gap-2">
        <button type="submit" class="btn btn-sm btn-primary" [disabled]="form.invalid || saving">Enregistrer</button>
        <button type="button" class="btn btn-sm btn-secondary" (click)="cancelled.emit()">Annuler</button>
      </div>
    </form>
  `,
})
export class AvenantFormComponent {
  @Input() conventionId!: number;
  @Output() saved = new EventEmitter<IAvenant>();
  @Output() cancelled = new EventEmitter<void>();

  saving = false;

  form = this.fb.group({
    typeAvenant: ['PROLONGATION', Validators.required],
    dateSignature: ['', Validators.required],
    objet: ['', [Validators.required, Validators.maxLength(500)]],
    nouvelleEcheance: [''],
    montantAdditionnel: [null as number | null],
    modificationsClauses: [''],
  });

  constructor(
    private fb: FormBuilder,
    private avenantService: AvenantService,
  ) {}

  save(): void {
    if (this.form.invalid) return;
    this.saving = true;
    const val = this.form.getRawValue();
    this.avenantService
      .create({
        id: null,
        typeAvenant: val.typeAvenant as any,
        dateSignature: val.dateSignature ? (val.dateSignature as any) : null,
        objet: val.objet,
        nouvelleEcheance: val.nouvelleEcheance ? (val.nouvelleEcheance as any) : null,
        montantAdditionnel: val.montantAdditionnel,
        modificationsClauses: val.modificationsClauses,
        statut: 'BROUILLON',
        convention: { id: this.conventionId, numConvention: this.conventionId },
      })
      .subscribe({
        next: avenant => {
          this.saved.emit(avenant);
          this.saving = false;
        },
        error: () => {
          this.saving = false;
        },
      });
  }
}
