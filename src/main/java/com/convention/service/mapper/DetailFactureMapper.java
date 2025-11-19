package com.convention.service.mapper;

import com.convention.domain.DetailFactureEntity;
import com.convention.domain.FactureEntity;
import com.convention.service.dto.DetailFactureDTO;
import com.convention.service.dto.FactureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DetailFactureEntity} and its DTO {@link DetailFactureDTO}.
 */
@Mapper(componentModel = "spring")
public interface DetailFactureMapper extends EntityMapper<DetailFactureDTO, DetailFactureEntity> {
    @Mapping(target = "facture", source = "facture", qualifiedByName = "factureId")
    DetailFactureDTO toDto(DetailFactureEntity s);

    @Named("factureId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FactureDTO toDtoFactureId(FactureEntity factureEntity);
}
