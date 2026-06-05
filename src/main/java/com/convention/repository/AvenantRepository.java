package com.convention.repository;

import com.convention.domain.AvenantEntity;
import com.convention.domain.enumeration.StatutAvenant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface AvenantRepository extends JpaRepository<AvenantEntity, Long> {
    List<AvenantEntity> findByConventionIdOrderByNumeroAvenantAsc(Long conventionId);

    List<AvenantEntity> findByStatut(StatutAvenant statut);

    Optional<Integer> findTopNumeroAvenantByConventionId(Long conventionId);

    @Query("SELECT MAX(a.numeroAvenant) FROM AvenantEntity a WHERE a.convention.id = :conventionId")
    Optional<Integer> findMaxNumeroByConventionId(Long conventionId);
}
