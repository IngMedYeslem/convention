package com.convention.repository;

import com.convention.domain.PaymentEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findByFactureId(Long factureId);
    List<PaymentEntity> findByFactureConventionId(Long conventionId);
    Page<PaymentEntity> findByFacture_Convention_CreatedByUnite_Id(Long uniteId, Pageable pageable);
    Page<PaymentEntity> findByFacture_Convention_CreatedByUnite_Parent_Id(Long parentId, Pageable pageable);
}
