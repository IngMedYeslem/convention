package com.convention.service.impl;

import com.convention.domain.ConventionEntity;
import com.convention.repository.ClientRepository;
import com.convention.repository.ConventionRepository;
import com.convention.repository.UniteOrganisationnelleRepository;
import com.convention.repository.UserRepository;
import com.convention.security.SecurityUtils;
import com.convention.service.ConventionService;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.mapper.ConventionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConventionServiceImpl implements ConventionService {

    private static final Logger LOG = LoggerFactory.getLogger(ConventionServiceImpl.class);

    private final ConventionRepository conventionRepository;
    private final ClientRepository clientRepository;
    private final ConventionMapper conventionMapper;
    private final UserRepository userRepository;
    private final UniteOrganisationnelleRepository uniteRepository;

    public ConventionServiceImpl(
        ConventionRepository conventionRepository,
        ClientRepository clientRepository,
        ConventionMapper conventionMapper,
        UserRepository userRepository,
        UniteOrganisationnelleRepository uniteRepository
    ) {
        this.conventionRepository = conventionRepository;
        this.clientRepository = clientRepository;
        this.conventionMapper = conventionMapper;
        this.userRepository = userRepository;
        this.uniteRepository = uniteRepository;
    }

    @Override
    public ConventionDTO save(ConventionDTO conventionDTO) {
        LOG.debug("Request to save Convention : {}", conventionDTO);
        ConventionEntity conventionEntity = conventionMapper.toEntity(conventionDTO);
        if (conventionDTO.getClient() != null && conventionDTO.getClient().getId() != null) {
            conventionEntity.setClient(clientRepository.getReferenceById(conventionDTO.getClient().getId()));
        }
        // Auto-assign createdByUnite from the current user's unit (only on creation)
        if (conventionDTO.getId() == null) {
            SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneWithAuthoritiesByLogin)
                .map(u -> u.getUniteOrg())
                .ifPresent(conventionEntity::setCreatedByUnite);
        } else if (conventionDTO.getCreatedByUniteId() != null) {
            uniteRepository.findById(conventionDTO.getCreatedByUniteId()).ifPresent(conventionEntity::setCreatedByUnite);
        }
        conventionEntity = conventionRepository.save(conventionEntity);
        return conventionMapper.toDto(conventionEntity);
    }

    @Override
    public ConventionDTO update(ConventionDTO conventionDTO) {
        LOG.debug("Request to update Convention : {}", conventionDTO);
        // Load existing entity first so that createdByUnite (and other managed refs) are preserved
        return conventionRepository
            .findById(conventionDTO.getId())
            .map(existing -> {
                conventionMapper.partialUpdate(existing, conventionDTO);
                if (conventionDTO.getClient() != null && conventionDTO.getClient().getId() != null) {
                    existing.setClient(clientRepository.getReferenceById(conventionDTO.getClient().getId()));
                }
                // createdByUnite intentionally NOT overwritten — preserved from DB
                return conventionRepository.save(existing);
            })
            .map(conventionMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Convention introuvable: " + conventionDTO.getId()));
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
