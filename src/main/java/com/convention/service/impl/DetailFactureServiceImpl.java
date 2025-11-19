package com.convention.service.impl;

import com.convention.domain.DetailFactureEntity;
import com.convention.repository.DetailFactureRepository;
import com.convention.service.DetailFactureService;
import com.convention.service.dto.DetailFactureDTO;
import com.convention.service.mapper.DetailFactureMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.convention.domain.DetailFactureEntity}.
 */
@Service
@Transactional
public class DetailFactureServiceImpl implements DetailFactureService {

    private static final Logger LOG = LoggerFactory.getLogger(DetailFactureServiceImpl.class);

    private final DetailFactureRepository detailFactureRepository;

    private final DetailFactureMapper detailFactureMapper;

    public DetailFactureServiceImpl(DetailFactureRepository detailFactureRepository, DetailFactureMapper detailFactureMapper) {
        this.detailFactureRepository = detailFactureRepository;
        this.detailFactureMapper = detailFactureMapper;
    }

    @Override
    public DetailFactureDTO save(DetailFactureDTO detailFactureDTO) {
        LOG.debug("Request to save DetailFacture : {}", detailFactureDTO);
        DetailFactureEntity detailFactureEntity = detailFactureMapper.toEntity(detailFactureDTO);
        detailFactureEntity = detailFactureRepository.save(detailFactureEntity);
        return detailFactureMapper.toDto(detailFactureEntity);
    }

    @Override
    public DetailFactureDTO update(DetailFactureDTO detailFactureDTO) {
        LOG.debug("Request to update DetailFacture : {}", detailFactureDTO);
        DetailFactureEntity detailFactureEntity = detailFactureMapper.toEntity(detailFactureDTO);
        detailFactureEntity = detailFactureRepository.save(detailFactureEntity);
        return detailFactureMapper.toDto(detailFactureEntity);
    }

    @Override
    public Optional<DetailFactureDTO> partialUpdate(DetailFactureDTO detailFactureDTO) {
        LOG.debug("Request to partially update DetailFacture : {}", detailFactureDTO);

        return detailFactureRepository
            .findById(detailFactureDTO.getId())
            .map(existingDetailFacture -> {
                detailFactureMapper.partialUpdate(existingDetailFacture, detailFactureDTO);

                return existingDetailFacture;
            })
            .map(detailFactureRepository::save)
            .map(detailFactureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetailFactureDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DetailFactures");
        return detailFactureRepository.findAll(pageable).map(detailFactureMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetailFactureDTO> findOne(Long id) {
        LOG.debug("Request to get DetailFacture : {}", id);
        return detailFactureRepository.findById(id).map(detailFactureMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DetailFacture : {}", id);
        detailFactureRepository.deleteById(id);
    }
}
