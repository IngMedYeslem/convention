import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HierarchyRouteAccessService } from 'app/core/auth/hierarchy-route-access.service';
import { AdminOrHierarchyRouteAccessService } from 'app/core/auth/admin-or-hierarchy-route-access.service';
import { errorRoute } from './layouts/error/error.route';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: 'admin',
    canActivate: [AdminOrHierarchyRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [HierarchyRouteAccessService],
    data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] },
    title: 'Tableau de bord',
  },
  {
    path: 'statistiques',
    loadComponent: () => import('./statistiques/statistiques.component').then(m => m.StatistiquesComponent),
    canActivate: [HierarchyRouteAccessService],
    data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] },
    title: 'Statistiques',
  },
  {
    path: 'facture/:id/imprimer',
    loadComponent: () => import('./entities/facture/print/facture-imprimer.component').then(m => m.FactureImprimerComponent),
    title: 'Imprimer la facture',
  },
  {
    path: 'facture/:id/lettre-paiement',
    loadComponent: () => import('./entities/facture/print/facture-lettre-paiement.component').then(m => m.FactureLettreComponent),
    title: 'Lettre de paiement',
  },
  {
    path: 'rapport/factures-par-client',
    loadComponent: () => import('./rapport/rapport-factures-client.component').then(m => m.RapportFacturesClientComponent),
    canActivate: [HierarchyRouteAccessService],
    data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] },
    title: 'Rapport — Factures par client',
  },
  {
    path: 'rapport/conventions-par-client',
    loadComponent: () => import('./rapport/rapport-conventions-client.component').then(m => m.RapportConventionsClientComponent),
    canActivate: [HierarchyRouteAccessService],
    data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] },
    title: 'Rapport — Conventions par client',
  },
  {
    path: 'rapport/etat-facturation',
    loadComponent: () => import('./rapport/rapport-etat-facturation.component').then(m => m.RapportEtatFacturationComponent),
    canActivate: [HierarchyRouteAccessService],
    data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] },
    title: 'État général de contrôle facturation',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
