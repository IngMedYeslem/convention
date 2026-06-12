package com.convention.service.impl;

import com.convention.domain.DetailConventionEntity;
import com.convention.repository.ConventionRepository;
import com.convention.repository.DetailConventionRepository;
import com.convention.service.DetailConventionService;
import com.convention.service.dto.DetailConventionDTO;
import com.convention.service.mapper.DetailConventionMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.convention.domain.DetailConventionEntity}.
 */
@Service
@Transactional
public class DetailConventionServiceImpl implements DetailConventionService {

    private static final Logger LOG = LoggerFactory.getLogger(DetailConventionServiceImpl.class);

    private final DetailConventionRepository detailConventionRepository;
    private final ConventionRepository conventionRepository;
    private final DetailConventionMapper detailConventionMapper;

    public DetailConventionServiceImpl(
        DetailConventionRepository detailConventionRepository,
        ConventionRepository conventionRepository,
        DetailConventionMapper detailConventionMapper
    ) {
        this.detailConventionRepository = detailConventionRepository;
        this.conventionRepository = conventionRepository;
        this.detailConventionMapper = detailConventionMapper;
    }

    @Override
    public DetailConventionDTO save(DetailConventionDTO detailConventionDTO) {
        LOG.debug("Request to save DetailConvention : {}", detailConventionDTO);
        DetailConventionEntity detailConventionEntity = detailConventionMapper.toEntity(detailConventionDTO);
        if (detailConventionDTO.getConvention() != null && detailConventionDTO.getConvention().getId() != null) {
            detailConventionEntity.setConvention(conventionRepository.getReferenceById(detailConventionDTO.getConvention().getId()));
        }
        detailConventionEntity = detailConventionRepository.save(detailConventionEntity);
        return detailConventionMapper.toDto(detailConventionEntity);
    }

    @Override
    public DetailConventionDTO update(DetailConventionDTO detailConventionDTO) {
        LOG.debug("Request to update DetailConvention : {}", detailConventionDTO);
        DetailConventionEntity detailConventionEntity = detailConventionMapper.toEntity(detailConventionDTO);
        if (detailConventionDTO.getConvention() != null && detailConventionDTO.getConvention().getId() != null) {
            detailConventionEntity.setConvention(conventionRepository.getReferenceById(detailConventionDTO.getConvention().getId()));
        }
        detailConventionEntity = detailConventionRepository.save(detailConventionEntity);
        return detailConventionMapper.toDto(detailConventionEntity);
    }

    @Override
    public Optional<DetailConventionDTO> partialUpdate(DetailConventionDTO detailConventionDTO) {
        LOG.debug("Request to partially update DetailConvention : {}", detailConventionDTO);

        return detailConventionRepository
            .findById(detailConventionDTO.getId())
            .map(existingDetailConvention -> {
                detailConventionMapper.partialUpdate(existingDetailConvention, detailConventionDTO);

                return existingDetailConvention;
            })
            .map(detailConventionRepository::save)
            .map(detailConventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetailConventionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DetailConventions");
        return detailConventionRepository.findAll(pageable).map(detailConventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetailConventionDTO> findByConventionId(Long conventionId) {
        LOG.debug("Request to get DetailConventions for convention : {}", conventionId);
        return detailConventionRepository.findByConventionId(conventionId).stream().map(detailConventionMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetailConventionDTO> findOne(Long id) {
        LOG.debug("Request to get DetailConvention : {}", id);
        return detailConventionRepository.findById(id).map(detailConventionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DetailConvention : {}", id);
        detailConventionRepository.deleteById(id);
    }
}
