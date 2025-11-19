package com.convention.web.rest;

import static com.convention.domain.DetailConventionEntityAsserts.*;
import static com.convention.web.rest.TestUtil.createUpdateProxyForBean;
import static com.convention.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.convention.IntegrationTest;
import com.convention.domain.ConventionEntity;
import com.convention.domain.DetailConventionEntity;
import com.convention.repository.DetailConventionRepository;
import com.convention.service.dto.DetailConventionDTO;
import com.convention.service.mapper.DetailConventionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DetailConventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailConventionResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(1);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final BigDecimal DEFAULT_MONTANT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TOTAL = new BigDecimal(2);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/detail-conventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DetailConventionRepository detailConventionRepository;

    @Autowired
    private DetailConventionMapper detailConventionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailConventionMockMvc;

    private DetailConventionEntity detailConventionEntity;

    private DetailConventionEntity insertedDetailConventionEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailConventionEntity createEntity(EntityManager em) {
        DetailConventionEntity detailConventionEntity = new DetailConventionEntity()
            .designation(DEFAULT_DESIGNATION)
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE)
            .montantTotal(DEFAULT_MONTANT_TOTAL)
            .observations(DEFAULT_OBSERVATIONS)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        ConventionEntity convention;
        if (TestUtil.findAll(em, ConventionEntity.class).isEmpty()) {
            convention = ConventionResourceIT.createEntity(em);
            em.persist(convention);
            em.flush();
        } else {
            convention = TestUtil.findAll(em, ConventionEntity.class).get(0);
        }
        detailConventionEntity.setConvention(convention);
        return detailConventionEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailConventionEntity createUpdatedEntity(EntityManager em) {
        DetailConventionEntity updatedDetailConventionEntity = new DetailConventionEntity()
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .observations(UPDATED_OBSERVATIONS)
            .dateCreation(UPDATED_DATE_CREATION);
        // Add required entity
        ConventionEntity convention;
        if (TestUtil.findAll(em, ConventionEntity.class).isEmpty()) {
            convention = ConventionResourceIT.createUpdatedEntity(em);
            em.persist(convention);
            em.flush();
        } else {
            convention = TestUtil.findAll(em, ConventionEntity.class).get(0);
        }
        updatedDetailConventionEntity.setConvention(convention);
        return updatedDetailConventionEntity;
    }

    @BeforeEach
    void initTest() {
        detailConventionEntity = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDetailConventionEntity != null) {
            detailConventionRepository.delete(insertedDetailConventionEntity);
            insertedDetailConventionEntity = null;
        }
    }

    @Test
    @Transactional
    void createDetailConvention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);
        var returnedDetailConventionDTO = om.readValue(
            restDetailConventionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DetailConventionDTO.class
        );

        // Validate the DetailConvention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDetailConventionEntity = detailConventionMapper.toEntity(returnedDetailConventionDTO);
        assertDetailConventionEntityUpdatableFieldsEquals(
            returnedDetailConventionEntity,
            getPersistedDetailConventionEntity(returnedDetailConventionEntity)
        );

        insertedDetailConventionEntity = returnedDetailConventionEntity;
    }

    @Test
    @Transactional
    void createDetailConventionWithExistingId() throws Exception {
        // Create the DetailConvention with an existing ID
        detailConventionEntity.setId(1L);
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailConventionEntity.setDesignation(null);

        // Create the DetailConvention, which fails.
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        restDetailConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixUnitaireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailConventionEntity.setPrixUnitaire(null);

        // Create the DetailConvention, which fails.
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        restDetailConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailConventionEntity.setQuantite(null);

        // Create the DetailConvention, which fails.
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        restDetailConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailConventions() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        // Get all the detailConventionList
        restDetailConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailConventionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(sameNumber(DEFAULT_PRIX_UNITAIRE))))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montantTotal").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTAL))))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getDetailConvention() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        // Get the detailConvention
        restDetailConventionMockMvc
            .perform(get(ENTITY_API_URL_ID, detailConventionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailConventionEntity.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.prixUnitaire").value(sameNumber(DEFAULT_PRIX_UNITAIRE)))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montantTotal").value(sameNumber(DEFAULT_MONTANT_TOTAL)))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDetailConvention() throws Exception {
        // Get the detailConvention
        restDetailConventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetailConvention() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailConvention
        DetailConventionEntity updatedDetailConventionEntity = detailConventionRepository
            .findById(detailConventionEntity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDetailConventionEntity are not directly saved in db
        em.detach(updatedDetailConventionEntity);
        updatedDetailConventionEntity
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .observations(UPDATED_OBSERVATIONS)
            .dateCreation(UPDATED_DATE_CREATION);
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(updatedDetailConventionEntity);

        restDetailConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailConventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailConventionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDetailConventionEntityToMatchAllProperties(updatedDetailConventionEntity);
    }

    @Test
    @Transactional
    void putNonExistingDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailConventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailConventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailConventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailConventionWithPatch() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailConvention using partial update
        DetailConventionEntity partialUpdatedDetailConventionEntity = new DetailConventionEntity();
        partialUpdatedDetailConventionEntity.setId(detailConventionEntity.getId());

        partialUpdatedDetailConventionEntity.prixUnitaire(UPDATED_PRIX_UNITAIRE).montantTotal(UPDATED_MONTANT_TOTAL);

        restDetailConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailConventionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetailConventionEntity))
            )
            .andExpect(status().isOk());

        // Validate the DetailConvention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetailConventionEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDetailConventionEntity, detailConventionEntity),
            getPersistedDetailConventionEntity(detailConventionEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateDetailConventionWithPatch() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailConvention using partial update
        DetailConventionEntity partialUpdatedDetailConventionEntity = new DetailConventionEntity();
        partialUpdatedDetailConventionEntity.setId(detailConventionEntity.getId());

        partialUpdatedDetailConventionEntity
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .observations(UPDATED_OBSERVATIONS)
            .dateCreation(UPDATED_DATE_CREATION);

        restDetailConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailConventionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetailConventionEntity))
            )
            .andExpect(status().isOk());

        // Validate the DetailConvention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetailConventionEntityUpdatableFieldsEquals(
            partialUpdatedDetailConventionEntity,
            getPersistedDetailConventionEntity(partialUpdatedDetailConventionEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailConventionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detailConventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detailConventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailConventionEntity.setId(longCount.incrementAndGet());

        // Create the DetailConvention
        DetailConventionDTO detailConventionDTO = detailConventionMapper.toDto(detailConventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailConventionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(detailConventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailConvention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailConvention() throws Exception {
        // Initialize the database
        insertedDetailConventionEntity = detailConventionRepository.saveAndFlush(detailConventionEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the detailConvention
        restDetailConventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailConventionEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return detailConventionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DetailConventionEntity getPersistedDetailConventionEntity(DetailConventionEntity detailConvention) {
        return detailConventionRepository.findById(detailConvention.getId()).orElseThrow();
    }

    protected void assertPersistedDetailConventionEntityToMatchAllProperties(DetailConventionEntity expectedDetailConventionEntity) {
        assertDetailConventionEntityAllPropertiesEquals(
            expectedDetailConventionEntity,
            getPersistedDetailConventionEntity(expectedDetailConventionEntity)
        );
    }

    protected void assertPersistedDetailConventionEntityToMatchUpdatableProperties(DetailConventionEntity expectedDetailConventionEntity) {
        assertDetailConventionEntityAllUpdatablePropertiesEquals(
            expectedDetailConventionEntity,
            getPersistedDetailConventionEntity(expectedDetailConventionEntity)
        );
    }
}
