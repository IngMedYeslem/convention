package com.convention.service;

import com.convention.domain.enumeration.NiveauHierarchique;
import com.convention.repository.UserRepository;
import com.convention.security.SecurityUtils;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Provides data-scoping helpers so each REST resource applies the same
 * SERVICE / DEPARTEMENT / DIRECTION isolation without duplicating logic.
 */
@Service
public class DataScopeService {

    private final UserRepository userRepository;

    public DataScopeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns a scoped page, or {@code null} if the current user has no unit
     * restriction (DIRECTION or admin → caller should use the default query).
     *
     * @param pageable      the pagination info
     * @param serviceScope  query for SERVICE  (uniteId, pageable) → Page
     * @param deptScope     query for DEPARTEMENT (parentId, pageable) → Page
     */
    public <T> Page<T> scopedPage(
        Pageable pageable,
        BiFunction<Long, Pageable, Page<T>> serviceScope,
        BiFunction<Long, Pageable, Page<T>> deptScope
    ) {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .filter(u -> u.getUniteOrg() != null)
            .map(u -> {
                NiveauHierarchique niveau = u.getUniteOrg().getNiveau();
                if (niveau == NiveauHierarchique.SERVICE) {
                    return serviceScope.apply(u.getUniteOrg().getId(), pageable);
                }
                if (niveau == NiveauHierarchique.DEPARTEMENT) {
                    return deptScope.apply(u.getUniteOrg().getId(), pageable);
                }
                return (Page<T>) null; // DIRECTION → no restriction
            })
            .orElse(null); // no unit → no restriction
    }

    /** Convenience overload that accepts a fallback and always returns a page. */
    public <T> Page<T> scopedPage(
        Pageable pageable,
        BiFunction<Long, Pageable, Page<T>> serviceScope,
        BiFunction<Long, Pageable, Page<T>> deptScope,
        Function<Pageable, Page<T>> globalScope
    ) {
        Page<T> scoped = scopedPage(pageable, serviceScope, deptScope);
        return scoped != null ? scoped : globalScope.apply(pageable);
    }
}
