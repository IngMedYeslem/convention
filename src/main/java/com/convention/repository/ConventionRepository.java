package com.convention.repository;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ConventionRepository extends JpaRepository<ConventionEntity, Long>, JpaSpecificationExecutor<ConventionEntity> {
    List<ConventionEntity> findByStatut(StatutConvention statut);

    Long countByStatut(StatutConvention statut);

    Long countByDateSignConvBetween(LocalDate startDate, LocalDate endDate);

    // Filter by exact unit (SERVICE)
    Page<ConventionEntity> findByCreatedByUniteId(Long uniteId, Pageable pageable);

    // Filter by parent unit (DEPARTEMENT sees all its SERVICE children)
    Page<ConventionEntity> findByCreatedByUniteParentId(Long parentUniteId, Pageable pageable);
}
