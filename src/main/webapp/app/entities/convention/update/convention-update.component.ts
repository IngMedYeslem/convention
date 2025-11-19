import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { StatutConvention } from 'app/entities/enumerations/statut-convention.model';
import { ConventionService } from '../service/convention.service';
import { IConvention } from '../convention.model';
import { ConventionFormGroup, ConventionFormService } from './convention-form.service';

@Component({
  selector: 'jhi-convention-update',
  templateUrl: './convention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConventionUpdateComponent implements OnInit {
  isSaving = false;
  convention: IConvention | null = null;
  statutConventionValues = Object.keys(StatutConvention);

  clientsSharedCollection: IClient[] = [];

  protected conventionService = inject(ConventionService);
  protected conventionFormService = inject(ConventionFormService);
  protected clientService = inject(ClientService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConventionFormGroup = this.conventionFormService.createConventionFormGroup();

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ convention }) => {
      this.convention = convention;
      if (convention) {
        this.updateForm(convention);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const convention = this.conventionFormService.getConvention(this.editForm);
    if (convention.id !== null) {
      this.subscribeToSaveResponse(this.conventionService.update(convention));
    } else {
      this.subscribeToSaveResponse(this.conventionService.create(convention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConvention>>): void {
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
