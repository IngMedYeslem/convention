import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailFacture, NewDetailFacture } from '../detail-facture.model';

export type PartialUpdateDetailFacture = Partial<IDetailFacture> & Pick<IDetailFacture, 'id'>;

export type EntityResponseType = HttpResponse<IDetailFacture>;
export type EntityArrayResponseType = HttpResponse<IDetailFacture[]>;

@Injectable({ providedIn: 'root' })
export class DetailFactureService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detail-factures');

  create(detailFacture: NewDetailFacture): Observable<EntityResponseType> {
    return this.http.post<IDetailFacture>(this.resourceUrl, detailFacture, { observe: 'response' });
  }

  update(detailFacture: IDetailFacture): Observable<EntityResponseType> {
    return this.http.put<IDetailFacture>(`${this.resourceUrl}/${this.getDetailFactureIdentifier(detailFacture)}`, detailFacture, {
      observe: 'response',
    });
  }

  partialUpdate(detailFacture: PartialUpdateDetailFacture): Observable<EntityResponseType> {
    return this.http.patch<IDetailFacture>(`${this.resourceUrl}/${this.getDetailFactureIdentifier(detailFacture)}`, detailFacture, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDetailFacture>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDetailFacture[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDetailFactureIdentifier(detailFacture: Pick<IDetailFacture, 'id'>): number {
    return detailFacture.id;
  }

  compareDetailFacture(o1: Pick<IDetailFacture, 'id'> | null, o2: Pick<IDetailFacture, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetailFactureIdentifier(o1) === this.getDetailFactureIdentifier(o2) : o1 === o2;
  }

  addDetailFactureToCollectionIfMissing<Type extends Pick<IDetailFacture, 'id'>>(
    detailFactureCollection: Type[],
    ...detailFacturesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detailFactures: Type[] = detailFacturesToCheck.filter(isPresent);
    if (detailFactures.length > 0) {
      const detailFactureCollectionIdentifiers = detailFactureCollection.map(detailFactureItem =>
        this.getDetailFactureIdentifier(detailFactureItem),
      );
      const detailFacturesToAdd = detailFactures.filter(detailFactureItem => {
        const detailFactureIdentifier = this.getDetailFactureIdentifier(detailFactureItem);
        if (detailFactureCollectionIdentifiers.includes(detailFactureIdentifier)) {
          return false;
        }
        detailFactureCollectionIdentifiers.push(detailFactureIdentifier);
        return true;
      });
      return [...detailFacturesToAdd, ...detailFactureCollection];
    }
    return detailFactureCollection;
  }
}
