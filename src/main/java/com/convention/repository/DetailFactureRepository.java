package com.convention.repository;

import com.convention.domain.DetailFactureEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DetailFactureEntity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailFactureRepository extends JpaRepository<DetailFactureEntity, Long> {
    List<DetailFactureEntity> findByFactureId(Long factureId);
    Page<DetailFactureEntity> findByFacture_Convention_CreatedByUnite_Id(Long uniteId, Pageable pageable);
    Page<DetailFactureEntity> findByFacture_Convention_CreatedByUnite_Parent_Id(Long parentId, Pageable pageable);
}
