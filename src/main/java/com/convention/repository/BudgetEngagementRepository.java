package com.convention.repository;

import com.convention.domain.BudgetEngagementEntity;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetEngagementRepository extends JpaRepository<BudgetEngagementEntity, Long> {
    List<BudgetEngagementEntity> findByConventionIdOrderByAnneeBudgetaireDesc(Long conventionId);

    List<BudgetEngagementEntity> findByAnneeBudgetaire(Integer annee);

    @Query("SELECT SUM(b.montantAutorise) FROM BudgetEngagementEntity b WHERE b.convention.id = :conventionId")
    java.math.BigDecimal sumMontantAutoriseByConventionId(Long conventionId);

    @Query("SELECT SUM(b.montantConsomme) FROM BudgetEngagementEntity b WHERE b.convention.id = :conventionId")
    java.math.BigDecimal sumMontantConsommeByConventionId(Long conventionId);
}
