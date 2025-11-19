import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';

@Component({
  templateUrl: './facture-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FactureDeleteDialogComponent {
  facture?: IFacture;

  protected factureService = inject(FactureService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.factureService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
