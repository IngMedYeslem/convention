import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DetailFactureResolve from './route/detail-facture-routing-resolve.service';

const detailFactureRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/detail-facture.component').then(m => m.DetailFactureComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/detail-facture-detail.component').then(m => m.DetailFactureDetailComponent),
    resolve: {
      detailFacture: DetailFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/detail-facture-update.component').then(m => m.DetailFactureUpdateComponent),
    resolve: {
      detailFacture: DetailFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/detail-facture-update.component').then(m => m.DetailFactureUpdateComponent),
    resolve: {
      detailFacture: DetailFactureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default detailFactureRoute;
