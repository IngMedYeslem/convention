import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'conventionApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'conventionApp.client.home.title' },
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'convention',
    data: { pageTitle: 'conventionApp.convention.home.title' },
    loadChildren: () => import('./convention/convention.routes'),
  },
  {
    path: 'detail-convention',
    data: { pageTitle: 'conventionApp.detailConvention.home.title' },
    loadChildren: () => import('./detail-convention/detail-convention.routes'),
  },
  {
    path: 'facture',
    data: { pageTitle: 'conventionApp.facture.home.title' },
    loadChildren: () => import('./facture/facture.routes'),
  },
  {
    path: 'detail-facture',
    data: { pageTitle: 'conventionApp.detailFacture.home.title' },
    loadChildren: () => import('./detail-facture/detail-facture.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
