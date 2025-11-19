package com.convention.web.rest;

import static com.convention.domain.ClientEntityAsserts.*;
import static com.convention.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.convention.IntegrationTest;
import com.convention.domain.ClientEntity;
import com.convention.repository.ClientRepository;
import com.convention.service.dto.ClientDTO;
import com.convention.service.mapper.ClientMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final Long DEFAULT_NUM_CLIENT = 1L;
    private static final Long UPDATED_NUM_CLIENT = 2L;
    private static final Long SMALLER_NUM_CLIENT = 1L - 1L;

    private static final String DEFAULT_NOM_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CLIENT = "LU8O1d@v.URgmVN.gb";
    private static final String UPDATED_EMAIL_CLIENT = "uwOh@Zpd-6.tege";

    private static final String DEFAULT_WHATS_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_WHATS_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_OBS_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_OBS_CLIENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private ClientEntity clientEntity;

    private ClientEntity insertedClientEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientEntity createEntity() {
        return new ClientEntity()
            .numClient(DEFAULT_NUM_CLIENT)
            .nomClient(DEFAULT_NOM_CLIENT)
            .adresseClient(DEFAULT_ADRESSE_CLIENT)
            .emailClient(DEFAULT_EMAIL_CLIENT)
            .whatsClient(DEFAULT_WHATS_CLIENT)
            .obsClient(DEFAULT_OBS_CLIENT)
            .dateCreation(DEFAULT_DATE_CREATION)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientEntity createUpdatedEntity() {
        return new ClientEntity()
            .numClient(UPDATED_NUM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .emailClient(UPDATED_EMAIL_CLIENT)
            .whatsClient(UPDATED_WHATS_CLIENT)
            .obsClient(UPDATED_OBS_CLIENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        clientEntity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientEntity != null) {
            clientRepository.delete(insertedClientEntity);
            insertedClientEntity = null;
        }
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);
        var returnedClientDTO = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientEntity = clientMapper.toEntity(returnedClientDTO);
        assertClientEntityUpdatableFieldsEquals(returnedClientEntity, getPersistedClientEntity(returnedClientEntity));

        insertedClientEntity = returnedClientEntity;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        clientEntity.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumClientIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientEntity.setNumClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomClientIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientEntity.setNomClient(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        clientEntity.setActif(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numClient").value(hasItem(DEFAULT_NUM_CLIENT.intValue())))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT)))
            .andExpect(jsonPath("$.[*].adresseClient").value(hasItem(DEFAULT_ADRESSE_CLIENT)))
            .andExpect(jsonPath("$.[*].emailClient").value(hasItem(DEFAULT_EMAIL_CLIENT)))
            .andExpect(jsonPath("$.[*].whatsClient").value(hasItem(DEFAULT_WHATS_CLIENT)))
            .andExpect(jsonPath("$.[*].obsClient").value(hasItem(DEFAULT_OBS_CLIENT)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, clientEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientEntity.getId().intValue()))
            .andExpect(jsonPath("$.numClient").value(DEFAULT_NUM_CLIENT.intValue()))
            .andExpect(jsonPath("$.nomClient").value(DEFAULT_NOM_CLIENT))
            .andExpect(jsonPath("$.adresseClient").value(DEFAULT_ADRESSE_CLIENT))
            .andExpect(jsonPath("$.emailClient").value(DEFAULT_EMAIL_CLIENT))
            .andExpect(jsonPath("$.whatsClient").value(DEFAULT_WHATS_CLIENT))
            .andExpect(jsonPath("$.obsClient").value(DEFAULT_OBS_CLIENT))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        Long id = clientEntity.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultClientFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultClientFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient equals to
        defaultClientFiltering("numClient.equals=" + DEFAULT_NUM_CLIENT, "numClient.equals=" + UPDATED_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient in
        defaultClientFiltering("numClient.in=" + DEFAULT_NUM_CLIENT + "," + UPDATED_NUM_CLIENT, "numClient.in=" + UPDATED_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient is not null
        defaultClientFiltering("numClient.specified=true", "numClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient is greater than or equal to
        defaultClientFiltering("numClient.greaterThanOrEqual=" + DEFAULT_NUM_CLIENT, "numClient.greaterThanOrEqual=" + UPDATED_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient is less than or equal to
        defaultClientFiltering("numClient.lessThanOrEqual=" + DEFAULT_NUM_CLIENT, "numClient.lessThanOrEqual=" + SMALLER_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient is less than
        defaultClientFiltering("numClient.lessThan=" + UPDATED_NUM_CLIENT, "numClient.lessThan=" + DEFAULT_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNumClientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where numClient is greater than
        defaultClientFiltering("numClient.greaterThan=" + SMALLER_NUM_CLIENT, "numClient.greaterThan=" + DEFAULT_NUM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNomClientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where nomClient equals to
        defaultClientFiltering("nomClient.equals=" + DEFAULT_NOM_CLIENT, "nomClient.equals=" + UPDATED_NOM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNomClientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where nomClient in
        defaultClientFiltering("nomClient.in=" + DEFAULT_NOM_CLIENT + "," + UPDATED_NOM_CLIENT, "nomClient.in=" + UPDATED_NOM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNomClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where nomClient is not null
        defaultClientFiltering("nomClient.specified=true", "nomClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNomClientContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where nomClient contains
        defaultClientFiltering("nomClient.contains=" + DEFAULT_NOM_CLIENT, "nomClient.contains=" + UPDATED_NOM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByNomClientNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where nomClient does not contain
        defaultClientFiltering("nomClient.doesNotContain=" + UPDATED_NOM_CLIENT, "nomClient.doesNotContain=" + DEFAULT_NOM_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseClientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where adresseClient equals to
        defaultClientFiltering("adresseClient.equals=" + DEFAULT_ADRESSE_CLIENT, "adresseClient.equals=" + UPDATED_ADRESSE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseClientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where adresseClient in
        defaultClientFiltering(
            "adresseClient.in=" + DEFAULT_ADRESSE_CLIENT + "," + UPDATED_ADRESSE_CLIENT,
            "adresseClient.in=" + UPDATED_ADRESSE_CLIENT
        );
    }

    @Test
    @Transactional
    void getAllClientsByAdresseClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where adresseClient is not null
        defaultClientFiltering("adresseClient.specified=true", "adresseClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAdresseClientContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where adresseClient contains
        defaultClientFiltering("adresseClient.contains=" + DEFAULT_ADRESSE_CLIENT, "adresseClient.contains=" + UPDATED_ADRESSE_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByAdresseClientNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where adresseClient does not contain
        defaultClientFiltering(
            "adresseClient.doesNotContain=" + UPDATED_ADRESSE_CLIENT,
            "adresseClient.doesNotContain=" + DEFAULT_ADRESSE_CLIENT
        );
    }

    @Test
    @Transactional
    void getAllClientsByEmailClientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where emailClient equals to
        defaultClientFiltering("emailClient.equals=" + DEFAULT_EMAIL_CLIENT, "emailClient.equals=" + UPDATED_EMAIL_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByEmailClientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where emailClient in
        defaultClientFiltering(
            "emailClient.in=" + DEFAULT_EMAIL_CLIENT + "," + UPDATED_EMAIL_CLIENT,
            "emailClient.in=" + UPDATED_EMAIL_CLIENT
        );
    }

    @Test
    @Transactional
    void getAllClientsByEmailClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where emailClient is not null
        defaultClientFiltering("emailClient.specified=true", "emailClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEmailClientContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where emailClient contains
        defaultClientFiltering("emailClient.contains=" + DEFAULT_EMAIL_CLIENT, "emailClient.contains=" + UPDATED_EMAIL_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByEmailClientNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where emailClient does not contain
        defaultClientFiltering("emailClient.doesNotContain=" + UPDATED_EMAIL_CLIENT, "emailClient.doesNotContain=" + DEFAULT_EMAIL_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByWhatsClientIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where whatsClient equals to
        defaultClientFiltering("whatsClient.equals=" + DEFAULT_WHATS_CLIENT, "whatsClient.equals=" + UPDATED_WHATS_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByWhatsClientIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where whatsClient in
        defaultClientFiltering(
            "whatsClient.in=" + DEFAULT_WHATS_CLIENT + "," + UPDATED_WHATS_CLIENT,
            "whatsClient.in=" + UPDATED_WHATS_CLIENT
        );
    }

    @Test
    @Transactional
    void getAllClientsByWhatsClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where whatsClient is not null
        defaultClientFiltering("whatsClient.specified=true", "whatsClient.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByWhatsClientContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where whatsClient contains
        defaultClientFiltering("whatsClient.contains=" + DEFAULT_WHATS_CLIENT, "whatsClient.contains=" + UPDATED_WHATS_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByWhatsClientNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where whatsClient does not contain
        defaultClientFiltering("whatsClient.doesNotContain=" + UPDATED_WHATS_CLIENT, "whatsClient.doesNotContain=" + DEFAULT_WHATS_CLIENT);
    }

    @Test
    @Transactional
    void getAllClientsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where dateCreation equals to
        defaultClientFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllClientsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where dateCreation in
        defaultClientFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllClientsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where dateCreation is not null
        defaultClientFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where actif equals to
        defaultClientFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllClientsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where actif in
        defaultClientFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllClientsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        // Get all the clientList where actif is not null
        defaultClientFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].numClient").value(hasItem(DEFAULT_NUM_CLIENT.intValue())))
            .andExpect(jsonPath("$.[*].nomClient").value(hasItem(DEFAULT_NOM_CLIENT)))
            .andExpect(jsonPath("$.[*].adresseClient").value(hasItem(DEFAULT_ADRESSE_CLIENT)))
            .andExpect(jsonPath("$.[*].emailClient").value(hasItem(DEFAULT_EMAIL_CLIENT)))
            .andExpect(jsonPath("$.[*].whatsClient").value(hasItem(DEFAULT_WHATS_CLIENT)))
            .andExpect(jsonPath("$.[*].obsClient").value(hasItem(DEFAULT_OBS_CLIENT)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client
        ClientEntity updatedClientEntity = clientRepository.findById(clientEntity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientEntity are not directly saved in db
        em.detach(updatedClientEntity);
        updatedClientEntity
            .numClient(UPDATED_NUM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .emailClient(UPDATED_EMAIL_CLIENT)
            .whatsClient(UPDATED_WHATS_CLIENT)
            .obsClient(UPDATED_OBS_CLIENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .actif(UPDATED_ACTIF);
        ClientDTO clientDTO = clientMapper.toDto(updatedClientEntity);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientEntityToMatchAllProperties(updatedClientEntity);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        ClientEntity partialUpdatedClientEntity = new ClientEntity();
        partialUpdatedClientEntity.setId(clientEntity.getId());

        partialUpdatedClientEntity
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .whatsClient(UPDATED_WHATS_CLIENT)
            .obsClient(UPDATED_OBS_CLIENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .actif(UPDATED_ACTIF);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientEntity))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientEntityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientEntity, clientEntity),
            getPersistedClientEntity(clientEntity)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        ClientEntity partialUpdatedClientEntity = new ClientEntity();
        partialUpdatedClientEntity.setId(clientEntity.getId());

        partialUpdatedClientEntity
            .numClient(UPDATED_NUM_CLIENT)
            .nomClient(UPDATED_NOM_CLIENT)
            .adresseClient(UPDATED_ADRESSE_CLIENT)
            .emailClient(UPDATED_EMAIL_CLIENT)
            .whatsClient(UPDATED_WHATS_CLIENT)
            .obsClient(UPDATED_OBS_CLIENT)
            .dateCreation(UPDATED_DATE_CREATION)
            .actif(UPDATED_ACTIF);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientEntity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientEntity))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientEntityUpdatableFieldsEquals(partialUpdatedClientEntity, getPersistedClientEntity(partialUpdatedClientEntity));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientEntity.setId(longCount.incrementAndGet());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(clientEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClientEntity = clientRepository.saveAndFlush(clientEntity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientEntity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected ClientEntity getPersistedClientEntity(ClientEntity client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientEntityToMatchAllProperties(ClientEntity expectedClientEntity) {
        assertClientEntityAllPropertiesEquals(expectedClientEntity, getPersistedClientEntity(expectedClientEntity));
    }

    protected void assertPersistedClientEntityToMatchUpdatableProperties(ClientEntity expectedClientEntity) {
        assertClientEntityAllUpdatablePropertiesEquals(expectedClientEntity, getPersistedClientEntity(expectedClientEntity));
    }
}
