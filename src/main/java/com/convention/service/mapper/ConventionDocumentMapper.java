package com.convention.service.mapper;

import com.convention.domain.ConventionDocumentEntity;
import com.convention.domain.ConventionEntity;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.dto.ConventionDocumentDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConventionDocumentMapper extends EntityMapper<ConventionDocumentDTO, ConventionDocumentEntity> {
    @Mapping(target = "convention", source = "convention", qualifiedByName = "conventionRef")
    ConventionDocumentDTO toDto(ConventionDocumentEntity s);

    @Named("conventionRef")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numConvention", source = "numConvention")
    ConventionDTO toDtoConventionRef(ConventionEntity entity);
}
