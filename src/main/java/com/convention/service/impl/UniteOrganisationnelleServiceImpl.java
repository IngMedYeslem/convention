package com.convention.service.impl;

import com.convention.domain.UniteOrganisationnelleEntity;
import com.convention.repository.UniteOrganisationnelleRepository;
import com.convention.service.UniteOrganisationnelleService;
import com.convention.service.dto.UniteOrganisationnelleDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UniteOrganisationnelleServiceImpl implements UniteOrganisationnelleService {

    private final UniteOrganisationnelleRepository repository;

    public UniteOrganisationnelleServiceImpl(UniteOrganisationnelleRepository repository) {
        this.repository = repository;
    }

    @Override
    public UniteOrganisationnelleDTO save(UniteOrganisationnelleDTO dto) {
        UniteOrganisationnelleEntity entity = toEntity(dto);
        return toDto(repository.save(entity));
    }

    @Override
    public UniteOrganisationnelleDTO update(UniteOrganisationnelleDTO dto) {
        UniteOrganisationnelleEntity entity = toEntity(dto);
        return toDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniteOrganisationnelleDTO> findOne(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UniteOrganisationnelleDTO> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private UniteOrganisationnelleEntity toEntity(UniteOrganisationnelleDTO dto) {
        UniteOrganisationnelleEntity e = new UniteOrganisationnelleEntity();
        e.setId(dto.getId());
        e.setNom(dto.getNom());
        e.setCode(dto.getCode());
        e.setNiveau(dto.getNiveau());
        if (dto.getParentId() != null) {
            e.setParent(repository.getReferenceById(dto.getParentId()));
        }
        return e;
    }

    public UniteOrganisationnelleDTO toDto(UniteOrganisationnelleEntity e) {
        UniteOrganisationnelleDTO dto = new UniteOrganisationnelleDTO();
        dto.setId(e.getId());
        dto.setNom(e.getNom());
        dto.setCode(e.getCode());
        dto.setNiveau(e.getNiveau());
        if (e.getParent() != null) {
            dto.setParentId(e.getParent().getId());
            dto.setParentNom(e.getParent().getNom());
        }
        return dto;
    }
}
