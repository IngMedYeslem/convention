package com.convention.repository;

import com.convention.domain.DetailFactureEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailFactureEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailFactureRepository extends JpaRepository<DetailFactureEntity, Long> {}
