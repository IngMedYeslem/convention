import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/unite-organisationnelle.component').then(m => m.UniteOrganisationnelleComponent),
  },
  {
    path: 'new',
    loadComponent: () => import('./update/unite-organisationnelle-update.component').then(m => m.UniteOrganisationnelleUpdateComponent),
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/unite-organisationnelle-update.component').then(m => m.UniteOrganisationnelleUpdateComponent),
  },
];

export default routes;
