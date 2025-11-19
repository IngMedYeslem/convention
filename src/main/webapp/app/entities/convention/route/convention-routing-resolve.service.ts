import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConvention } from '../convention.model';
import { ConventionService } from '../service/convention.service';

const conventionResolve = (route: ActivatedRouteSnapshot): Observable<null | IConvention> => {
  const id = route.params.id;
  if (id) {
    return inject(ConventionService)
      .find(id)
      .pipe(
        mergeMap((convention: HttpResponse<IConvention>) => {
          if (convention.body) {
            return of(convention.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default conventionResolve;
