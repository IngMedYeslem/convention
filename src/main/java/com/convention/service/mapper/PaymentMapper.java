package com.convention.service.mapper;

import com.convention.domain.PaymentEntity;
import com.convention.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PaymentEntity} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = { FactureMapper.class })
public interface PaymentMapper extends EntityMapper<PaymentDTO, PaymentEntity> {}
