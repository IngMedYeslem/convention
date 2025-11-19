package com.convention.web.rest;

import com.convention.repository.DetailFactureRepository;
import com.convention.service.DetailFactureService;
import com.convention.service.dto.DetailFactureDTO;
import com.convention.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.convention.domain.DetailFactureEntity}.
 */
@RestController
@RequestMapping("/api/detail-factures")
public class DetailFactureResource {

    private static final Logger LOG = LoggerFactory.getLogger(DetailFactureResource.class);

    private static final String ENTITY_NAME = "detailFacture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailFactureService detailFactureService;

    private final DetailFactureRepository detailFactureRepository;

    public DetailFactureResource(DetailFactureService detailFactureService, DetailFactureRepository detailFactureRepository) {
        this.detailFactureService = detailFactureService;
        this.detailFactureRepository = detailFactureRepository;
    }

    /**
     * {@code POST  /detail-factures} : Create a new detailFacture.
     *
     * @param detailFactureDTO the detailFactureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailFactureDTO, or with status {@code 400 (Bad Request)} if the detailFacture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DetailFactureDTO> createDetailFacture(@Valid @RequestBody DetailFactureDTO detailFactureDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DetailFacture : {}", detailFactureDTO);
        if (detailFactureDTO.getId() != null) {
            throw new BadRequestAlertException("A new detailFacture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        detailFactureDTO = detailFactureService.save(detailFactureDTO);
        return ResponseEntity.created(new URI("/api/detail-factures/" + detailFactureDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, detailFactureDTO.getId().toString()))
            .body(detailFactureDTO);
    }

    /**
     * {@code PUT  /detail-factures/:id} : Updates an existing detailFacture.
     *
     * @param id the id of the detailFactureDTO to save.
     * @param detailFactureDTO the detailFactureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailFactureDTO,
     * or with status {@code 400 (Bad Request)} if the detailFactureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailFactureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DetailFactureDTO> updateDetailFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailFactureDTO detailFactureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DetailFacture : {}, {}", id, detailFactureDTO);
        if (detailFactureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailFactureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        detailFactureDTO = detailFactureService.update(detailFactureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailFactureDTO.getId().toString()))
            .body(detailFactureDTO);
    }

    /**
     * {@code PATCH  /detail-factures/:id} : Partial updates given fields of an existing detailFacture, field will ignore if it is null
     *
     * @param id the id of the detailFactureDTO to save.
     * @param detailFactureDTO the detailFactureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailFactureDTO,
     * or with status {@code 400 (Bad Request)} if the detailFactureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detailFactureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailFactureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailFactureDTO> partialUpdateDetailFacture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailFactureDTO detailFactureDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DetailFacture partially : {}, {}", id, detailFactureDTO);
        if (detailFactureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailFactureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailFactureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailFactureDTO> result = detailFactureService.partialUpdate(detailFactureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailFactureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-factures} : get all the detailFactures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailFactures in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DetailFactureDTO>> getAllDetailFactures(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DetailFactures");
        Page<DetailFactureDTO> page = detailFactureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-factures/:id} : get the "id" detailFacture.
     *
     * @param id the id of the detailFactureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailFactureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetailFactureDTO> getDetailFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DetailFacture : {}", id);
        Optional<DetailFactureDTO> detailFactureDTO = detailFactureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailFactureDTO);
    }

    /**
     * {@code DELETE  /detail-factures/:id} : delete the "id" detailFacture.
     *
     * @param id the id of the detailFactureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetailFacture(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DetailFacture : {}", id);
        detailFactureService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
