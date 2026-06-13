package com.convention.service;

import com.convention.service.dto.UniteOrganisationnelleDTO;
import java.util.List;
import java.util.Optional;

public interface UniteOrganisationnelleService {
    UniteOrganisationnelleDTO save(UniteOrganisationnelleDTO dto);
    UniteOrganisationnelleDTO update(UniteOrganisationnelleDTO dto);
    Optional<UniteOrganisationnelleDTO> findOne(Long id);
    List<UniteOrganisationnelleDTO> findAll();
    void delete(Long id);
}
