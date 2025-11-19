package com.convention.service;

import com.convention.service.dto.ConventionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.convention.domain.ConventionEntity}.
 */
public interface ConventionService {
    /**
     * Save a convention.
     *
     * @param conventionDTO the entity to save.
     * @return the persisted entity.
     */
    ConventionDTO save(ConventionDTO conventionDTO);

    /**
     * Updates a convention.
     *
     * @param conventionDTO the entity to update.
     * @return the persisted entity.
     */
    ConventionDTO update(ConventionDTO conventionDTO);

    /**
     * Partially updates a convention.
     *
     * @param conventionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConventionDTO> partialUpdate(ConventionDTO conventionDTO);

    /**
     * Get the "id" convention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConventionDTO> findOne(Long id);

    /**
     * Delete the "id" convention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
