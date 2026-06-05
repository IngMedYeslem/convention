import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ConventionDocumentService, IConventionDocument } from './convention-document.service';

@Component({
  selector: 'jhi-convention-documents',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card mt-3">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h6 class="mb-0">Documents ({{ documents.length }})</h6>
        <label class="btn btn-sm btn-primary mb-0" style="cursor:pointer">
          + Ajouter un document
          <input type="file" class="d-none" (change)="onFileSelected($event)" accept=".pdf,.doc,.docx,.jpg,.png" />
        </label>
      </div>

      @if (showUploadForm) {
        <div class="card-body border-bottom bg-light">
          <div class="row g-2 align-items-end">
            <div class="col-md-4">
              <label class="form-label form-label-sm">Type de document</label>
              <select class="form-select form-select-sm" [(ngModel)]="typeDocument">
                <option value="CONVENTION_SIGNEE">Convention signée</option>
                <option value="AVENANT">Avenant</option>
                <option value="DELEGATION">Délégation de signature</option>
                <option value="ETUDE_JURIDIQUE">Étude juridique</option>
                <option value="RAPPORT_SUIVI">Rapport de suivi</option>
                <option value="AUTRE">Autre</option>
              </select>
            </div>
            <div class="col-md-5">
              <label class="form-label form-label-sm">Observations</label>
              <input type="text" class="form-control form-control-sm" [(ngModel)]="observations" placeholder="Optionnel..." />
            </div>
            <div class="col-md-3 d-flex gap-2">
              <button class="btn btn-sm btn-success" (click)="upload()" [disabled]="uploading">
                {{ uploading ? 'Envoi...' : 'Uploader' }}
              </button>
              <button class="btn btn-sm btn-secondary" (click)="annuler()">Annuler</button>
            </div>
          </div>
          @if (selectedFile) {
            <small class="text-muted mt-1 d-block"
              >Fichier sélectionné: {{ selectedFile.name }} ({{ (selectedFile.size / 1024).toFixed(1) }} Ko)</small
            >
          }
        </div>
      }

      <div class="card-body p-0">
        @if (documents.length === 0) {
          <p class="text-muted p-3 mb-0">Aucun document joint.</p>
        }
        <ul class="list-group list-group-flush">
          @for (doc of documents; track doc.id) {
            <li class="list-group-item d-flex justify-content-between align-items-center py-2">
              <div>
                <span class="badge bg-secondary me-2">{{ doc.typeDocument }}</span>
                <span>{{ doc.nomFichier }}</span>
                @if (doc.observations) {
                  <small class="text-muted ms-2">— {{ doc.observations }}</small>
                }
                <small class="text-muted ms-2">{{ doc.dateDepot | date: 'dd/MM/yyyy HH:mm' }}</small>
              </div>
              <div class="d-flex gap-2">
                <a [href]="getDownloadUrl(doc.id)" class="btn btn-xs btn-outline-primary" target="_blank"> Télécharger </a>
                <button class="btn btn-xs btn-outline-danger" (click)="supprimer(doc.id)">Suppr.</button>
              </div>
            </li>
          }
        </ul>
      </div>
    </div>
  `,
})
export class ConventionDocumentComponent implements OnInit {
  @Input() conventionId!: number;

  documents: IConventionDocument[] = [];
  selectedFile: File | null = null;
  typeDocument = 'CONVENTION_SIGNEE';
  observations = '';
  uploading = false;
  showUploadForm = false;

  constructor(private documentService: ConventionDocumentService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.documentService.findByConvention(this.conventionId).subscribe(docs => (this.documents = docs));
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
      this.showUploadForm = true;
    }
  }

  upload(): void {
    if (!this.selectedFile) return;
    this.uploading = true;
    this.documentService.upload(this.conventionId, this.selectedFile, this.typeDocument, this.observations).subscribe({
      next: doc => {
        this.documents = [doc, ...this.documents];
        this.annuler();
        this.uploading = false;
      },
      error: () => {
        this.uploading = false;
      },
    });
  }

  annuler(): void {
    this.selectedFile = null;
    this.showUploadForm = false;
    this.observations = '';
  }

  supprimer(id: number): void {
    if (confirm('Supprimer ce document ?')) {
      this.documentService.delete(id).subscribe(() => {
        this.documents = this.documents.filter(d => d.id !== id);
      });
    }
  }

  getDownloadUrl(id: number): string {
    return this.documentService.getDownloadUrl(id);
  }
}
