import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ConventionResolve from './route/convention-routing-resolve.service';

const conventionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/convention.component').then(m => m.ConventionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/convention-detail.component').then(m => m.ConventionDetailComponent),
    resolve: {
      convention: ConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/convention-update.component').then(m => m.ConventionUpdateComponent),
    resolve: {
      convention: ConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'scan',
    loadComponent: () => import('./scan/convention-scan.component').then(m => m.ConventionScanComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/convention-update.component').then(m => m.ConventionUpdateComponent),
    resolve: {
      convention: ConventionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default conventionRoute;
