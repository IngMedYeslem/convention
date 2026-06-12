package com.convention.service;

import com.convention.domain.AvenantEntity;
import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.StatutAvenant;
import com.convention.repository.AvenantRepository;
import com.convention.repository.ConventionRepository;
import com.convention.service.dto.AvenantDTO;
import com.convention.service.mapper.AvenantMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AvenantService {

    private static final Logger LOG = LoggerFactory.getLogger(AvenantService.class);

    private final AvenantRepository avenantRepository;
    private final ConventionRepository conventionRepository;
    private final AvenantMapper avenantMapper;

    public AvenantService(AvenantRepository avenantRepository, ConventionRepository conventionRepository, AvenantMapper avenantMapper) {
        this.avenantRepository = avenantRepository;
        this.conventionRepository = conventionRepository;
        this.avenantMapper = avenantMapper;
    }

    public AvenantDTO save(AvenantDTO dto) {
        LOG.debug("Saving avenant: {}", dto);
        AvenantEntity entity = avenantMapper.toEntity(dto);
        if (entity.getDateCreation() == null) {
            entity.setDateCreation(Instant.now());
        }
        if (entity.getNumeroAvenant() == null) {
            Long conventionId = dto.getConvention().getId();
            Integer maxNum = avenantRepository.findMaxNumeroByConventionId(conventionId).orElse(0);
            entity.setNumeroAvenant(maxNum + 1);
        }
        entity = avenantRepository.save(entity);
        return avenantMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<AvenantDTO> findByConvention(Long conventionId) {
        return avenantRepository.findByConventionIdOrderByNumeroAvenantAsc(conventionId).stream().map(avenantMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<AvenantDTO> findOne(Long id) {
        return avenantRepository.findById(id).map(avenantMapper::toDto);
    }

    public AvenantDTO signer(Long id) {
        AvenantEntity avenant = avenantRepository.findById(id).orElseThrow(() -> new RuntimeException("Avenant introuvable: " + id));
        avenant.setStatut(StatutAvenant.SIGNE);

        // Sauvegarde convention si nécessaire
        if (avenant.getConvention() != null) {
            conventionRepository.save(avenant.getConvention());
        }

        return avenantMapper.toDto(avenantRepository.save(avenant));
    }

    public void delete(Long id) {
        avenantRepository.deleteById(id);
    }
}
