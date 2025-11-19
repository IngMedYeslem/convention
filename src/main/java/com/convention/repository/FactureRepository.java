package com.convention.repository;

import com.convention.domain.FactureEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<FactureEntity, Long>, JpaSpecificationExecutor<FactureEntity> {}
