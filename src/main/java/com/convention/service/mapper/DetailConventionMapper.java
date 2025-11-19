package com.convention.service.mapper;

import com.convention.domain.ConventionEntity;
import com.convention.domain.DetailConventionEntity;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.dto.DetailConventionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetailConventionEntity} and its DTO {@link DetailConventionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetailConventionMapper extends EntityMapper<DetailConventionDTO, DetailConventionEntity> {
    @Mapping(target = "convention", source = "convention", qualifiedByName = "conventionId")
    DetailConventionDTO toDto(DetailConventionEntity s);

    @Named("conventionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConventionDTO toDtoConventionId(ConventionEntity conventionEntity);
}
