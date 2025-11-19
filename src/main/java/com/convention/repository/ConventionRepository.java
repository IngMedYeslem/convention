package com.convention.repository;

import com.convention.domain.ConventionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConventionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConventionRepository extends JpaRepository<ConventionEntity, Long>, JpaSpecificationExecutor<ConventionEntity> {}
