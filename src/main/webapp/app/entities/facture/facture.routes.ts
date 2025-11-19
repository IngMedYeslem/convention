import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FactureResolve from './route/facture-routing-resolve.service';

const factureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/facture.component').then(m => m.FactureComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/facture-detail.component').then(m => m.FactureDetailComponent),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/facture-update.component').then(m => m.FactureUpdateComponent),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/facture-update.component').then(m => m.FactureUpdateComponent),
    resolve: {
      facture: FactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default factureRoute;
