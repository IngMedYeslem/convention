package com.convention.web.rest;

import static com.convention.domain.DetailFactureEntityAsserts.*;
import static com.convention.web.rest.TestUtil.createUpdateProxyForBean;
import static com.convention.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.convention.IntegrationTest;
import com.convention.domain.DetailFactureEntity;
import com.convention.domain.FactureEntity;
import com.convention.repository.DetailFactureRepository;
import com.convention.service.dto.DetailFactureDTO;
import com.convention.service.mapper.DetailFactureMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DetailFactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailFactureResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(1);

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final BigDecimal DEFAULT_MONTANT_HT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_HT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAUX_TVA = new BigDecimal(0);
    private static final BigDecimal UPDATED_TAUX_TVA = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MONTANT_TVA = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TVA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MONTANT_TTC = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TTC = new BigDecimal(2);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/detail-factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DetailFactureRepository detailFactureRepository;

    @Autowired
    private DetailFactureMapper detailFactureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailFactureMockMvc;

    private DetailFactureEntity detailFactureEntity;

    private DetailFactureEntity insertedDetailFactureEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailFactureEntity createEntity(EntityManager em) {
        DetailFactureEntity detailFactureEntity = new DetailFactureEntity()
            .designation(DEFAULT_DESIGNATION)
            .prixUnitaire(DEFAULT_PRIX_UNITAIRE)
            .quantite(DEFAULT_QUANTITE)
            .montantHT(DEFAULT_MONTANT_HT)
            .tauxTVA(DEFAULT_TAUX_TVA)
            .montantTVA(DEFAULT_MONTANT_TVA)
            .montantTTC(DEFAULT_MONTANT_TTC)
            .observations(DEFAULT_OBSERVATIONS);
        // Add required entity
        FactureEntity facture;
        if (TestUtil.findAll(em, FactureEntity.class).isEmpty()) {
            facture = FactureResourceIT.createEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, FactureEntity.class).get(0);
        }
        detailFactureEntity.setFacture(facture);
        return detailFactureEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailFactureEntity createUpdatedEntity(EntityManager em) {
        DetailFactureEntity updatedDetailFactureEntity = new DetailFactureEntity()
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantHT(UPDATED_MONTANT_HT)
            .tauxTVA(UPDATED_TAUX_TVA)
            .montantTVA(UPDATED_MONTANT_TVA)
            .montantTTC(UPDATED_MONTANT_TTC)
            .observations(UPDATED_OBSERVATIONS);
        // Add required entity
        FactureEntity facture;
        if (TestUtil.findAll(em, FactureEntity.class).isEmpty()) {
            facture = FactureResourceIT.createUpdatedEntity(em);
            em.persist(facture);
            em.flush();
        } else {
            facture = TestUtil.findAll(em, FactureEntity.class).get(0);
        }
        updatedDetailFactureEntity.setFacture(facture);
        return updatedDetailFactureEntity;
    }

    @BeforeEach
    void initTest() {
        detailFactureEntity = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDetailFactureEntity != null) {
            detailFactureRepository.delete(insertedDetailFactureEntity);
            insertedDetailFactureEntity = null;
        }
    }

    @Test
    @Transactional
    void createDetailFacture() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);
        var returnedDetailFactureDTO = om.readValue(
            restDetailFactureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DetailFactureDTO.class
        );

        // Validate the DetailFacture in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDetailFactureEntity = detailFactureMapper.toEntity(returnedDetailFactureDTO);
        assertDetailFactureEntityUpdatableFieldsEquals(
            returnedDetailFactureEntity,
            getPersistedDetailFactureEntity(returnedDetailFactureEntity)
        );

        insertedDetailFactureEntity = returnedDetailFactureEntity;
    }

    @Test
    @Transactional
    void createDetailFactureWithExistingId() throws Exception {
        // Create the DetailFacture with an existing ID
        detailFactureEntity.setId(1L);
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailFactureEntity.setDesignation(null);

        // Create the DetailFacture, which fails.
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        restDetailFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrixUnitaireIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailFactureEntity.setPrixUnitaire(null);

        // Create the DetailFacture, which fails.
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        restDetailFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        detailFactureEntity.setQuantite(null);

        // Create the DetailFacture, which fails.
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        restDetailFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailFactures() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        // Get all the detailFactureList
        restDetailFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailFactureEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(sameNumber(DEFAULT_PRIX_UNITAIRE))))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].montantHT").value(hasItem(sameNumber(DEFAULT_MONTANT_HT))))
            .andExpect(jsonPath("$.[*].tauxTVA").value(hasItem(sameNumber(DEFAULT_TAUX_TVA))))
            .andExpect(jsonPath("$.[*].montantTVA").value(hasItem(sameNumber(DEFAULT_MONTANT_TVA))))
            .andExpect(jsonPath("$.[*].montantTTC").value(hasItem(sameNumber(DEFAULT_MONTANT_TTC))))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)));
    }

    @Test
    @Transactional
    void getDetailFacture() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        // Get the detailFacture
        restDetailFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, detailFactureEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailFactureEntity.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.prixUnitaire").value(sameNumber(DEFAULT_PRIX_UNITAIRE)))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.montantHT").value(sameNumber(DEFAULT_MONTANT_HT)))
            .andExpect(jsonPath("$.tauxTVA").value(sameNumber(DEFAULT_TAUX_TVA)))
            .andExpect(jsonPath("$.montantTVA").value(sameNumber(DEFAULT_MONTANT_TVA)))
            .andExpect(jsonPath("$.montantTTC").value(sameNumber(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS));
    }

    @Test
    @Transactional
    void getNonExistingDetailFacture() throws Exception {
        // Get the detailFacture
        restDetailFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDetailFacture() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailFacture
        DetailFactureEntity updatedDetailFactureEntity = detailFactureRepository.findById(detailFactureEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDetailFactureEntity are not directly saved in db
        em.detach(updatedDetailFactureEntity);
        updatedDetailFactureEntity
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantHT(UPDATED_MONTANT_HT)
            .tauxTVA(UPDATED_TAUX_TVA)
            .montantTVA(UPDATED_MONTANT_TVA)
            .montantTTC(UPDATED_MONTANT_TTC)
            .observations(UPDATED_OBSERVATIONS);
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(updatedDetailFactureEntity);

        restDetailFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailFactureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailFactureDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDetailFactureEntityToMatchAllProperties(updatedDetailFactureEntity);
    }

    @Test
    @Transactional
    void putNonExistingDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailFactureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(detailFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailFactureWithPatch() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailFacture using partial update
        DetailFactureEntity partialUpdatedDetailFactureEntity = new DetailFactureEntity();
        partialUpdatedDetailFactureEntity.setId(detailFactureEntity.getId());

        partialUpdatedDetailFactureEntity.designation(UPDATED_DESIGNATION).quantite(UPDATED_QUANTITE);

        restDetailFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailFactureEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetailFactureEntity))
            )
            .andExpect(status().isOk());

        // Validate the DetailFacture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetailFactureEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDetailFactureEntity, detailFactureEntity),
            getPersistedDetailFactureEntity(detailFactureEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateDetailFactureWithPatch() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the detailFacture using partial update
        DetailFactureEntity partialUpdatedDetailFactureEntity = new DetailFactureEntity();
        partialUpdatedDetailFactureEntity.setId(detailFactureEntity.getId());

        partialUpdatedDetailFactureEntity
            .designation(UPDATED_DESIGNATION)
            .prixUnitaire(UPDATED_PRIX_UNITAIRE)
            .quantite(UPDATED_QUANTITE)
            .montantHT(UPDATED_MONTANT_HT)
            .tauxTVA(UPDATED_TAUX_TVA)
            .montantTVA(UPDATED_MONTANT_TVA)
            .montantTTC(UPDATED_MONTANT_TTC)
            .observations(UPDATED_OBSERVATIONS);

        restDetailFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailFactureEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDetailFactureEntity))
            )
            .andExpect(status().isOk());

        // Validate the DetailFacture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDetailFactureEntityUpdatableFieldsEquals(
            partialUpdatedDetailFactureEntity,
            getPersistedDetailFactureEntity(partialUpdatedDetailFactureEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailFactureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detailFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(detailFactureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        detailFactureEntity.setId(longCount.incrementAndGet());

        // Create the DetailFacture
        DetailFactureDTO detailFactureDTO = detailFactureMapper.toDto(detailFactureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(detailFactureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailFacture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailFacture() throws Exception {
        // Initialize the database
        insertedDetailFactureEntity = detailFactureRepository.saveAndFlush(detailFactureEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the detailFacture
        restDetailFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailFactureEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return detailFactureRepository.count();
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

    protected DetailFactureEntity getPersistedDetailFactureEntity(DetailFactureEntity detailFacture) {
        return detailFactureRepository.findById(detailFacture.getId()).orElseThrow();
    }

    protected void assertPersistedDetailFactureEntityToMatchAllProperties(DetailFactureEntity expectedDetailFactureEntity) {
        assertDetailFactureEntityAllPropertiesEquals(
            expectedDetailFactureEntity,
            getPersistedDetailFactureEntity(expectedDetailFactureEntity)
        );
    }

    protected void assertPersistedDetailFactureEntityToMatchUpdatableProperties(DetailFactureEntity expectedDetailFactureEntity) {
        assertDetailFactureEntityAllUpdatablePropertiesEquals(
            expectedDetailFactureEntity,
            getPersistedDetailFactureEntity(expectedDetailFactureEntity)
        );
    }
}
