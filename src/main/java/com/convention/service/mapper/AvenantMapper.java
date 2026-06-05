package com.convention.service.mapper;

import com.convention.domain.AvenantEntity;
import com.convention.domain.ConventionEntity;
import com.convention.service.dto.AvenantDTO;
import com.convention.service.dto.ConventionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AvenantMapper extends EntityMapper<AvenantDTO, AvenantEntity> {
    @Mapping(target = "convention", source = "convention", qualifiedByName = "conventionId")
    AvenantDTO toDto(AvenantEntity s);

    @Named("conventionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numConvention", source = "numConvention")
    @Mapping(target = "statut", source = "statut")
    ConventionDTO toDtoConventionId(ConventionEntity entity);
}
