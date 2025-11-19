import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDetailFacture } from '../detail-facture.model';
import { DetailFactureService } from '../service/detail-facture.service';

@Component({
  templateUrl: './detail-facture-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DetailFactureDeleteDialogComponent {
  detailFacture?: IDetailFacture;

  protected detailFactureService = inject(DetailFactureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.detailFactureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
