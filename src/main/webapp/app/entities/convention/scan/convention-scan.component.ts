import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { PdfScanService } from '../service/pdf-scan.service';
import { ConventionService } from '../service/convention.service';
import { IConvention } from '../convention.model';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-convention-scan',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container-fluid">
      <div class="row justify-content-center">
        <div class="col-md-8">
          <div class="card">
            <div class="card-header">
              <h4 class="card-title mb-0">
                <i class="fa fa-file-pdf-o me-2"></i>
                Scanner une Convention PDF
              </h4>
            </div>
            <div class="card-body">
              <div class="alert alert-info mb-3">
                <h6><i class="fa fa-info-circle me-2"></i>Test de la fonctionnalité</h6>
                <p class="mb-2">Vous pouvez télécharger un PDF de test pour essayer la fonctionnalité de scan :</p>
                <a href="/api/pdf/test" class="btn btn-sm btn-outline-info" target="_blank">
                  <i class="fa fa-download me-1"></i>
                  Télécharger PDF de test
                </a>
              </div>

              <form [formGroup]="scanForm" (ngSubmit)="onSubmit()">
                <div class="mb-3">
                  <label for="pdfFile" class="form-label">Sélectionner un fichier PDF</label>
                  <input
                    type="file"
                    class="form-control"
                    id="pdfFile"
                    accept=".pdf"
                    (change)="onFileSelected($event)"
                    [class.is-invalid]="scanForm.get('file')?.invalid && scanForm.get('file')?.touched"
                  />
                  <div class="invalid-feedback" *ngIf="scanForm.get('file')?.invalid && scanForm.get('file')?.touched">
                    Veuillez sélectionner un fichier PDF valide.
                  </div>
                  <div class="form-text">Formats supportés : PDF avec texte sélectionnable ou QR code</div>
                </div>

                <div class="d-flex justify-content-between">
                  <button type="button" class="btn btn-secondary" (click)="goBack()">
                    <i class="fa fa-arrow-left me-1"></i>
                    Retour
                  </button>
                  <button type="submit" class="btn btn-primary" [disabled]="scanForm.invalid || isScanning">
                    <i class="fa fa-search me-1" *ngIf="!isScanning"></i>
                    <i class="fa fa-spinner fa-spin me-1" *ngIf="isScanning"></i>
                    {{ isScanning ? 'Scan en cours...' : 'Scanner le PDF' }}
                  </button>
                </div>
              </form>

              <div class="mt-4" *ngIf="scannedData">
                <div class="alert alert-success">
                  <h5><i class="fa fa-check-circle me-2"></i>Données extraites avec succès !</h5>
                </div>
                <div class="card">
                  <div class="card-header">
                    <h6 class="mb-0">Informations détectées</h6>
                  </div>
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-6" *ngIf="scannedData.numConvention"><strong>Numéro:</strong> {{ scannedData.numConvention }}</div>
                      <div class="col-md-6" *ngIf="scannedData.dateSignConv">
                        <strong>Date signature:</strong> {{ scannedData.dateSignConv }}
                      </div>
                      <div class="col-md-6" *ngIf="scannedData.dateDebutConv">
                        <strong>Date début:</strong> {{ scannedData.dateDebutConv }}
                      </div>
                      <div class="col-md-6" *ngIf="scannedData.periodeEcheance">
                        <strong>Période d'échéance:</strong> {{ scannedData.periodeEcheance === 'MENSUEL' ? 'Mensuel' : 'Annuel' }}
                      </div>
                      <div class="col-md-6" *ngIf="scannedData.redevance">
                        <strong>Redevance:</strong> {{ scannedData.redevance | currency: 'MRU' }}
                      </div>
                      <div class="col-md-6" *ngIf="scannedData.nomResponsable">
                        <strong>Responsable:</strong> {{ scannedData.nomResponsable }}
                      </div>
                    </div>

                    <div class="mt-3 d-flex gap-2">
                      <button type="button" class="btn btn-success" (click)="createConvention()">
                        <i class="fa fa-plus me-1"></i>
                        Créer la Convention
                      </button>
                      <button type="button" class="btn btn-outline-primary" (click)="editBeforeCreate()">
                        <i class="fa fa-edit me-1"></i>
                        Modifier avant création
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
})
export class ConventionScanComponent {
  scanForm: FormGroup;
  isScanning = false;
  scannedData: IConvention | null = null;

  private readonly pdfScanService = inject(PdfScanService);
  private readonly conventionService = inject(ConventionService);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly alertService = inject(AlertService);

  constructor() {
    this.scanForm = this.fb.group({
      file: [null, Validators.required],
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      if (file.type === 'application/pdf') {
        this.scanForm.patchValue({ file });
        this.scanForm.get('file')?.markAsTouched();
      } else {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Veuillez sélectionner un fichier PDF valide.',
        });
        this.scanForm.patchValue({ file: null });
      }
    }
  }

  onSubmit(): void {
    if (this.scanForm.valid) {
      const file = this.scanForm.get('file')?.value;
      this.scanPdf(file);
    }
  }

  createConvention(): void {
    if (this.scannedData) {
      const newConvention = { ...this.scannedData, id: null };
      this.conventionService.create(newConvention).subscribe({
        next: () => {
          this.alertService.addAlert({
            type: 'success',
            message: 'Convention créée avec succès !',
          });
          this.router.navigate(['/convention']);
        },
        error: () => {
          this.alertService.addAlert({
            type: 'danger',
            message: 'Erreur lors de la création de la convention.',
          });
        },
      });
    }
  }

  editBeforeCreate(): void {
    if (this.scannedData) {
      this.router.navigate(['/convention/new'], {
        state: { scannedData: this.scannedData },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/convention']);
  }

  private scanPdf(file: File): void {
    this.isScanning = true;
    this.scannedData = null;

    this.pdfScanService.scanPdf(file).subscribe({
      next: response => {
        this.scannedData = response.body;
        this.isScanning = false;
        this.alertService.addAlert({
          type: 'success',
          message: 'PDF scanné avec succès !',
        });
      },
      error: error => {
        this.isScanning = false;
        this.alertService.addAlert({
          type: 'danger',
          message: 'Erreur lors du scan du PDF. Veuillez réessayer.',
        });
        console.error('Scan error:', error);
      },
    });
  }
}
