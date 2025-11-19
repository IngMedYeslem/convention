package com.convention.service.mapper;

import com.convention.domain.ClientEntity;
import com.convention.domain.ConventionEntity;
import com.convention.domain.FactureEntity;
import com.convention.service.dto.ClientDTO;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.dto.FactureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FactureEntity} and its DTO {@link FactureDTO}.
 */
@Mapper(componentModel = "spring")
public interface FactureMapper extends EntityMapper<FactureDTO, FactureEntity> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "convention", source = "convention", qualifiedByName = "conventionId")
    FactureDTO toDto(FactureEntity s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(ClientEntity clientEntity);

    @Named("conventionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConventionDTO toDtoConventionId(ConventionEntity conventionEntity);
}
