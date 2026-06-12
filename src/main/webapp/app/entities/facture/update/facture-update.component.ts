import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, forkJoin, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IConvention } from 'app/entities/convention/convention.model';
import { ConventionService } from 'app/entities/convention/service/convention.service';
import { IDetailFacture, NewDetailFacture } from 'app/entities/detail-facture/detail-facture.model';
import { DetailFactureService } from 'app/entities/detail-facture/service/detail-facture.service';
import { TypeFacture } from 'app/entities/enumerations/type-facture.model';
import { StatutFacture } from 'app/entities/enumerations/statut-facture.model';
import { FactureService } from '../service/facture.service';
import { IFacture } from '../facture.model';
import { FactureFormGroup, FactureFormService } from './facture-form.service';

interface LigneFacture {
  id?: number | null;
  designation: string;
  prixUnitaire: number;
  quantite: number;
  montantHT: number;
  tauxTVA: number;
  montantTVA: number;
  montantTTC: number;
  observations: string;
  _deleted?: boolean;
}

@Component({
  selector: 'jhi-facture-update',
  templateUrl: './facture-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FactureUpdateComponent implements OnInit {
  isSaving = false;
  facture: IFacture | null = null;
  typeFactureValues = Object.keys(TypeFacture);
  statutFactureValues = Object.keys(StatutFacture);

  clientsSharedCollection: IClient[] = [];
  conventionsSharedCollection: IConvention[] = [];
  lignes: LigneFacture[] = [];

  protected http = inject(HttpClient);
  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected factureService = inject(FactureService);
  protected factureFormService = inject(FactureFormService);
  protected clientService = inject(ClientService);
  protected conventionService = inject(ConventionService);
  protected detailFactureService = inject(DetailFactureService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FactureFormGroup = this.factureFormService.createFactureFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  compareConvention = (o1: IConvention | null, o2: IConvention | null): boolean => this.conventionService.compareConvention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facture }) => {
      this.facture = facture;
      if (facture) {
        this.updateForm(facture);
        if (facture.id) {
          this.http.get<IDetailFacture[]>(`/api/detail-factures/by-facture/${facture.id}`).subscribe(details => {
            this.lignes = details.map(d => ({
              id: d.id,
              designation: d.designation ?? '',
              prixUnitaire: Number(d.prixUnitaire ?? 0),
              quantite: d.quantite ?? 1,
              montantHT: Number(d.montantHT ?? 0),
              tauxTVA: Number(d.tauxTVA ?? 0),
              montantTVA: Number(d.montantTVA ?? 0),
              montantTTC: Number(d.montantTTC ?? 0),
              observations: d.observations ?? '',
            }));
          });
        }
      }

      this.loadRelationshipsOptions();
    });
  }

  addLigne(): void {
    this.lignes.push({
      designation: '',
      prixUnitaire: 0,
      quantite: 1,
      montantHT: 0,
      tauxTVA: 0,
      montantTVA: 0,
      montantTTC: 0,
      observations: '',
    });
  }

  removeLigne(index: number): void {
    const ligne = this.lignes[index];
    if (ligne.id) {
      ligne._deleted = true;
    } else {
      this.lignes.splice(index, 1);
    }
  }

  calcLigne(ligne: LigneFacture): void {
    ligne.montantHT = Math.round(ligne.prixUnitaire * ligne.quantite * 100) / 100;
    ligne.montantTVA = Math.round(((ligne.montantHT * ligne.tauxTVA) / 100) * 100) / 100;
    ligne.montantTTC = Math.round((ligne.montantHT + ligne.montantTVA) * 100) / 100;
  }

  get lignesActives(): LigneFacture[] {
    return this.lignes.filter(l => !l._deleted);
  }

  get totalHT(): number {
    return this.lignesActives.reduce((s, l) => s + l.montantHT, 0);
  }

  get totalTTC(): number {
    return this.lignesActives.reduce((s, l) => s + l.montantTTC, 0);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('conventionApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facture = this.factureFormService.getFacture(this.editForm);
    const save$ = facture.id !== null ? this.factureService.update(facture) : this.factureService.create(facture);

    save$
      .pipe(
        switchMap((res: HttpResponse<IFacture>) => {
          const savedFacture = res.body!;
          const ops: Observable<any>[] = [];
          for (const ligne of this.lignes) {
            if (ligne._deleted && ligne.id) {
              ops.push(this.detailFactureService.delete(ligne.id));
            } else if (!ligne._deleted && ligne.id) {
              const dto: IDetailFacture = {
                id: ligne.id,
                designation: ligne.designation,
                prixUnitaire: ligne.prixUnitaire,
                quantite: ligne.quantite,
                montantHT: ligne.montantHT,
                tauxTVA: ligne.tauxTVA,
                montantTVA: ligne.montantTVA,
                montantTTC: ligne.montantTTC,
                observations: ligne.observations,
                facture: { id: savedFacture.id! },
              };
              ops.push(this.detailFactureService.update(dto));
            } else if (!ligne._deleted && !ligne.id) {
              const dto: NewDetailFacture = {
                id: null,
                designation: ligne.designation,
                prixUnitaire: ligne.prixUnitaire,
                quantite: ligne.quantite,
                montantHT: ligne.montantHT,
                tauxTVA: ligne.tauxTVA,
                montantTVA: ligne.montantTVA,
                montantTTC: ligne.montantTTC,
                observations: ligne.observations,
                facture: { id: savedFacture.id! },
              };
              ops.push(this.detailFactureService.create(dto));
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

  protected subscribeToSaveResponse(_result: Observable<HttpResponse<IFacture>>): void {
    // kept for compatibility — actual save is in save()
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(facture: IFacture): void {
    this.facture = facture;
    this.factureFormService.resetForm(this.editForm, facture);

    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.clientsSharedCollection, facture.client);
    this.conventionsSharedCollection = this.conventionService.addConventionToCollectionIfMissing<IConvention>(
      this.conventionsSharedCollection,
      facture.convention,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.facture?.client)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));

    this.conventionService
      .query()
      .pipe(map((res: HttpResponse<IConvention[]>) => res.body ?? []))
      .pipe(
        map((conventions: IConvention[]) =>
          this.conventionService.addConventionToCollectionIfMissing<IConvention>(conventions, this.facture?.convention),
        ),
      )
      .subscribe((conventions: IConvention[]) => (this.conventionsSharedCollection = conventions));
  }
}
