import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';

/**
 * Route guard that restricts access based on niveauHierarchique.
 * Usage: canActivate: [HierarchyRouteAccessService]
 *        data: { niveaux: ['DEPARTEMENT', 'DIRECTION'] }
 */
export const HierarchyRouteAccessService: CanActivateFn = (next: ActivatedRouteSnapshot) => {
  const accountService = inject(AccountService);
  const router = inject(Router);

  return accountService.identity().pipe(
    map(account => {
      if (!account) {
        router.navigate(['/login']);
        return false;
      }

      const allowed: string[] = next.data['niveaux'] ?? [];
      const niveau = (account as any).niveauHierarchique as string | undefined;

      if (allowed.length === 0 || (niveau && allowed.includes(niveau))) {
        return true;
      }

      router.navigate(['/']);
      return false;
    }),
  );
};
