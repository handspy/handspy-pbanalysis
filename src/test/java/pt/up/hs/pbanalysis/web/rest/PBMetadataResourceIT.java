package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.PbanalysisApp;
import pt.up.hs.pbanalysis.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.pbanalysis.domain.PBMetadata;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.repository.PBMetadataRepository;
import pt.up.hs.pbanalysis.service.PBMetadataService;
import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;
import pt.up.hs.pbanalysis.service.mapper.PBMetadataMapper;
import pt.up.hs.pbanalysis.web.rest.errors.ExceptionTranslator;
import pt.up.hs.pbanalysis.service.dto.PBMetadataCriteria;
import pt.up.hs.pbanalysis.service.PBMetadataQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static pt.up.hs.pbanalysis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PBMetadataResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PbanalysisApp.class})
public class PBMetadataResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private PBMetadataRepository pBMetadataRepository;

    @Autowired
    private PBMetadataMapper pBMetadataMapper;

    @Autowired
    private PBMetadataService pBMetadataService;

    @Autowired
    private PBMetadataQueryService pBMetadataQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPBMetadataMockMvc;

    private PBMetadata pBMetadata;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PBMetadataResource pBMetadataResource = new PBMetadataResource(pBMetadataService, pBMetadataQueryService);
        this.restPBMetadataMockMvc = MockMvcBuilders.standaloneSetup(pBMetadataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBMetadata createEntity(EntityManager em) {
        PBMetadata pBMetadata = new PBMetadata()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return pBMetadata;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBMetadata createUpdatedEntity(EntityManager em) {
        PBMetadata pBMetadata = new PBMetadata()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        return pBMetadata;
    }

    @BeforeEach
    public void initTest() {
        pBMetadata = createEntity(em);
    }

    @Test
    @Transactional
    public void createPBMetadata() throws Exception {
        int databaseSizeBeforeCreate = pBMetadataRepository.findAll().size();

        // Create the PBMetadata
        PBMetadataDTO pBMetadataDTO = pBMetadataMapper.toDto(pBMetadata);
        restPBMetadataMockMvc.perform(post("/api/pb-metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBMetadataDTO)))
            .andExpect(status().isCreated());

        // Validate the PBMetadata in the database
        List<PBMetadata> pBMetadataList = pBMetadataRepository.findAll();
        assertThat(pBMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        PBMetadata testPBMetadata = pBMetadataList.get(pBMetadataList.size() - 1);
        assertThat(testPBMetadata.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPBMetadata.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createPBMetadataWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pBMetadataRepository.findAll().size();

        // Create the PBMetadata with an existing ID
        pBMetadata.setId(1L);
        PBMetadataDTO pBMetadataDTO = pBMetadataMapper.toDto(pBMetadata);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPBMetadataMockMvc.perform(post("/api/pb-metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBMetadata in the database
        List<PBMetadata> pBMetadataList = pBMetadataRepository.findAll();
        assertThat(pBMetadataList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPBMetadata() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList
        restPBMetadataMockMvc.perform(get("/api/pb-metadata?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getPBMetadata() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get the pBMetadata
        restPBMetadataMockMvc.perform(get("/api/pb-metadata/{id}", pBMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pBMetadata.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }


    @Test
    @Transactional
    public void getPBMetadataByIdFiltering() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        Long id = pBMetadata.getId();

        defaultPBMetadataShouldBeFound("id.equals=" + id);
        defaultPBMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultPBMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPBMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultPBMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPBMetadataShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPBMetadataByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key equals to DEFAULT_KEY
        defaultPBMetadataShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the pBMetadataList where key equals to UPDATED_KEY
        defaultPBMetadataShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key not equals to DEFAULT_KEY
        defaultPBMetadataShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the pBMetadataList where key not equals to UPDATED_KEY
        defaultPBMetadataShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key in DEFAULT_KEY or UPDATED_KEY
        defaultPBMetadataShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the pBMetadataList where key equals to UPDATED_KEY
        defaultPBMetadataShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key is not null
        defaultPBMetadataShouldBeFound("key.specified=true");

        // Get all the pBMetadataList where key is null
        defaultPBMetadataShouldNotBeFound("key.specified=false");
    }
                @Test
    @Transactional
    public void getAllPBMetadataByKeyContainsSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key contains DEFAULT_KEY
        defaultPBMetadataShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the pBMetadataList where key contains UPDATED_KEY
        defaultPBMetadataShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where key does not contain DEFAULT_KEY
        defaultPBMetadataShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the pBMetadataList where key does not contain UPDATED_KEY
        defaultPBMetadataShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }


    @Test
    @Transactional
    public void getAllPBMetadataByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value equals to DEFAULT_VALUE
        defaultPBMetadataShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the pBMetadataList where value equals to UPDATED_VALUE
        defaultPBMetadataShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value not equals to DEFAULT_VALUE
        defaultPBMetadataShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the pBMetadataList where value not equals to UPDATED_VALUE
        defaultPBMetadataShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByValueIsInShouldWork() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultPBMetadataShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the pBMetadataList where value equals to UPDATED_VALUE
        defaultPBMetadataShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value is not null
        defaultPBMetadataShouldBeFound("value.specified=true");

        // Get all the pBMetadataList where value is null
        defaultPBMetadataShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllPBMetadataByValueContainsSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value contains DEFAULT_VALUE
        defaultPBMetadataShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the pBMetadataList where value contains UPDATED_VALUE
        defaultPBMetadataShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllPBMetadataByValueNotContainsSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        // Get all the pBMetadataList where value does not contain DEFAULT_VALUE
        defaultPBMetadataShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the pBMetadataList where value does not contain UPDATED_VALUE
        defaultPBMetadataShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllPBMetadataByAnalysisIsEqualToSomething() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);
        PBAnalysis analysis = PBAnalysisResourceIT.createEntity(em);
        em.persist(analysis);
        em.flush();
        pBMetadata.setAnalysis(analysis);
        pBMetadataRepository.saveAndFlush(pBMetadata);
        Long analysisId = analysis.getId();

        // Get all the pBMetadataList where analysis equals to analysisId
        defaultPBMetadataShouldBeFound("analysisId.equals=" + analysisId);

        // Get all the pBMetadataList where analysis equals to analysisId + 1
        defaultPBMetadataShouldNotBeFound("analysisId.equals=" + (analysisId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPBMetadataShouldBeFound(String filter) throws Exception {
        restPBMetadataMockMvc.perform(get("/api/pb-metadata?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restPBMetadataMockMvc.perform(get("/api/pb-metadata/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPBMetadataShouldNotBeFound(String filter) throws Exception {
        restPBMetadataMockMvc.perform(get("/api/pb-metadata?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPBMetadataMockMvc.perform(get("/api/pb-metadata/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPBMetadata() throws Exception {
        // Get the pBMetadata
        restPBMetadataMockMvc.perform(get("/api/pb-metadata/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePBMetadata() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        int databaseSizeBeforeUpdate = pBMetadataRepository.findAll().size();

        // Update the pBMetadata
        PBMetadata updatedPBMetadata = pBMetadataRepository.findById(pBMetadata.getId()).get();
        // Disconnect from session so that the updates on updatedPBMetadata are not directly saved in db
        em.detach(updatedPBMetadata);
        updatedPBMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        PBMetadataDTO pBMetadataDTO = pBMetadataMapper.toDto(updatedPBMetadata);

        restPBMetadataMockMvc.perform(put("/api/pb-metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBMetadataDTO)))
            .andExpect(status().isOk());

        // Validate the PBMetadata in the database
        List<PBMetadata> pBMetadataList = pBMetadataRepository.findAll();
        assertThat(pBMetadataList).hasSize(databaseSizeBeforeUpdate);
        PBMetadata testPBMetadata = pBMetadataList.get(pBMetadataList.size() - 1);
        assertThat(testPBMetadata.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPBMetadata.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingPBMetadata() throws Exception {
        int databaseSizeBeforeUpdate = pBMetadataRepository.findAll().size();

        // Create the PBMetadata
        PBMetadataDTO pBMetadataDTO = pBMetadataMapper.toDto(pBMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPBMetadataMockMvc.perform(put("/api/pb-metadata")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBMetadata in the database
        List<PBMetadata> pBMetadataList = pBMetadataRepository.findAll();
        assertThat(pBMetadataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePBMetadata() throws Exception {
        // Initialize the database
        pBMetadataRepository.saveAndFlush(pBMetadata);

        int databaseSizeBeforeDelete = pBMetadataRepository.findAll().size();

        // Delete the pBMetadata
        restPBMetadataMockMvc.perform(delete("/api/pb-metadata/{id}", pBMetadata.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PBMetadata> pBMetadataList = pBMetadataRepository.findAll();
        assertThat(pBMetadataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
