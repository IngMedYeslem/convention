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
import { IConvention } from 'app/entities/convention/convention.model';
import { ConventionService } from 'app/entities/convention/service/convention.service';
import { DetailConventionService } from '../service/detail-convention.service';
import { IDetailConvention } from '../detail-convention.model';
import { DetailConventionFormGroup, DetailConventionFormService } from './detail-convention-form.service';

@Component({
  selector: 'jhi-detail-convention-update',
  templateUrl: './detail-convention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DetailConventionUpdateComponent implements OnInit {
  isSaving = false;
  detailConvention: IDetailConvention | null = null;

  conventionsSharedCollection: IConvention[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected detailConventionService = inject(DetailConventionService);
  protected detailConventionFormService = inject(DetailConventionFormService);
  protected conventionService = inject(ConventionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DetailConventionFormGroup = this.detailConventionFormService.createDetailConventionFormGroup();

  compareConvention = (o1: IConvention | null, o2: IConvention | null): boolean => this.conventionService.compareConvention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailConvention }) => {
      this.detailConvention = detailConvention;
      if (detailConvention) {
        this.updateForm(detailConvention);
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
    const detailConvention = this.detailConventionFormService.getDetailConvention(this.editForm);
    if (detailConvention.id !== null) {
      this.subscribeToSaveResponse(this.detailConventionService.update(detailConvention));
    } else {
      this.subscribeToSaveResponse(this.detailConventionService.create(detailConvention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailConvention>>): void {
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

  protected updateForm(detailConvention: IDetailConvention): void {
    this.detailConvention = detailConvention;
    this.detailConventionFormService.resetForm(this.editForm, detailConvention);

    this.conventionsSharedCollection = this.conventionService.addConventionToCollectionIfMissing<IConvention>(
      this.conventionsSharedCollection,
      detailConvention.convention,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.conventionService
      .query()
      .pipe(map((res: HttpResponse<IConvention[]>) => res.body ?? []))
      .pipe(
        map((conventions: IConvention[]) =>
          this.conventionService.addConventionToCollectionIfMissing<IConvention>(conventions, this.detailConvention?.convention),
        ),
      )
      .subscribe((conventions: IConvention[]) => (this.conventionsSharedCollection = conventions));
  }
}
