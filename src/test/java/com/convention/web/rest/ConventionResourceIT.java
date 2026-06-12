package com.convention.web.rest;

import static com.convention.domain.ConventionEntityAsserts.*;
import static com.convention.web.rest.TestUtil.createUpdateProxyForBean;
import static com.convention.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.convention.IntegrationTest;
import com.convention.domain.ClientEntity;
import com.convention.domain.ConventionEntity;
import com.convention.domain.enumeration.PeriodeEcheance;
import com.convention.domain.enumeration.StatutConvention;
import com.convention.repository.ConventionRepository;
import com.convention.service.dto.ConventionDTO;
import com.convention.service.mapper.ConventionMapper;
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
 * Integration tests for the {@link ConventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConventionResourceIT {

    private static final Long DEFAULT_NUM_CONVENTION = 1L;
    private static final Long UPDATED_NUM_CONVENTION = 2L;
    private static final Long SMALLER_NUM_CONVENTION = 1L - 1L;

    private static final LocalDate DEFAULT_DATE_SIGN_CONV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SIGN_CONV = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_SIGN_CONV = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_DEBUT_CONV = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_CONV = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT_CONV = LocalDate.ofEpochDay(-1L);

    private static final PeriodeEcheance DEFAULT_PERIODE_ECHEANCE = PeriodeEcheance.MENSUEL;
    private static final PeriodeEcheance UPDATED_PERIODE_ECHEANCE = PeriodeEcheance.ANNUEL;

    private static final BigDecimal DEFAULT_REDEVANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_REDEVANCE = new BigDecimal(1);
    private static final BigDecimal SMALLER_REDEVANCE = new BigDecimal(0 - 1);

    private static final String DEFAULT_NOM_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_RESPONSABLE = "BBBBBBBBBB";

    private static final StatutConvention DEFAULT_STATUT = StatutConvention.BROUILLON;
    private static final StatutConvention UPDATED_STATUT = StatutConvention.ACTIVE;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/conventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConventionRepository conventionRepository;

    @Autowired
    private ConventionMapper conventionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConventionMockMvc;

    private ConventionEntity conventionEntity;

    private ConventionEntity insertedConventionEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConventionEntity createEntity(EntityManager em) {
        ConventionEntity conventionEntity = new ConventionEntity()
            .numConvention(DEFAULT_NUM_CONVENTION)
            .dateSignConv(DEFAULT_DATE_SIGN_CONV)
            .dateDebutConv(DEFAULT_DATE_DEBUT_CONV)
            .periodeEcheance(DEFAULT_PERIODE_ECHEANCE)
            .redevance(DEFAULT_REDEVANCE)
            .nomResponsable(DEFAULT_NOM_RESPONSABLE)
            .statut(DEFAULT_STATUT)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        conventionEntity.setClient(client);
        return conventionEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConventionEntity createUpdatedEntity(EntityManager em) {
        ConventionEntity updatedConventionEntity = new ConventionEntity()
            .numConvention(UPDATED_NUM_CONVENTION)
            .dateSignConv(UPDATED_DATE_SIGN_CONV)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .periodeEcheance(UPDATED_PERIODE_ECHEANCE)
            .redevance(UPDATED_REDEVANCE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        // Add required entity
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity();
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        updatedConventionEntity.setClient(client);
        return updatedConventionEntity;
    }

    @BeforeEach
    void initTest() {
        conventionEntity = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedConventionEntity != null) {
            conventionRepository.delete(insertedConventionEntity);
            insertedConventionEntity = null;
        }
    }

    @Test
    @Transactional
    void createConvention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);
        var returnedConventionDTO = om.readValue(
            restConventionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConventionDTO.class
        );

        // Validate the Convention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConventionEntity = conventionMapper.toEntity(returnedConventionDTO);
        assertConventionEntityUpdatableFieldsEquals(returnedConventionEntity, getPersistedConventionEntity(returnedConventionEntity));

        insertedConventionEntity = returnedConventionEntity;
    }

    @Test
    @Transactional
    void createConventionWithExistingId() throws Exception {
        // Create the Convention with an existing ID
        conventionEntity.setId(1L);
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumConventionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setNumConvention(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateSignConvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setDateSignConv(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutConvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setDateDebutConv(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodeEcheanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setPeriodeEcheance(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRedevanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setRedevance(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomResponsableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setNomResponsable(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conventionEntity.setStatut(null);

        // Create the Convention, which fails.
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        restConventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConventions() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conventionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numConvention").value(hasItem(DEFAULT_NUM_CONVENTION.intValue())))
            .andExpect(jsonPath("$.[*].dateSignConv").value(hasItem(DEFAULT_DATE_SIGN_CONV.toString())))
            .andExpect(jsonPath("$.[*].dateDebutConv").value(hasItem(DEFAULT_DATE_DEBUT_CONV.toString())))
            .andExpect(jsonPath("$.[*].periodeEcheance").value(hasItem(DEFAULT_PERIODE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].redevance").value(hasItem(sameNumber(DEFAULT_REDEVANCE))))
            .andExpect(jsonPath("$.[*].nomResponsable").value(hasItem(DEFAULT_NOM_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())));
    }

    @Test
    @Transactional
    void getConvention() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get the convention
        restConventionMockMvc
            .perform(get(ENTITY_API_URL_ID, conventionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conventionEntity.getId().intValue()))
            .andExpect(jsonPath("$.numConvention").value(DEFAULT_NUM_CONVENTION.intValue()))
            .andExpect(jsonPath("$.dateSignConv").value(DEFAULT_DATE_SIGN_CONV.toString()))
            .andExpect(jsonPath("$.dateDebutConv").value(DEFAULT_DATE_DEBUT_CONV.toString()))
            .andExpect(jsonPath("$.periodeEcheance").value(DEFAULT_PERIODE_ECHEANCE.toString()))
            .andExpect(jsonPath("$.redevance").value(sameNumber(DEFAULT_REDEVANCE)))
            .andExpect(jsonPath("$.nomResponsable").value(DEFAULT_NOM_RESPONSABLE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()));
    }

    @Test
    @Transactional
    void getConventionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        Long id = conventionEntity.getId();

        defaultConventionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConventionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConventionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention equals to
        defaultConventionFiltering("numConvention.equals=" + DEFAULT_NUM_CONVENTION, "numConvention.equals=" + UPDATED_NUM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention in
        defaultConventionFiltering(
            "numConvention.in=" + DEFAULT_NUM_CONVENTION + "," + UPDATED_NUM_CONVENTION,
            "numConvention.in=" + UPDATED_NUM_CONVENTION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention is not null
        defaultConventionFiltering("numConvention.specified=true", "numConvention.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention is greater than or equal to
        defaultConventionFiltering(
            "numConvention.greaterThanOrEqual=" + DEFAULT_NUM_CONVENTION,
            "numConvention.greaterThanOrEqual=" + UPDATED_NUM_CONVENTION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention is less than or equal to
        defaultConventionFiltering(
            "numConvention.lessThanOrEqual=" + DEFAULT_NUM_CONVENTION,
            "numConvention.lessThanOrEqual=" + SMALLER_NUM_CONVENTION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention is less than
        defaultConventionFiltering("numConvention.lessThan=" + UPDATED_NUM_CONVENTION, "numConvention.lessThan=" + DEFAULT_NUM_CONVENTION);
    }

    @Test
    @Transactional
    void getAllConventionsByNumConventionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where numConvention is greater than
        defaultConventionFiltering(
            "numConvention.greaterThan=" + SMALLER_NUM_CONVENTION,
            "numConvention.greaterThan=" + DEFAULT_NUM_CONVENTION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv equals to
        defaultConventionFiltering("dateSignConv.equals=" + DEFAULT_DATE_SIGN_CONV, "dateSignConv.equals=" + UPDATED_DATE_SIGN_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv in
        defaultConventionFiltering(
            "dateSignConv.in=" + DEFAULT_DATE_SIGN_CONV + "," + UPDATED_DATE_SIGN_CONV,
            "dateSignConv.in=" + UPDATED_DATE_SIGN_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv is not null
        defaultConventionFiltering("dateSignConv.specified=true", "dateSignConv.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv is greater than or equal to
        defaultConventionFiltering(
            "dateSignConv.greaterThanOrEqual=" + DEFAULT_DATE_SIGN_CONV,
            "dateSignConv.greaterThanOrEqual=" + UPDATED_DATE_SIGN_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv is less than or equal to
        defaultConventionFiltering(
            "dateSignConv.lessThanOrEqual=" + DEFAULT_DATE_SIGN_CONV,
            "dateSignConv.lessThanOrEqual=" + SMALLER_DATE_SIGN_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv is less than
        defaultConventionFiltering("dateSignConv.lessThan=" + UPDATED_DATE_SIGN_CONV, "dateSignConv.lessThan=" + DEFAULT_DATE_SIGN_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateSignConvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateSignConv is greater than
        defaultConventionFiltering(
            "dateSignConv.greaterThan=" + SMALLER_DATE_SIGN_CONV,
            "dateSignConv.greaterThan=" + DEFAULT_DATE_SIGN_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv equals to
        defaultConventionFiltering("dateDebutConv.equals=" + DEFAULT_DATE_DEBUT_CONV, "dateDebutConv.equals=" + UPDATED_DATE_DEBUT_CONV);
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv in
        defaultConventionFiltering(
            "dateDebutConv.in=" + DEFAULT_DATE_DEBUT_CONV + "," + UPDATED_DATE_DEBUT_CONV,
            "dateDebutConv.in=" + UPDATED_DATE_DEBUT_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv is not null
        defaultConventionFiltering("dateDebutConv.specified=true", "dateDebutConv.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv is greater than or equal to
        defaultConventionFiltering(
            "dateDebutConv.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT_CONV,
            "dateDebutConv.greaterThanOrEqual=" + UPDATED_DATE_DEBUT_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv is less than or equal to
        defaultConventionFiltering(
            "dateDebutConv.lessThanOrEqual=" + DEFAULT_DATE_DEBUT_CONV,
            "dateDebutConv.lessThanOrEqual=" + SMALLER_DATE_DEBUT_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv is less than
        defaultConventionFiltering(
            "dateDebutConv.lessThan=" + UPDATED_DATE_DEBUT_CONV,
            "dateDebutConv.lessThan=" + DEFAULT_DATE_DEBUT_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateDebutConvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateDebutConv is greater than
        defaultConventionFiltering(
            "dateDebutConv.greaterThan=" + SMALLER_DATE_DEBUT_CONV,
            "dateDebutConv.greaterThan=" + DEFAULT_DATE_DEBUT_CONV
        );
    }

    @Test
    @Transactional
    void getAllConventionsByPeriodeEcheanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where periodeEcheance equals to
        defaultConventionFiltering(
            "periodeEcheance.equals=" + DEFAULT_PERIODE_ECHEANCE,
            "periodeEcheance.equals=" + UPDATED_PERIODE_ECHEANCE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByPeriodeEcheanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where periodeEcheance in
        defaultConventionFiltering(
            "periodeEcheance.in=" + DEFAULT_PERIODE_ECHEANCE + "," + UPDATED_PERIODE_ECHEANCE,
            "periodeEcheance.in=" + UPDATED_PERIODE_ECHEANCE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByPeriodeEcheanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where periodeEcheance is not null
        defaultConventionFiltering("periodeEcheance.specified=true", "periodeEcheance.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance equals to
        defaultConventionFiltering("redevance.equals=" + DEFAULT_REDEVANCE, "redevance.equals=" + UPDATED_REDEVANCE);
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance in
        defaultConventionFiltering("redevance.in=" + DEFAULT_REDEVANCE + "," + UPDATED_REDEVANCE, "redevance.in=" + UPDATED_REDEVANCE);
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance is not null
        defaultConventionFiltering("redevance.specified=true", "redevance.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance is greater than or equal to
        defaultConventionFiltering(
            "redevance.greaterThanOrEqual=" + DEFAULT_REDEVANCE,
            "redevance.greaterThanOrEqual=" + UPDATED_REDEVANCE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance is less than or equal to
        defaultConventionFiltering("redevance.lessThanOrEqual=" + DEFAULT_REDEVANCE, "redevance.lessThanOrEqual=" + SMALLER_REDEVANCE);
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance is less than
        defaultConventionFiltering("redevance.lessThan=" + UPDATED_REDEVANCE, "redevance.lessThan=" + DEFAULT_REDEVANCE);
    }

    @Test
    @Transactional
    void getAllConventionsByRedevanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where redevance is greater than
        defaultConventionFiltering("redevance.greaterThan=" + SMALLER_REDEVANCE, "redevance.greaterThan=" + DEFAULT_REDEVANCE);
    }

    @Test
    @Transactional
    void getAllConventionsByNomResponsableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where nomResponsable equals to
        defaultConventionFiltering("nomResponsable.equals=" + DEFAULT_NOM_RESPONSABLE, "nomResponsable.equals=" + UPDATED_NOM_RESPONSABLE);
    }

    @Test
    @Transactional
    void getAllConventionsByNomResponsableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where nomResponsable in
        defaultConventionFiltering(
            "nomResponsable.in=" + DEFAULT_NOM_RESPONSABLE + "," + UPDATED_NOM_RESPONSABLE,
            "nomResponsable.in=" + UPDATED_NOM_RESPONSABLE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByNomResponsableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where nomResponsable is not null
        defaultConventionFiltering("nomResponsable.specified=true", "nomResponsable.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByNomResponsableContainsSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where nomResponsable contains
        defaultConventionFiltering(
            "nomResponsable.contains=" + DEFAULT_NOM_RESPONSABLE,
            "nomResponsable.contains=" + UPDATED_NOM_RESPONSABLE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByNomResponsableNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where nomResponsable does not contain
        defaultConventionFiltering(
            "nomResponsable.doesNotContain=" + UPDATED_NOM_RESPONSABLE,
            "nomResponsable.doesNotContain=" + DEFAULT_NOM_RESPONSABLE
        );
    }

    @Test
    @Transactional
    void getAllConventionsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where statut equals to
        defaultConventionFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConventionsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where statut in
        defaultConventionFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConventionsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where statut is not null
        defaultConventionFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateCreation equals to
        defaultConventionFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllConventionsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateCreation in
        defaultConventionFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateCreation is not null
        defaultConventionFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateModification equals to
        defaultConventionFiltering(
            "dateModification.equals=" + DEFAULT_DATE_MODIFICATION,
            "dateModification.equals=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateModification in
        defaultConventionFiltering(
            "dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION,
            "dateModification.in=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllConventionsByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        // Get all the conventionList where dateModification is not null
        defaultConventionFiltering("dateModification.specified=true", "dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllConventionsByClientIsEqualToSomething() throws Exception {
        ClientEntity client;
        if (TestUtil.findAll(em, ClientEntity.class).isEmpty()) {
            conventionRepository.saveAndFlush(conventionEntity);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, ClientEntity.class).get(0);
        }
        em.persist(client);
        em.flush();
        conventionEntity.setClient(client);
        conventionRepository.saveAndFlush(conventionEntity);
        Long clientId = client.getId();
        // Get all the conventionList where client equals to clientId
        defaultConventionShouldBeFound("clientId.equals=" + clientId);

        // Get all the conventionList where client equals to (clientId + 1)
        defaultConventionShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    private void defaultConventionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConventionShouldBeFound(shouldBeFound);
        defaultConventionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConventionShouldBeFound(String filter) throws Exception {
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conventionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numConvention").value(hasItem(DEFAULT_NUM_CONVENTION.intValue())))
            .andExpect(jsonPath("$.[*].dateSignConv").value(hasItem(DEFAULT_DATE_SIGN_CONV.toString())))
            .andExpect(jsonPath("$.[*].dateDebutConv").value(hasItem(DEFAULT_DATE_DEBUT_CONV.toString())))
            .andExpect(jsonPath("$.[*].periodeEcheance").value(hasItem(DEFAULT_PERIODE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].redevance").value(hasItem(sameNumber(DEFAULT_REDEVANCE))))
            .andExpect(jsonPath("$.[*].nomResponsable").value(hasItem(DEFAULT_NOM_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())));

        // Check, that the count call also returns 1
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConventionShouldNotBeFound(String filter) throws Exception {
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConvention() throws Exception {
        // Get the convention
        restConventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConvention() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the convention
        ConventionEntity updatedConventionEntity = conventionRepository.findById(conventionEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConventionEntity are not directly saved in db
        em.detach(updatedConventionEntity);
        updatedConventionEntity
            .numConvention(UPDATED_NUM_CONVENTION)
            .dateSignConv(UPDATED_DATE_SIGN_CONV)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .periodeEcheance(UPDATED_PERIODE_ECHEANCE)
            .redevance(UPDATED_REDEVANCE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        ConventionDTO conventionDTO = conventionMapper.toDto(updatedConventionEntity);

        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conventionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConventionEntityToMatchAllProperties(updatedConventionEntity);
    }

    @Test
    @Transactional
    void putNonExistingConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConventionWithPatch() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the convention using partial update
        ConventionEntity partialUpdatedConventionEntity = new ConventionEntity();
        partialUpdatedConventionEntity.setId(conventionEntity.getId());

        partialUpdatedConventionEntity
            .numConvention(UPDATED_NUM_CONVENTION)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .periodeEcheance(UPDATED_PERIODE_ECHEANCE)
            .dateModification(UPDATED_DATE_MODIFICATION);

        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConventionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConventionEntity))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConventionEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConventionEntity, conventionEntity),
            getPersistedConventionEntity(conventionEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateConventionWithPatch() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the convention using partial update
        ConventionEntity partialUpdatedConventionEntity = new ConventionEntity();
        partialUpdatedConventionEntity.setId(conventionEntity.getId());

        partialUpdatedConventionEntity
            .numConvention(UPDATED_NUM_CONVENTION)
            .dateSignConv(UPDATED_DATE_SIGN_CONV)
            .dateDebutConv(UPDATED_DATE_DEBUT_CONV)
            .periodeEcheance(UPDATED_PERIODE_ECHEANCE)
            .redevance(UPDATED_REDEVANCE)
            .nomResponsable(UPDATED_NOM_RESPONSABLE)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);

        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConventionEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConventionEntity))
            )
            .andExpect(status().isOk());

        // Validate the Convention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConventionEntityUpdatableFieldsEquals(
            partialUpdatedConventionEntity,
            getPersistedConventionEntity(partialUpdatedConventionEntity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conventionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConvention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conventionEntity.setId(longCount.incrementAndGet());

        // Create the Convention
        ConventionDTO conventionDTO = conventionMapper.toDto(conventionEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConventionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(conventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Convention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConvention() throws Exception {
        // Initialize the database
        insertedConventionEntity = conventionRepository.saveAndFlush(conventionEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the convention
        restConventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, conventionEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return conventionRepository.count();
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

    protected ConventionEntity getPersistedConventionEntity(ConventionEntity convention) {
        return conventionRepository.findById(convention.getId()).orElseThrow();
    }

    protected void assertPersistedConventionEntityToMatchAllProperties(ConventionEntity expectedConventionEntity) {
        assertConventionEntityAllPropertiesEquals(expectedConventionEntity, getPersistedConventionEntity(expectedConventionEntity));
    }

    protected void assertPersistedConventionEntityToMatchUpdatableProperties(ConventionEntity expectedConventionEntity) {
        assertConventionEntityAllUpdatablePropertiesEquals(
            expectedConventionEntity,
            getPersistedConventionEntity(expectedConventionEntity)
        );
    }
}
