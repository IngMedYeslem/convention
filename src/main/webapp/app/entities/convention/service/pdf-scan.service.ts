import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IConvention } from '../convention.model';

@Injectable({ providedIn: 'root' })
export class PdfScanService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pdf');

  scanPdf(file: File): Observable<HttpResponse<IConvention>> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<IConvention>(`${this.resourceUrl}/scan`, formData, { observe: 'response' });
  }
}
