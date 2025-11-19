package com.convention.repository;

import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutConvention;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConventionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConventionRepository extends JpaRepository<ConventionEntity, Long>, JpaSpecificationExecutor<ConventionEntity> {
    List<ConventionEntity> findByStatut(StatutConvention statut);

    Long countByStatut(StatutConvention statut);

    Long countByDateSignConvBetween(LocalDate startDate, LocalDate endDate);
}
