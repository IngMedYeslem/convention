package com.convention.repository;

import com.convention.domain.DetailConventionEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface DetailConventionRepository extends JpaRepository<DetailConventionEntity, Long> {
    List<DetailConventionEntity> findByConventionId(Long conventionId);
    Page<DetailConventionEntity> findByConvention_CreatedByUnite_Id(Long uniteId, Pageable pageable);
    Page<DetailConventionEntity> findByConvention_CreatedByUnite_Parent_Id(Long parentId, Pageable pageable);
}
