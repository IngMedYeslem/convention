import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, forkJoin, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IDetailConvention, NewDetailConvention } from 'app/entities/detail-convention/detail-convention.model';
import { DetailConventionService } from 'app/entities/detail-convention/service/detail-convention.service';
import { StatutConvention } from 'app/entities/enumerations/statut-convention.model';
import { PeriodeEcheance } from 'app/entities/enumerations/periode-echeance.model';
import { ConventionService } from '../service/convention.service';
import { IConvention } from '../convention.model';
import { ConventionFormGroup, ConventionFormService } from './convention-form.service';
import { AccountService } from 'app/core/auth/account.service';

interface LigneConvention {
  id?: number | null;
  designation: string;
  prixUnitaire: number;
  quantite: number;
  montantTotal: number;
  observations: string;
  _deleted?: boolean;
}

@Component({
  selector: 'jhi-convention-update',
  templateUrl: './convention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConventionUpdateComponent implements OnInit {
  isSaving = false;
  convention: IConvention | null = null;
  statutConventionValues = Object.keys(StatutConvention);
  periodeEcheanceValues = Object.keys(PeriodeEcheance);

  clientsSharedCollection: IClient[] = [];
  lignes: LigneConvention[] = [];

  readonly account = inject(AccountService).trackCurrentAccount();

  protected http = inject(HttpClient);
  protected conventionService = inject(ConventionService);
  protected conventionFormService = inject(ConventionFormService);
  protected clientService = inject(ClientService);
  protected detailConventionService = inject(DetailConventionService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConventionFormGroup = this.conventionFormService.createConventionFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ convention }) => {
      this.convention = convention;
      if (convention) {
        this.updateForm(convention);
        if (convention.id) {
          this.http.get<IDetailConvention[]>(`/api/detail-conventions/by-convention/${convention.id}`).subscribe(details => {
            this.lignes = details.map(d => ({
              id: d.id,
              designation: d.designation ?? '',
              prixUnitaire: Number(d.prixUnitaire ?? 0),
              quantite: d.quantite ?? 1,
              montantTotal: Number(d.montantTotal ?? 0),
              observations: d.observations ?? '',
            }));
          });
        }
      } else {
        // Nouvelle convention : SERVICE → forcer BROUILLON
        const niveau = (this.account() as any)?.niveauHierarchique;
        if (niveau === 'SERVICE' || niveau) {
          this.editForm.patchValue({ statut: StatutConvention.BROUILLON });
        }
      }

      this.loadRelationshipsOptions();

      // Pré-remplir depuis les données scannées (router state)
      const scanned = this.router.getCurrentNavigation()?.extras?.state?.['scannedData'] ?? history.state?.scannedData;
      if (scanned && !convention) {
        setTimeout(() => this.prefillFromScanned(scanned), 300);
      }
    });
  }

  private prefillFromScanned(scanned: IConvention): void {
    const patch: Record<string, unknown> = {};
    if (scanned.numConvention != null) patch['numConvention'] = scanned.numConvention;
    if (scanned.dateSignConv) patch['dateSignConv'] = scanned.dateSignConv;
    if (scanned.dateDebutConv) patch['dateDebutConv'] = scanned.dateDebutConv;
    if (scanned.periodeEcheance) patch['periodeEcheance'] = scanned.periodeEcheance;
    if (scanned.redevance != null) patch['redevance'] = scanned.redevance;
    if (scanned.nomResponsable) patch['nomResponsable'] = scanned.nomResponsable;
    if (scanned.client) {
      const found = this.clientsSharedCollection.find(c => c.id === scanned.client!.id);
      if (found) patch['client'] = found;
    }
    this.editForm.patchValue(patch as any);
  }

  addLigne(): void {
    this.lignes.push({ designation: '', prixUnitaire: 0, quantite: 1, montantTotal: 0, observations: '' });
  }

  removeLigne(index: number): void {
    const ligne = this.lignes[index];
    if (ligne.id) {
      ligne._deleted = true;
    } else {
      this.lignes.splice(index, 1);
    }
  }

  calcLigne(ligne: LigneConvention): void {
    ligne.montantTotal = Math.round(ligne.prixUnitaire * ligne.quantite * 100) / 100;
  }

  get lignesActives(): LigneConvention[] {
    return this.lignes.filter(l => !l._deleted);
  }

  get totalConvention(): number {
    return this.lignesActives.reduce((s, l) => s + l.montantTotal, 0);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const convention = this.conventionFormService.getConvention(this.editForm);
    const save$ = convention.id !== null ? this.conventionService.update(convention) : this.conventionService.create(convention);

    save$
      .pipe(
        switchMap((res: HttpResponse<IConvention>) => {
          const savedConvention = res.body!;
          const ops: Observable<any>[] = [];
          for (const ligne of this.lignes) {
            if (ligne._deleted && ligne.id) {
              ops.push(this.detailConventionService.delete(ligne.id));
            } else if (!ligne._deleted && ligne.id) {
              const dto: IDetailConvention = {
                id: ligne.id,
                designation: ligne.designation,
                prixUnitaire: ligne.prixUnitaire,
                quantite: ligne.quantite,
                montantTotal: ligne.montantTotal,
                observations: ligne.observations,
                convention: { id: savedConvention.id! },
              };
              ops.push(this.detailConventionService.update(dto));
            } else if (!ligne._deleted && !ligne.id) {
              const dto: NewDetailConvention = {
                id: null,
                designation: ligne.designation,
                prixUnitaire: ligne.prixUnitaire,
                quantite: ligne.quantite,
                montantTotal: ligne.montantTotal,
                observations: ligne.observations,
                convention: { id: savedConvention.id! },
              };
              ops.push(this.detailConventionService.create(dto));
            }
          }
          return ops.length > 0 ? forkJoin(ops) : of([]);
        }),
        finalize(() => this.onSaveFinalize()),
      )
      .subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(convention: IConvention): void {
    this.convention = convention;
    this.conventionFormService.resetForm(this.editForm, convention);

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(
      this.clientsSharedCollection,
      convention.client,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.convention?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }
}
