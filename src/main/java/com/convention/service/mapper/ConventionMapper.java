package com.convention.service.mapper;

import com.convention.domain.ClientEntity;
import com.convention.domain.ConventionEntity;
import com.convention.service.dto.ClientDTO;
import com.convention.service.dto.ConventionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConventionEntity} and its DTO {@link ConventionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConventionMapper extends EntityMapper<ConventionDTO, ConventionEntity> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    ConventionDTO toDto(ConventionEntity s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(ClientEntity clientEntity);
}
