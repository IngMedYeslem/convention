package com.convention.web.rest;

import static com.convention.domain.FactureEntityAsserts.*;
import static com.convention.web.rest.TestUtil.createUpdateProxyForBean;
import static com.convention.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.convention.IntegrationTest;
import com.convention.domain.ClientEntity;
import com.convention.domain.ConventionEntity;
import com.convention.domain.FactureEntity;
import com.convention.domain.enumeration.StatutFacture;
import com.convention.domain.enumeration.TypeFacture;
import com.convention.repository.FactureRepository;
import com.convention.service.dto.FactureDTO;
import com.convention.service.mapper.FactureMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link FactureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactureResourceIT {

    private static final Long DEFAULT_NUM_FACTURE = 1L;
    private static final Long UPDATED_NUM_FACTURE = 2L;
    private static final Long SMALLER_NUM_FACTURE = 1L - 1L;

    private static final LocalDate DEFAULT_DATE_FACTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FACTURE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FACTURE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_MONTANT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANT_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_MONTANT_TOTAL = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_MONTANT_TTC = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT_TTC = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTANT_TTC = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TVA = new BigDecimal(0);
    private static final BigDecimal UPDATED_TVA = new BigDecimal(1);
    private static final BigDecimal SMALLER_TVA = new BigDecimal(0 - 1);

    private static final String DEFAULT_OBSERVATIONS = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ANCIENNE_REF = "AAAAAAAAAA";
    private static final String UPDATED_ANCIENNE_REF = "BBBBBBBBBB";

    private static final TypeFacture DEFAULT_TYPE_FACTURE = TypeFacture.NORMALE;
    private static final TypeFacture UPDATED_TYPE_FACTURE = TypeFacture.AVOIR;

    private static final StatutFacture DEFAULT_STATUT = StatutFacture.BROUILLON;
    private static final StatutFacture UPDATED_STATUT = StatutFacture.EMISE;

    private static final LocalDate DEFAULT_DATE_ECHEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ECHEANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ECHEANCE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/factures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private FactureMapper factureMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFactureMockMvc;

    private FactureEntity factureEntity;

    private FactureEntity insertedFactureEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureEntity createEntity(EntityManager em) {
        FactureEntity factureEntity = new FactureEntity()
            .numFacture(DEFAULT_NUM_FACTURE)
            .dateFacture(DEFAULT_DATE_FACTURE)
            .montantTotal(DEFAULT_MONTANT_TOTAL)
            .montantTTC(DEFAULT_MONTANT_TTC)
            .tva(DEFAULT_TVA)
            .observations(DEFAULT_OBSERVATIONS)
            .ancienneRef(DEFAULT_ANCIENNE_REF)
            .typeFacture(DEFAULT_TYPE_FACTURE)
            .statut(DEFAULT_STATUT)
            .dateEcheance(DEFAULT_DATE_ECHEANCE)
            .dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        factureEntity.setClient(client);
        return factureEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactureEntity createUpdatedEntity(EntityManager em) {
        FactureEntity updatedFactureEntity = new FactureEntity()
            .numFacture(UPDATED_NUM_FACTURE)
            .dateFacture(UPDATED_DATE_FACTURE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .montantTTC(UPDATED_MONTANT_TTC)
            .tva(UPDATED_TVA)
            .observations(UPDATED_OBSERVATIONS)
            .ancienneRef(UPDATED_ANCIENNE_REF)
            .typeFacture(UPDATED_TYPE_FACTURE)
            .statut(UPDATED_STATUT)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .dateCreation(UPDATED_DATE_CREATION);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        updatedFactureEntity.setClient(client);
        return updatedFactureEntity;
    }

    @BeforeEach
    void initTest() {
        factureEntity = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedFactureEntity != null) {
            factureRepository.delete(insertedFactureEntity);
            insertedFactureEntity = null;
        }
    }

    @Test
    @Transactional
    void createFacture() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);
        var returnedFactureDTO = om.readValue(
            restFactureMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactureDTO.class
        );

        // Validate the Facture in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFactureEntity = factureMapper.toEntity(returnedFactureDTO);
        assertFactureEntityUpdatableFieldsEquals(returnedFactureEntity, getPersistedFactureEntity(returnedFactureEntity));

        insertedFactureEntity = returnedFactureEntity;
    }

    @Test
    @Transactional
    void createFactureWithExistingId() throws Exception {
        // Create the Facture with an existing ID
        factureEntity.setId(1L);
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumFactureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factureEntity.setNumFacture(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFactureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factureEntity.setDateFacture(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factureEntity.setMontantTotal(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeFactureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factureEntity.setTypeFacture(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        factureEntity.setStatut(null);

        // Create the Facture, which fails.
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        restFactureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFactures() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factureEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numFacture").value(hasItem(DEFAULT_NUM_FACTURE.intValue())))
            .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].montantTotal").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTAL))))
            .andExpect(jsonPath("$.[*].montantTTC").value(hasItem(sameNumber(DEFAULT_MONTANT_TTC))))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(sameNumber(DEFAULT_TVA))))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)))
            .andExpect(jsonPath("$.[*].ancienneRef").value(hasItem(DEFAULT_ANCIENNE_REF)))
            .andExpect(jsonPath("$.[*].typeFacture").value(hasItem(DEFAULT_TYPE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @Test
    @Transactional
    void getFacture() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get the facture
        restFactureMockMvc
            .perform(get(ENTITY_API_URL_ID, factureEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factureEntity.getId().intValue()))
            .andExpect(jsonPath("$.numFacture").value(DEFAULT_NUM_FACTURE.intValue()))
            .andExpect(jsonPath("$.dateFacture").value(DEFAULT_DATE_FACTURE.toString()))
            .andExpect(jsonPath("$.montantTotal").value(sameNumber(DEFAULT_MONTANT_TOTAL)))
            .andExpect(jsonPath("$.montantTTC").value(sameNumber(DEFAULT_MONTANT_TTC)))
            .andExpect(jsonPath("$.tva").value(sameNumber(DEFAULT_TVA)))
            .andExpect(jsonPath("$.observations").value(DEFAULT_OBSERVATIONS))
            .andExpect(jsonPath("$.ancienneRef").value(DEFAULT_ANCIENNE_REF))
            .andExpect(jsonPath("$.typeFacture").value(DEFAULT_TYPE_FACTURE.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateEcheance").value(DEFAULT_DATE_ECHEANCE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getFacturesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        Long id = factureEntity.getId();

        defaultFactureFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFactureFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFactureFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture equals to
        defaultFactureFiltering("numFacture.equals=" + DEFAULT_NUM_FACTURE, "numFacture.equals=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture in
        defaultFactureFiltering("numFacture.in=" + DEFAULT_NUM_FACTURE + "," + UPDATED_NUM_FACTURE, "numFacture.in=" + UPDATED_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture is not null
        defaultFactureFiltering("numFacture.specified=true", "numFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture is greater than or equal to
        defaultFactureFiltering(
            "numFacture.greaterThanOrEqual=" + DEFAULT_NUM_FACTURE,
            "numFacture.greaterThanOrEqual=" + UPDATED_NUM_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture is less than or equal to
        defaultFactureFiltering("numFacture.lessThanOrEqual=" + DEFAULT_NUM_FACTURE, "numFacture.lessThanOrEqual=" + SMALLER_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture is less than
        defaultFactureFiltering("numFacture.lessThan=" + UPDATED_NUM_FACTURE, "numFacture.lessThan=" + DEFAULT_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByNumFactureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where numFacture is greater than
        defaultFactureFiltering("numFacture.greaterThan=" + SMALLER_NUM_FACTURE, "numFacture.greaterThan=" + DEFAULT_NUM_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture equals to
        defaultFactureFiltering("dateFacture.equals=" + DEFAULT_DATE_FACTURE, "dateFacture.equals=" + UPDATED_DATE_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture in
        defaultFactureFiltering(
            "dateFacture.in=" + DEFAULT_DATE_FACTURE + "," + UPDATED_DATE_FACTURE,
            "dateFacture.in=" + UPDATED_DATE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture is not null
        defaultFactureFiltering("dateFacture.specified=true", "dateFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture is greater than or equal to
        defaultFactureFiltering(
            "dateFacture.greaterThanOrEqual=" + DEFAULT_DATE_FACTURE,
            "dateFacture.greaterThanOrEqual=" + UPDATED_DATE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture is less than or equal to
        defaultFactureFiltering(
            "dateFacture.lessThanOrEqual=" + DEFAULT_DATE_FACTURE,
            "dateFacture.lessThanOrEqual=" + SMALLER_DATE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture is less than
        defaultFactureFiltering("dateFacture.lessThan=" + UPDATED_DATE_FACTURE, "dateFacture.lessThan=" + DEFAULT_DATE_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateFactureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateFacture is greater than
        defaultFactureFiltering("dateFacture.greaterThan=" + SMALLER_DATE_FACTURE, "dateFacture.greaterThan=" + DEFAULT_DATE_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal equals to
        defaultFactureFiltering("montantTotal.equals=" + DEFAULT_MONTANT_TOTAL, "montantTotal.equals=" + UPDATED_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal in
        defaultFactureFiltering(
            "montantTotal.in=" + DEFAULT_MONTANT_TOTAL + "," + UPDATED_MONTANT_TOTAL,
            "montantTotal.in=" + UPDATED_MONTANT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal is not null
        defaultFactureFiltering("montantTotal.specified=true", "montantTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal is greater than or equal to
        defaultFactureFiltering(
            "montantTotal.greaterThanOrEqual=" + DEFAULT_MONTANT_TOTAL,
            "montantTotal.greaterThanOrEqual=" + UPDATED_MONTANT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal is less than or equal to
        defaultFactureFiltering(
            "montantTotal.lessThanOrEqual=" + DEFAULT_MONTANT_TOTAL,
            "montantTotal.lessThanOrEqual=" + SMALLER_MONTANT_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal is less than
        defaultFactureFiltering("montantTotal.lessThan=" + UPDATED_MONTANT_TOTAL, "montantTotal.lessThan=" + DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTotal is greater than
        defaultFactureFiltering("montantTotal.greaterThan=" + SMALLER_MONTANT_TOTAL, "montantTotal.greaterThan=" + DEFAULT_MONTANT_TOTAL);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC equals to
        defaultFactureFiltering("montantTTC.equals=" + DEFAULT_MONTANT_TTC, "montantTTC.equals=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC in
        defaultFactureFiltering("montantTTC.in=" + DEFAULT_MONTANT_TTC + "," + UPDATED_MONTANT_TTC, "montantTTC.in=" + UPDATED_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC is not null
        defaultFactureFiltering("montantTTC.specified=true", "montantTTC.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC is greater than or equal to
        defaultFactureFiltering(
            "montantTTC.greaterThanOrEqual=" + DEFAULT_MONTANT_TTC,
            "montantTTC.greaterThanOrEqual=" + UPDATED_MONTANT_TTC
        );
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC is less than or equal to
        defaultFactureFiltering("montantTTC.lessThanOrEqual=" + DEFAULT_MONTANT_TTC, "montantTTC.lessThanOrEqual=" + SMALLER_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC is less than
        defaultFactureFiltering("montantTTC.lessThan=" + UPDATED_MONTANT_TTC, "montantTTC.lessThan=" + DEFAULT_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByMontantTTCIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where montantTTC is greater than
        defaultFactureFiltering("montantTTC.greaterThan=" + SMALLER_MONTANT_TTC, "montantTTC.greaterThan=" + DEFAULT_MONTANT_TTC);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva equals to
        defaultFactureFiltering("tva.equals=" + DEFAULT_TVA, "tva.equals=" + UPDATED_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva in
        defaultFactureFiltering("tva.in=" + DEFAULT_TVA + "," + UPDATED_TVA, "tva.in=" + UPDATED_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva is not null
        defaultFactureFiltering("tva.specified=true", "tva.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva is greater than or equal to
        defaultFactureFiltering("tva.greaterThanOrEqual=" + DEFAULT_TVA, "tva.greaterThanOrEqual=" + (DEFAULT_TVA.add(BigDecimal.ONE)));
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva is less than or equal to
        defaultFactureFiltering("tva.lessThanOrEqual=" + DEFAULT_TVA, "tva.lessThanOrEqual=" + SMALLER_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva is less than
        defaultFactureFiltering("tva.lessThan=" + (DEFAULT_TVA.add(BigDecimal.ONE)), "tva.lessThan=" + DEFAULT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByTvaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where tva is greater than
        defaultFactureFiltering("tva.greaterThan=" + SMALLER_TVA, "tva.greaterThan=" + DEFAULT_TVA);
    }

    @Test
    @Transactional
    void getAllFacturesByAncienneRefIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where ancienneRef equals to
        defaultFactureFiltering("ancienneRef.equals=" + DEFAULT_ANCIENNE_REF, "ancienneRef.equals=" + UPDATED_ANCIENNE_REF);
    }

    @Test
    @Transactional
    void getAllFacturesByAncienneRefIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where ancienneRef in
        defaultFactureFiltering(
            "ancienneRef.in=" + DEFAULT_ANCIENNE_REF + "," + UPDATED_ANCIENNE_REF,
            "ancienneRef.in=" + UPDATED_ANCIENNE_REF
        );
    }

    @Test
    @Transactional
    void getAllFacturesByAncienneRefIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where ancienneRef is not null
        defaultFactureFiltering("ancienneRef.specified=true", "ancienneRef.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByAncienneRefContainsSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where ancienneRef contains
        defaultFactureFiltering("ancienneRef.contains=" + DEFAULT_ANCIENNE_REF, "ancienneRef.contains=" + UPDATED_ANCIENNE_REF);
    }

    @Test
    @Transactional
    void getAllFacturesByAncienneRefNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where ancienneRef does not contain
        defaultFactureFiltering("ancienneRef.doesNotContain=" + UPDATED_ANCIENNE_REF, "ancienneRef.doesNotContain=" + DEFAULT_ANCIENNE_REF);
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where typeFacture equals to
        defaultFactureFiltering("typeFacture.equals=" + DEFAULT_TYPE_FACTURE, "typeFacture.equals=" + UPDATED_TYPE_FACTURE);
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where typeFacture in
        defaultFactureFiltering(
            "typeFacture.in=" + DEFAULT_TYPE_FACTURE + "," + UPDATED_TYPE_FACTURE,
            "typeFacture.in=" + UPDATED_TYPE_FACTURE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByTypeFactureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where typeFacture is not null
        defaultFactureFiltering("typeFacture.specified=true", "typeFacture.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where statut equals to
        defaultFactureFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where statut in
        defaultFactureFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllFacturesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where statut is not null
        defaultFactureFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance equals to
        defaultFactureFiltering("dateEcheance.equals=" + DEFAULT_DATE_ECHEANCE, "dateEcheance.equals=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance in
        defaultFactureFiltering(
            "dateEcheance.in=" + DEFAULT_DATE_ECHEANCE + "," + UPDATED_DATE_ECHEANCE,
            "dateEcheance.in=" + UPDATED_DATE_ECHEANCE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance is not null
        defaultFactureFiltering("dateEcheance.specified=true", "dateEcheance.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance is greater than or equal to
        defaultFactureFiltering(
            "dateEcheance.greaterThanOrEqual=" + DEFAULT_DATE_ECHEANCE,
            "dateEcheance.greaterThanOrEqual=" + UPDATED_DATE_ECHEANCE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance is less than or equal to
        defaultFactureFiltering(
            "dateEcheance.lessThanOrEqual=" + DEFAULT_DATE_ECHEANCE,
            "dateEcheance.lessThanOrEqual=" + SMALLER_DATE_ECHEANCE
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance is less than
        defaultFactureFiltering("dateEcheance.lessThan=" + UPDATED_DATE_ECHEANCE, "dateEcheance.lessThan=" + DEFAULT_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateEcheanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateEcheance is greater than
        defaultFactureFiltering("dateEcheance.greaterThan=" + SMALLER_DATE_ECHEANCE, "dateEcheance.greaterThan=" + DEFAULT_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllFacturesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateCreation equals to
        defaultFactureFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllFacturesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateCreation in
        defaultFactureFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllFacturesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        // Get all the factureList where dateCreation is not null
        defaultFactureFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllFacturesByClientIsEqualToSomething() throws Exception {
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            factureRepository.saveAndFlush(factureEntity);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        em.persist(client);
        em.flush();
        factureEntity.setClient(client);
        factureRepository.saveAndFlush(factureEntity);
        Long clientId = client.getId();
        // Get all the factureList where client equals to clientId
        defaultFactureShouldBeFound("clientId.equals=" + clientId);

        // Get all the factureList where client equals to (clientId + 1)
        defaultFactureShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllFacturesByConventionIsEqualToSomething() throws Exception {
        ConventionEntity convention;
        if (TestUtil.findAll(em, ConventionEntity.class).isEmpty()) {
            factureRepository.saveAndFlush(factureEntity);
            convention = ConventionResourceIT.createEntity(em);
        } else {
            convention = TestUtil.findAll(em, ConventionEntity.class).get(0);
        }
        em.persist(convention);
        em.flush();
        factureEntity.setConvention(convention);
        factureRepository.saveAndFlush(factureEntity);
        Long conventionId = convention.getId();
        // Get all the factureList where convention equals to conventionId
        defaultFactureShouldBeFound("conventionId.equals=" + conventionId);

        // Get all the factureList where convention equals to (conventionId + 1)
        defaultFactureShouldNotBeFound("conventionId.equals=" + (conventionId + 1));
    }

    private void defaultFactureFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFactureShouldBeFound(shouldBeFound);
        defaultFactureShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFactureShouldBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factureEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numFacture").value(hasItem(DEFAULT_NUM_FACTURE.intValue())))
            .andExpect(jsonPath("$.[*].dateFacture").value(hasItem(DEFAULT_DATE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].montantTotal").value(hasItem(sameNumber(DEFAULT_MONTANT_TOTAL))))
            .andExpect(jsonPath("$.[*].montantTTC").value(hasItem(sameNumber(DEFAULT_MONTANT_TTC))))
            .andExpect(jsonPath("$.[*].tva").value(hasItem(sameNumber(DEFAULT_TVA))))
            .andExpect(jsonPath("$.[*].observations").value(hasItem(DEFAULT_OBSERVATIONS)))
            .andExpect(jsonPath("$.[*].ancienneRef").value(hasItem(DEFAULT_ANCIENNE_REF)))
            .andExpect(jsonPath("$.[*].typeFacture").value(hasItem(DEFAULT_TYPE_FACTURE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));

        // Check, that the count call also returns 1
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFactureShouldNotBeFound(String filter) throws Exception {
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFactureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFacture() throws Exception {
        // Get the facture
        restFactureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFacture() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture
        FactureEntity updatedFactureEntity = factureRepository.findById(factureEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFactureEntity are not directly saved in db
        em.detach(updatedFactureEntity);
        updatedFactureEntity
            .numFacture(UPDATED_NUM_FACTURE)
            .dateFacture(UPDATED_DATE_FACTURE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .montantTTC(UPDATED_MONTANT_TTC)
            .tva(UPDATED_TVA)
            .observations(UPDATED_OBSERVATIONS)
            .ancienneRef(UPDATED_ANCIENNE_REF)
            .typeFacture(UPDATED_TYPE_FACTURE)
            .statut(UPDATED_STATUT)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .dateCreation(UPDATED_DATE_CREATION);
        FactureDTO factureDTO = factureMapper.toDto(updatedFactureEntity);

        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactureEntityToMatchAllProperties(updatedFactureEntity);
    }

    @Test
    @Transactional
    void putNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factureDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        FactureEntity partialUpdatedFactureEntity = new FactureEntity();
        partialUpdatedFactureEntity.setId(factureEntity.getId());

        partialUpdatedFactureEntity
            .numFacture(UPDATED_NUM_FACTURE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .montantTTC(UPDATED_MONTANT_TTC)
            .ancienneRef(UPDATED_ANCIENNE_REF)
            .typeFacture(UPDATED_TYPE_FACTURE)
            .statut(UPDATED_STATUT)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .dateCreation(UPDATED_DATE_CREATION);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactureEntity))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactureEntity, factureEntity),
            getPersistedFactureEntity(factureEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateFactureWithPatch() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the facture using partial update
        FactureEntity partialUpdatedFactureEntity = new FactureEntity();
        partialUpdatedFactureEntity.setId(factureEntity.getId());

        partialUpdatedFactureEntity
            .numFacture(UPDATED_NUM_FACTURE)
            .dateFacture(UPDATED_DATE_FACTURE)
            .montantTotal(UPDATED_MONTANT_TOTAL)
            .montantTTC(UPDATED_MONTANT_TTC)
            .tva(UPDATED_TVA)
            .observations(UPDATED_OBSERVATIONS)
            .ancienneRef(UPDATED_ANCIENNE_REF)
            .typeFacture(UPDATED_TYPE_FACTURE)
            .statut(UPDATED_STATUT)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .dateCreation(UPDATED_DATE_CREATION);

        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactureEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactureEntity))
            )
            .andExpect(status().isOk());

        // Validate the Facture in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactureEntityUpdatableFieldsEquals(partialUpdatedFactureEntity, getPersistedFactureEntity(partialUpdatedFactureEntity));
    }

    @Test
    @Transactional
    void patchNonExistingFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFacture() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        factureEntity.setId(longCount.incrementAndGet());

        // Create the Facture
        FactureDTO factureDTO = factureMapper.toDto(factureEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factureDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Facture in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFacture() throws Exception {
        // Initialize the database
        insertedFactureEntity = factureRepository.saveAndFlush(factureEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the facture
        restFactureMockMvc
            .perform(delete(ENTITY_API_URL_ID, factureEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return factureRepository.count();
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

    protected FactureEntity getPersistedFactureEntity(FactureEntity facture) {
        return factureRepository.findById(facture.getId()).orElseThrow();
    }

    protected void assertPersistedFactureEntityToMatchAllProperties(FactureEntity expectedFactureEntity) {
        assertFactureEntityAllPropertiesEquals(expectedFactureEntity, getPersistedFactureEntity(expectedFactureEntity));
    }

    protected void assertPersistedFactureEntityToMatchUpdatableProperties(FactureEntity expectedFactureEntity) {
        assertFactureEntityAllUpdatablePropertiesEquals(expectedFactureEntity, getPersistedFactureEntity(expectedFactureEntity));
    }
}
