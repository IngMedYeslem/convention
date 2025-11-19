import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DetailConventionResolve from './route/detail-convention-routing-resolve.service';

const detailConventionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/detail-convention.component').then(m => m.DetailConventionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/detail-convention-detail.component').then(m => m.DetailConventionDetailComponent),
    resolve: {
      detailConvention: DetailConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/detail-convention-update.component').then(m => m.DetailConventionUpdateComponent),
    resolve: {
      detailConvention: DetailConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/detail-convention-update.component').then(m => m.DetailConventionUpdateComponent),
    resolve: {
      detailConvention: DetailConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default detailConventionRoute;
