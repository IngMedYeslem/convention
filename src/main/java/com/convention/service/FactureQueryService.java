package com.convention.service;

import com.convention.domain.*; // for static metamodels
import com.convention.domain.FactureEntity;
import com.convention.repository.FactureRepository;
import com.convention.service.criteria.FactureCriteria;
import com.convention.service.dto.FactureDTO;
import com.convention.service.mapper.FactureMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FactureEntity} entities in the database.
 * The main input is a {@link FactureCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FactureDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FactureQueryService extends QueryService<FactureEntity> {

    private static final Logger LOG = LoggerFactory.getLogger(FactureQueryService.class);

    private final FactureRepository factureRepository;

    private final FactureMapper factureMapper;

    public FactureQueryService(FactureRepository factureRepository, FactureMapper factureMapper) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
    }

    /**
     * Return a {@link Page} of {@link FactureDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FactureDTO> findByCriteria(FactureCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FactureEntity> specification = createSpecification(criteria);
        return factureRepository.findAll(specification, page).map(factureMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FactureCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FactureEntity> specification = createSpecification(criteria);
        return factureRepository.count(specification);
    }

    /**
     * Function to convert {@link FactureCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FactureEntity> createSpecification(FactureCriteria criteria) {
        Specification<FactureEntity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), FactureEntity_.id),
                buildRangeSpecification(criteria.getNumFacture(), FactureEntity_.numFacture),
                buildRangeSpecification(criteria.getDateFacture(), FactureEntity_.dateFacture),
                buildRangeSpecification(criteria.getMontantTotal(), FactureEntity_.montantTotal),
                buildRangeSpecification(criteria.getMontantTTC(), FactureEntity_.montantTTC),
                buildRangeSpecification(criteria.getTva(), FactureEntity_.tva),
                buildStringSpecification(criteria.getAncienneRef(), FactureEntity_.ancienneRef),
                buildSpecification(criteria.getTypeFacture(), FactureEntity_.typeFacture),
                buildSpecification(criteria.getStatut(), FactureEntity_.statut),
                buildRangeSpecification(criteria.getDateEcheance(), FactureEntity_.dateEcheance),
                buildRangeSpecification(criteria.getDateCreation(), FactureEntity_.dateCreation),
                buildSpecification(criteria.getClientId(), root -> root.join(FactureEntity_.client, JoinType.LEFT).get(ClientEntity_.id)),
                buildSpecification(criteria.getConventionId(), root ->
                    root.join(FactureEntity_.convention, JoinType.LEFT).get(ConventionEntity_.id)
                )
            );
        }
        return specification;
    }
}
