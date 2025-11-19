import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFacture } from '../facture.model';
import { FactureService } from '../service/facture.service';

const factureResolve = (route: ActivatedRouteSnapshot): Observable<null | IFacture> => {
  const id = route.params.id;
  if (id) {
    return inject(FactureService)
      .find(id)
      .pipe(
        mergeMap((facture: HttpResponse<IFacture>) => {
          if (facture.body) {
            return of(facture.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default factureResolve;
