import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';

/**
 * Allows access if the user is ROLE_ADMIN OR has niveauHierarchique
 * in ['DEPARTEMENT', 'DIRECTION'].
 */
export const AdminOrHierarchyRouteAccessService: CanActivateFn = () => {
  const accountService = inject(AccountService);
  const router = inject(Router);

  return accountService.identity().pipe(
    map(account => {
      if (!account) {
        router.navigate(['/login']);
        return false;
      }

      const isAdmin = accountService.hasAnyAuthority('ROLE_ADMIN');
      const niveau = (account as any).niveauHierarchique as string | undefined;
      const isApprover = niveau === 'DEPARTEMENT' || niveau === 'DIRECTION';

      if (isAdmin || isApprover) return true;

      router.navigate(['/']);
      return false;
    }),
  );
};
