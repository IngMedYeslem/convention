package com.convention.service.mapper;

import com.convention.domain.ClientEntity;
import com.convention.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClientEntity} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, ClientEntity> {}
