package com.convention.service;

import com.convention.service.dto.DetailConventionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.convention.domain.DetailConventionEntity}.
 */
public interface DetailConventionService {
    /**
     * Save a detailConvention.
     *
     * @param detailConventionDTO the entity to save.
     * @return the persisted entity.
     */
    DetailConventionDTO save(DetailConventionDTO detailConventionDTO);

    /**
     * Updates a detailConvention.
     *
     * @param detailConventionDTO the entity to update.
     * @return the persisted entity.
     */
    DetailConventionDTO update(DetailConventionDTO detailConventionDTO);

    /**
     * Partially updates a detailConvention.
     *
     * @param detailConventionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DetailConventionDTO> partialUpdate(DetailConventionDTO detailConventionDTO);

    /**
     * Get all the detailConventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DetailConventionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" detailConvention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetailConventionDTO> findOne(Long id);

    /**
     * Delete the "id" detailConvention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
