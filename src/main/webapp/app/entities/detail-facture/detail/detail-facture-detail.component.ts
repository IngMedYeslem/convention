import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDetailFacture } from '../detail-facture.model';

@Component({
  selector: 'jhi-detail-facture-detail',
  templateUrl: './detail-facture-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DetailFactureDetailComponent {
  detailFacture = input<IDetailFacture | null>(null);

  previousState(): void {
    window.history.back();
  }
}
