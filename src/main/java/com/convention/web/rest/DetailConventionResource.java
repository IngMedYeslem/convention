package com.convention.web.rest;

import com.convention.repository.DetailConventionRepository;
import com.convention.service.DetailConventionService;
import com.convention.service.dto.DetailConventionDTO;
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
 * REST controller for managing {@link com.convention.domain.DetailConventionEntity}.
 */
@RestController
@RequestMapping("/api/detail-conventions")
public class DetailConventionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DetailConventionResource.class);

    private static final String ENTITY_NAME = "detailConvention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailConventionService detailConventionService;

    private final DetailConventionRepository detailConventionRepository;

    public DetailConventionResource(
        DetailConventionService detailConventionService,
        DetailConventionRepository detailConventionRepository
    ) {
        this.detailConventionService = detailConventionService;
        this.detailConventionRepository = detailConventionRepository;
    }

    /**
     * {@code POST  /detail-conventions} : Create a new detailConvention.
     *
     * @param detailConventionDTO the detailConventionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailConventionDTO, or with status {@code 400 (Bad Request)} if the detailConvention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DetailConventionDTO> createDetailConvention(@Valid @RequestBody DetailConventionDTO detailConventionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DetailConvention : {}", detailConventionDTO);
        if (detailConventionDTO.getId() != null) {
            throw new BadRequestAlertException("A new detailConvention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        detailConventionDTO = detailConventionService.save(detailConventionDTO);
        return ResponseEntity.created(new URI("/api/detail-conventions/" + detailConventionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, detailConventionDTO.getId().toString()))
            .body(detailConventionDTO);
    }

    /**
     * {@code PUT  /detail-conventions/:id} : Updates an existing detailConvention.
     *
     * @param id the id of the detailConventionDTO to save.
     * @param detailConventionDTO the detailConventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailConventionDTO,
     * or with status {@code 400 (Bad Request)} if the detailConventionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailConventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DetailConventionDTO> updateDetailConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailConventionDTO detailConventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DetailConvention : {}, {}", id, detailConventionDTO);
        if (detailConventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailConventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailConventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        detailConventionDTO = detailConventionService.update(detailConventionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailConventionDTO.getId().toString()))
            .body(detailConventionDTO);
    }

    /**
     * {@code PATCH  /detail-conventions/:id} : Partial updates given fields of an existing detailConvention, field will ignore if it is null
     *
     * @param id the id of the detailConventionDTO to save.
     * @param detailConventionDTO the detailConventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailConventionDTO,
     * or with status {@code 400 (Bad Request)} if the detailConventionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the detailConventionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailConventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailConventionDTO> partialUpdateDetailConvention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailConventionDTO detailConventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DetailConvention partially : {}, {}", id, detailConventionDTO);
        if (detailConventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailConventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailConventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailConventionDTO> result = detailConventionService.partialUpdate(detailConventionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailConventionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-conventions/by-convention/:conventionId} : get all detailConventions for a convention.
     */
    @GetMapping("/by-convention/{conventionId}")
    public ResponseEntity<List<DetailConventionDTO>> getDetailConventionsByConvention(@PathVariable Long conventionId) {
        LOG.debug("REST request to get DetailConventions for convention : {}", conventionId);
        return ResponseEntity.ok(detailConventionService.findByConventionId(conventionId));
    }

    /**
     * {@code GET  /detail-conventions} : get all the detailConventions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailConventions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DetailConventionDTO>> getAllDetailConventions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DetailConventions");
        Page<DetailConventionDTO> page = detailConventionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detail-conventions/:id} : get the "id" detailConvention.
     *
     * @param id the id of the detailConventionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailConventionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DetailConventionDTO> getDetailConvention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DetailConvention : {}", id);
        Optional<DetailConventionDTO> detailConventionDTO = detailConventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailConventionDTO);
    }

    /**
     * {@code DELETE  /detail-conventions/:id} : delete the "id" detailConvention.
     *
     * @param id the id of the detailConventionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetailConvention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DetailConvention : {}", id);
        detailConventionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
