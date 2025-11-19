import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDetailConvention, NewDetailConvention } from '../detail-convention.model';

export type PartialUpdateDetailConvention = Partial<IDetailConvention> & Pick<IDetailConvention, 'id'>;

type RestOf<T extends IDetailConvention | NewDetailConvention> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestDetailConvention = RestOf<IDetailConvention>;

export type NewRestDetailConvention = RestOf<NewDetailConvention>;

export type PartialUpdateRestDetailConvention = RestOf<PartialUpdateDetailConvention>;

export type EntityResponseType = HttpResponse<IDetailConvention>;
export type EntityArrayResponseType = HttpResponse<IDetailConvention[]>;

@Injectable({ providedIn: 'root' })
export class DetailConventionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/detail-conventions');

  create(detailConvention: NewDetailConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailConvention);
    return this.http
      .post<RestDetailConvention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(detailConvention: IDetailConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailConvention);
    return this.http
      .put<RestDetailConvention>(`${this.resourceUrl}/${this.getDetailConventionIdentifier(detailConvention)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(detailConvention: PartialUpdateDetailConvention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(detailConvention);
    return this.http
      .patch<RestDetailConvention>(`${this.resourceUrl}/${this.getDetailConventionIdentifier(detailConvention)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDetailConvention>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDetailConvention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDetailConventionIdentifier(detailConvention: Pick<IDetailConvention, 'id'>): number {
    return detailConvention.id;
  }

  compareDetailConvention(o1: Pick<IDetailConvention, 'id'> | null, o2: Pick<IDetailConvention, 'id'> | null): boolean {
    return o1 && o2 ? this.getDetailConventionIdentifier(o1) === this.getDetailConventionIdentifier(o2) : o1 === o2;
  }

  addDetailConventionToCollectionIfMissing<Type extends Pick<IDetailConvention, 'id'>>(
    detailConventionCollection: Type[],
    ...detailConventionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const detailConventions: Type[] = detailConventionsToCheck.filter(isPresent);
    if (detailConventions.length > 0) {
      const detailConventionCollectionIdentifiers = detailConventionCollection.map(detailConventionItem =>
        this.getDetailConventionIdentifier(detailConventionItem),
      );
      const detailConventionsToAdd = detailConventions.filter(detailConventionItem => {
        const detailConventionIdentifier = this.getDetailConventionIdentifier(detailConventionItem);
        if (detailConventionCollectionIdentifiers.includes(detailConventionIdentifier)) {
          return false;
        }
        detailConventionCollectionIdentifiers.push(detailConventionIdentifier);
        return true;
      });
      return [...detailConventionsToAdd, ...detailConventionCollection];
    }
    return detailConventionCollection;
  }

  protected convertDateFromClient<T extends IDetailConvention | NewDetailConvention | PartialUpdateDetailConvention>(
    detailConvention: T,
  ): RestOf<T> {
    return {
      ...detailConvention,
      dateCreation: detailConvention.dateCreation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDetailConvention: RestDetailConvention): IDetailConvention {
    return {
      ...restDetailConvention,
      dateCreation: restDetailConvention.dateCreation ? dayjs(restDetailConvention.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDetailConvention>): HttpResponse<IDetailConvention> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDetailConvention[]>): HttpResponse<IDetailConvention[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
