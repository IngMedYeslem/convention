package com.convention.repository;

import com.convention.domain.DetailConventionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailConventionEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailConventionRepository extends JpaRepository<DetailConventionEntity, Long> {}
