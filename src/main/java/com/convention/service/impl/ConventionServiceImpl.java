package com.convention.service.impl;

import com.convention.domain.ConventionEntity;
import com.convention.repository.ClientRepository;
import com.convention.repository.ConventionRepository;
import com.convention.service.ConventionService;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.mapper.ConventionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.convention.domain.ConventionEntity}.
 */
@Service
@Transactional
public class ConventionServiceImpl implements ConventionService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionServiceImpl.class);

    private final ConventionRepository conventionRepository;
    private final ClientRepository clientRepository;
    private final ConventionMapper conventionMapper;

    public ConventionServiceImpl(
        ConventionRepository conventionRepository,
        ClientRepository clientRepository,
        ConventionMapper conventionMapper
    ) {
        this.conventionRepository = conventionRepository;
        this.clientRepository = clientRepository;
        this.conventionMapper = conventionMapper;
    }

    @Override
    public ConventionDTO save(ConventionDTO conventionDTO) {
        LOG.debug("Request to save Convention : {}", conventionDTO);
        ConventionEntity conventionEntity = conventionMapper.toEntity(conventionDTO);
        if (conventionDTO.getClient() != null && conventionDTO.getClient().getId() != null) {
            conventionEntity.setClient(clientRepository.getReferenceById(conventionDTO.getClient().getId()));
        }
        conventionEntity = conventionRepository.save(conventionEntity);
        return conventionMapper.toDto(conventionEntity);
    }

    @Override
    public ConventionDTO update(ConventionDTO conventionDTO) {
        LOG.debug("Request to update Convention : {}", conventionDTO);
        ConventionEntity conventionEntity = conventionMapper.toEntity(conventionDTO);
        if (conventionDTO.getClient() != null && conventionDTO.getClient().getId() != null) {
            conventionEntity.setClient(clientRepository.getReferenceById(conventionDTO.getClient().getId()));
        }
        conventionEntity = conventionRepository.save(conventionEntity);
        return conventionMapper.toDto(conventionEntity);
    }

    @Override
    public Optional<ConventionDTO> partialUpdate(ConventionDTO conventionDTO) {
        LOG.debug("Request to partially update Convention : {}", conventionDTO);

        return conventionRepository
            .findById(conventionDTO.getId())
            .map(existingConvention -> {
                conventionMapper.partialUpdate(existingConvention, conventionDTO);

                return existingConvention;
            })
            .map(conventionRepository::save)
            .map(conventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConventionDTO> findOne(Long id) {
        LOG.debug("Request to get Convention : {}", id);
        return conventionRepository.findById(id).map(conventionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Convention : {}", id);
        conventionRepository.deleteById(id);
    }
}
