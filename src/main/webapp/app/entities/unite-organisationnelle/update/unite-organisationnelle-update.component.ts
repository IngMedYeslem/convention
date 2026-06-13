import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import SharedModule from 'app/shared/shared.module';
import { IUniteOrganisationnelle, NewUniteOrganisationnelle } from '../unite-organisationnelle.model';
import { UniteOrganisationnelleService } from '../service/unite-organisationnelle.service';
import { NiveauHierarchique } from 'app/entities/enumerations/niveau-hierarchique.model';

@Component({
  selector: 'jhi-unite-organisationnelle-update',
  templateUrl: './unite-organisationnelle-update.component.html',
  imports: [RouterModule, FormsModule, SharedModule],
})
export class UniteOrganisationnelleUpdateComponent implements OnInit {
  isSaving = false;
  niveaux = Object.keys(NiveauHierarchique) as (keyof typeof NiveauHierarchique)[];
  allUnites: IUniteOrganisationnelle[] = [];

  unite: IUniteOrganisationnelle | NewUniteOrganisationnelle = {
    id: null,
    nom: '',
    code: '',
    niveau: 'SERVICE',
    parentId: null,
  };

  private service = inject(UniteOrganisationnelleService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ngOnInit(): void {
    this.service.findAll().subscribe(all => (this.allUnites = all));

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.service.find(Number(id)).subscribe(u => (this.unite = u));
    }
  }

  niveauLabel(niveau: string): string {
    switch (niveau) {
      case 'SERVICE':
        return 'مصلحة (Service)';
      case 'DEPARTEMENT':
        return 'قطاع (Département)';
      case 'DIRECTION':
        return 'إدارة (Administration)';
      default:
        return niveau;
    }
  }

  parentUnites(): IUniteOrganisationnelle[] {
    if (this.unite.niveau === 'SERVICE') return this.allUnites.filter(u => u.niveau === 'DEPARTEMENT');
    if (this.unite.niveau === 'DEPARTEMENT') return this.allUnites.filter(u => u.niveau === 'DIRECTION');
    return [];
  }

  save(): void {
    this.isSaving = true;
    if (this.unite.id) {
      this.service.update(this.unite.id, this.unite as IUniteOrganisationnelle).subscribe({
        next: () => this.onSaved(),
        error: () => {
          this.isSaving = false;
        },
      });
    } else {
      this.service.create(this.unite as NewUniteOrganisationnelle).subscribe({
        next: () => this.onSaved(),
        error: () => {
          this.isSaving = false;
        },
      });
    }
  }

  private onSaved(): void {
    this.isSaving = false;
    this.router.navigate(['/unite-organisationnelle']);
  }
}
