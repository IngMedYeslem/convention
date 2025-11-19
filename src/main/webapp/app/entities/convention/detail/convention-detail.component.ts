import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IConvention } from '../convention.model';

@Component({
  selector: 'jhi-convention-detail',
  templateUrl: './convention-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConventionDetailComponent {
  convention = input<IConvention | null>(null);

  previousState(): void {
    window.history.back();
  }
}
