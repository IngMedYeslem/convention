package com.convention.service.mapper;

import com.convention.domain.BudgetEngagementEntity;
import com.convention.domain.ConventionEntity;
import com.convention.service.dto.BudgetEngagementDTO;
import com.convention.service.dto.ConventionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BudgetEngagementMapper extends EntityMapper<BudgetEngagementDTO, BudgetEngagementEntity> {
    @Mapping(target = "convention", source = "convention", qualifiedByName = "conventionBudget")
    @Mapping(target = "soldeDisponible", expression = "java(s.getSoldeDisponible())")
    BudgetEngagementDTO toDto(BudgetEngagementEntity s);

    @Named("conventionBudget")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numConvention", source = "numConvention")
    ConventionDTO toDtoConventionBudget(ConventionEntity entity);
}
