package com.convention.service;

import com.convention.service.dto.DetailFactureDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.convention.domain.DetailFactureEntity}.
 */
public interface DetailFactureService {
    /**
     * Save a detailFacture.
     *
     * @param detailFactureDTO the entity to save.
     * @return the persisted entity.
     */
    DetailFactureDTO save(DetailFactureDTO detailFactureDTO);

    /**
     * Updates a detailFacture.
     *
     * @param detailFactureDTO the entity to update.
     * @return the persisted entity.
     */
    DetailFactureDTO update(DetailFactureDTO detailFactureDTO);

    /**
     * Partially updates a detailFacture.
     *
     * @param detailFactureDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DetailFactureDTO> partialUpdate(DetailFactureDTO detailFactureDTO);

    /**
     * Get all the detailFactures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DetailFactureDTO> findAll(Pageable pageable);

    /**
     * Get the "id" detailFacture.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetailFactureDTO> findOne(Long id);

    /**
     * Delete the "id" detailFacture.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
