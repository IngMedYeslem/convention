package com.convention.repository;

import com.convention.domain.ConventionDocumentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ConventionDocumentRepository extends JpaRepository<ConventionDocumentEntity, Long> {
    List<ConventionDocumentEntity> findByConventionIdOrderByDateDepotDesc(Long conventionId);

    void deleteByConventionId(Long conventionId);
}
