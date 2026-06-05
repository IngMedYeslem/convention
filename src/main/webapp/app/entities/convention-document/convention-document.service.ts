import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export interface IConventionDocument {
  id: number;
  nomFichier?: string | null;
  typeDocument?: string | null;
  contentType?: string | null;
  tailleFichier?: number | null;
  dateDepot?: string | null;
  deposePar?: string | null;
  observations?: string | null;
}

@Injectable({ providedIn: 'root' })
export class ConventionDocumentService {
  protected conventionResourceUrl = this.config.getEndpointFor('api/conventions');
  protected documentResourceUrl = this.config.getEndpointFor('api/documents');

  constructor(
    protected http: HttpClient,
    protected config: ApplicationConfigService,
  ) {}

  upload(conventionId: number, file: File, typeDocument: string, observations?: string): Observable<IConventionDocument> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('typeDocument', typeDocument);
    if (observations) formData.append('observations', observations);
    return this.http.post<IConventionDocument>(`${this.conventionResourceUrl}/${conventionId}/documents`, formData);
  }

  findByConvention(conventionId: number): Observable<IConventionDocument[]> {
    return this.http.get<IConventionDocument[]>(`${this.conventionResourceUrl}/${conventionId}/documents`);
  }

  getDownloadUrl(id: number): string {
    return this.config.getEndpointFor(`api/documents/${id}/download`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.documentResourceUrl}/${id}`);
  }
}
