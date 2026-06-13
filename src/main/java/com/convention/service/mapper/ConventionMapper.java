package com.convention.service.mapper;

import com.convention.domain.ClientEntity;
import com.convention.domain.ConventionEntity;
import com.convention.service.dto.ClientDTO;
import com.convention.service.dto.ConventionDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ConventionMapper extends EntityMapper<ConventionDTO, ConventionEntity> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "createdByUniteId", source = "createdByUnite.id")
    @Mapping(target = "createdByUniteNom", source = "createdByUnite.nom")
    ConventionDTO toDto(ConventionEntity s);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "createdByUnite", ignore = true)
    ConventionEntity toEntity(ConventionDTO dto);

    @Override
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "createdByUnite", ignore = true)
    void partialUpdate(@MappingTarget ConventionEntity entity, ConventionDTO dto);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomClient", source = "nomClient")
    ClientDTO toDtoClientId(ClientEntity clientEntity);
}
