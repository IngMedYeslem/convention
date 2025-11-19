import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetailFacture } from '../detail-facture.model';
import { DetailFactureService } from '../service/detail-facture.service';

const detailFactureResolve = (route: ActivatedRouteSnapshot): Observable<null | IDetailFacture> => {
  const id = route.params.id;
  if (id) {
    return inject(DetailFactureService)
      .find(id)
      .pipe(
        mergeMap((detailFacture: HttpResponse<IDetailFacture>) => {
          if (detailFacture.body) {
            return of(detailFacture.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default detailFactureResolve;
