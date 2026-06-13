package com.convention.repository;

import com.convention.domain.FactureEntity;
import com.convention.domain.enumeration.StatutFacture;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FactureEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FactureRepository extends JpaRepository<FactureEntity, Long>, JpaSpecificationExecutor<FactureEntity> {
    @Query("SELECT COALESCE(SUM(f.montantTTC), 0) FROM FactureEntity f WHERE f.statut = :statut")
    BigDecimal sumMontantTTCByStatut(@Param("statut") StatutFacture statut);

    @Query(
        "SELECT COALESCE(SUM(f.montantTTC), 0) FROM FactureEntity f WHERE f.dateFacture BETWEEN :startDate AND :endDate AND f.statut = :statut"
    )
    BigDecimal sumMontantTTCByDateFactureBetweenAndStatut(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("statut") StatutFacture statut
    );

    Page<FactureEntity> findByConvention_CreatedByUnite_Id(Long uniteId, Pageable pageable);
    Page<FactureEntity> findByConvention_CreatedByUnite_Parent_Id(Long parentId, Pageable pageable);
}
