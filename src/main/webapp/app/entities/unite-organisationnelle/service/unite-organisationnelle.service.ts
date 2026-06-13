import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IUniteOrganisationnelle, NewUniteOrganisationnelle } from '../unite-organisationnelle.model';

@Injectable({ providedIn: 'root' })
export class UniteOrganisationnelleService {
  private readonly http = inject(HttpClient);
  private readonly resourceUrl = '/api/unite-organisationnelles';

  findAll(): Observable<IUniteOrganisationnelle[]> {
    return this.http.get<IUniteOrganisationnelle[]>(this.resourceUrl);
  }

  find(id: number): Observable<IUniteOrganisationnelle> {
    return this.http.get<IUniteOrganisationnelle>(`${this.resourceUrl}/${id}`);
  }

  create(unite: NewUniteOrganisationnelle): Observable<IUniteOrganisationnelle> {
    return this.http.post<IUniteOrganisationnelle>(this.resourceUrl, unite);
  }

  update(id: number, unite: IUniteOrganisationnelle): Observable<IUniteOrganisationnelle> {
    return this.http.put<IUniteOrganisationnelle>(`${this.resourceUrl}/${id}`, unite);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
