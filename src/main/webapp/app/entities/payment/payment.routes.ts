import { Routes } from '@angular/router';

const paymentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/payment.component').then(m => m.PaymentComponent),
    data: {},
  },
  {
    path: 'new',
    loadComponent: () => import('./update/payment-update.component').then(m => m.PaymentUpdateComponent),
    data: {},
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/payment-detail.component').then(m => m.PaymentDetailComponent),
    data: {},
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/payment-update.component').then(m => m.PaymentUpdateComponent),
    data: {},
  },
];

export default paymentRoute;
