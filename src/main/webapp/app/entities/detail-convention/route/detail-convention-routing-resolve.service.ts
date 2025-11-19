import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDetailConvention } from '../detail-convention.model';
import { DetailConventionService } from '../service/detail-convention.service';

const detailConventionResolve = (route: ActivatedRouteSnapshot): Observable<null | IDetailConvention> => {
  const id = route.params.id;
  if (id) {
    return inject(DetailConventionService)
      .find(id)
      .pipe(
        mergeMap((detailConvention: HttpResponse<IDetailConvention>) => {
          if (detailConvention.body) {
            return of(detailConvention.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default detailConventionResolve;
