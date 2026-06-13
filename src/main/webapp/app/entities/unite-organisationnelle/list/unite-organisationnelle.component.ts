import { Component, OnInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import SharedModule from 'app/shared/shared.module';
import { IUniteOrganisationnelle } from '../unite-organisationnelle.model';
import { UniteOrganisationnelleService } from '../service/unite-organisationnelle.service';

@Component({
  selector: 'jhi-unite-organisationnelle',
  templateUrl: './unite-organisationnelle.component.html',
  imports: [RouterModule, SharedModule],
})
export class UniteOrganisationnelleComponent implements OnInit {
  unites: IUniteOrganisationnelle[] = [];
  isLoading = false;

  private service = inject(UniteOrganisationnelleService);
  private modalService = inject(NgbModal);

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.service.findAll().subscribe({
      next: data => {
        this.unites = data;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  delete(unite: IUniteOrganisationnelle): void {
    if (confirm(`Supprimer l'unité « ${unite.nom} » ?`)) {
      this.service.delete(unite.id).subscribe(() => this.load());
    }
  }

  niveauLabel(niveau: string | null | undefined): string {
    switch (niveau) {
      case 'SERVICE':
        return 'مصلحة (Service)';
      case 'DEPARTEMENT':
        return 'قطاع (Département)';
      case 'DIRECTION':
        return 'إدارة (Administration)';
      default:
        return niveau ?? '—';
    }
  }
}
