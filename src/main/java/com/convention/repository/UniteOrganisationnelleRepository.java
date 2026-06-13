package com.convention.repository;

import com.convention.domain.UniteOrganisationnelleEntity;
import com.convention.domain.enumeration.NiveauHierarchique;
import java.util.List;
import org.springframework.data.jpa.repository.*;

public interface UniteOrganisationnelleRepository extends JpaRepository<UniteOrganisationnelleEntity, Long> {
    List<UniteOrganisationnelleEntity> findByNiveau(NiveauHierarchique niveau);

    List<UniteOrganisationnelleEntity> findByParentId(Long parentId);
}
