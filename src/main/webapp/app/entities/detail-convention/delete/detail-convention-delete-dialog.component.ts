import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDetailConvention } from '../detail-convention.model';
import { DetailConventionService } from '../service/detail-convention.service';

@Component({
  templateUrl: './detail-convention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DetailConventionDeleteDialogComponent {
  detailConvention?: IDetailConvention;

  protected detailConventionService = inject(DetailConventionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailConventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
