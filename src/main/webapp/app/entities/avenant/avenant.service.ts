import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IAvenant, NewAvenant } from './avenant.model';

@Injectable({ providedIn: 'root' })
export class AvenantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/avenants');
  protected conventionResourceUrl = this.applicationConfigService.getEndpointFor('api/conventions');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(avenant: NewAvenant): Observable<IAvenant> {
    return this.http.post<IAvenant>(this.resourceUrl, avenant);
  }

  update(avenant: IAvenant): Observable<IAvenant> {
    return this.http.put<IAvenant>(`${this.resourceUrl}/${avenant.id}`, avenant);
  }

  find(id: number): Observable<IAvenant> {
    return this.http.get<IAvenant>(`${this.resourceUrl}/${id}`);
  }

  findByConvention(conventionId: number): Observable<IAvenant[]> {
    return this.http.get<IAvenant[]>(`${this.conventionResourceUrl}/${conventionId}/avenants`);
  }

  signer(id: number): Observable<IAvenant> {
    return this.http.post<IAvenant>(`${this.resourceUrl}/${id}/signer`, {});
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourceUrl}/${id}`);
  }
}
