import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IConvention } from 'app/entities/convention/convention.model';
import { ConventionService } from 'app/entities/convention/service/convention.service';
import { TypeFacture } from 'app/entities/enumerations/type-facture.model';
import { StatutFacture } from 'app/entities/enumerations/statut-facture.model';
import { FactureService } from '../service/facture.service';
import { IFacture } from '../facture.model';
import { FactureFormGroup, FactureFormService } from './facture-form.service';

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

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected factureService = inject(FactureService);
  protected factureFormService = inject(FactureFormService);
  protected clientService = inject(ClientService);
  protected conventionService = inject(ConventionService);
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
      }

      this.loadRelationshipsOptions();
    });
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
    if (facture.id !== null) {
      this.subscribeToSaveResponse(this.factureService.update(facture));
    } else {
      this.subscribeToSaveResponse(this.factureService.create(facture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacture>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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
