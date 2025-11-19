import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFacture } from 'app/entities/facture/facture.model';
import { FactureService } from 'app/entities/facture/service/facture.service';
import { IDetailFacture } from '../detail-facture.model';
import { DetailFactureService } from '../service/detail-facture.service';
import { DetailFactureFormGroup, DetailFactureFormService } from './detail-facture-form.service';

@Component({
  selector: 'jhi-detail-facture-update',
  templateUrl: './detail-facture-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DetailFactureUpdateComponent implements OnInit {
  isSaving = false;
  detailFacture: IDetailFacture | null = null;

  facturesSharedCollection: IFacture[] = [];

  protected detailFactureService = inject(DetailFactureService);
  protected detailFactureFormService = inject(DetailFactureFormService);
  protected factureService = inject(FactureService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DetailFactureFormGroup = this.detailFactureFormService.createDetailFactureFormGroup();

  compareFacture = (o1: IFacture | null, o2: IFacture | null): boolean => this.factureService.compareFacture(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ detailFacture }) => {
      this.detailFacture = detailFacture;
      if (detailFacture) {
        this.updateForm(detailFacture);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const detailFacture = this.detailFactureFormService.getDetailFacture(this.editForm);
    if (detailFacture.id !== null) {
      this.subscribeToSaveResponse(this.detailFactureService.update(detailFacture));
    } else {
      this.subscribeToSaveResponse(this.detailFactureService.create(detailFacture));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDetailFacture>>): void {
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

  protected updateForm(detailFacture: IDetailFacture): void {
    this.detailFacture = detailFacture;
    this.detailFactureFormService.resetForm(this.editForm, detailFacture);

    this.facturesSharedCollection = this.factureService.addFactureToCollectionIfMissing<IFacture>(
      this.facturesSharedCollection,
      detailFacture.facture,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.factureService
      .query()
      .pipe(map((res: HttpResponse<IFacture[]>) => res.body ?? []))
      .pipe(
        map((factures: IFacture[]) => this.factureService.addFactureToCollectionIfMissing<IFacture>(factures, this.detailFacture?.facture)),
      )
      .subscribe((factures: IFacture[]) => (this.facturesSharedCollection = factures));
  }
}
