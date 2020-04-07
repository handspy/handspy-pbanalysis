package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.PBAnalysisApp;
import pt.up.hs.pbanalysis.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;
import pt.up.hs.pbanalysis.web.rest.errors.ExceptionTranslator;
import pt.up.hs.pbanalysis.service.PBAnalysisQueryService;

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
 * Integration tests for the {@link PBAnalysisResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PBAnalysisApp.class})
public class PBAnalysisResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 1L;
    private static final Long DEFAULT_SAMPLE_ID = 1001L;
    private static final Long OTHER_SAMPLE_ID = 1L;
    private static final Long DEFAULT_PROTOCOL_ID = 2001L;
    private static final Long OTHER_PROTOCOL_ID = 1L;

    private static final Long DEFAULT_THRESHOLD = 1L;
    private static final Long UPDATED_THRESHOLD = 2L;
    private static final Long SMALLER_THRESHOLD = 1L - 1L;

    @Autowired
    private PBAnalysisRepository pBAnalysisRepository;

    @Autowired
    private PBAnalysisMapper pBAnalysisMapper;

    @Autowired
    private PBAnalysisService pBAnalysisService;

    @Autowired
    private PBAnalysisQueryService pBAnalysisQueryService;

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

    private MockMvc restPBAnalysisMockMvc;

    private PBAnalysis pBAnalysis;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PBAnalysisResource pBAnalysisResource = new PBAnalysisResource(pBAnalysisService, pBAnalysisQueryService);
        this.restPBAnalysisMockMvc = MockMvcBuilders.standaloneSetup(pBAnalysisResource)
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
    public static PBAnalysis createEntity(EntityManager em) {
        return new PBAnalysis()
            .threshold(DEFAULT_THRESHOLD)
            .protocolId(DEFAULT_PROTOCOL_ID)
            .sampleId(DEFAULT_SAMPLE_ID);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBAnalysis createUpdatedEntity(EntityManager em) {
        return new PBAnalysis()
            .threshold(UPDATED_THRESHOLD)
            .protocolId(OTHER_PROTOCOL_ID)
            .sampleId(OTHER_SAMPLE_ID);
    }

    @BeforeEach
    public void initTest() {
        pBAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createPBAnalysis() throws Exception {
        int databaseSizeBeforeCreate = pBAnalysisRepository.findAll().size();

        // Create the PBAnalysis
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pBAnalysis);
        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isCreated());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        PBAnalysis testPBAnalysis = pBAnalysisList.get(pBAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getSampleId()).isEqualTo(DEFAULT_SAMPLE_ID);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(DEFAULT_THRESHOLD);
    }

    @Test
    @Transactional
    public void createPBAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pBAnalysisRepository.findAll().size();

        // Create the PBAnalysis with an existing ID
        pBAnalysis.setId(1L);
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pBAnalysis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProtocolIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBAnalysisRepository.findAll().size();
        // set the field null
        pBAnalysis.setProtocolId(null);

        // Create the PBAnalysis, which fails.
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pBAnalysis);

        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSampleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBAnalysisRepository.findAll().size();
        // set the field null
        pBAnalysis.setSampleId(null);

        // Create the PBAnalysis, which fails.
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pBAnalysis);

        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPBAnalyses() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses?sort=id,desc", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleId").value(hasItem(DEFAULT_SAMPLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].protocolId").value(hasItem(DEFAULT_PROTOCOL_ID.intValue())))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD.intValue())));
    }

    @Test
    @Transactional
    public void getPBAnalysis() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get the pBAnalysis
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pBAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pBAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.sampleId").value(DEFAULT_SAMPLE_ID.intValue()))
            .andExpect(jsonPath("$.protocolId").value(DEFAULT_PROTOCOL_ID.intValue()))
            .andExpect(jsonPath("$.threshold").value(DEFAULT_THRESHOLD.intValue()));
    }


    @Test
    @Transactional
    public void getPBAnalysesByIdFiltering() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        Long id = pBAnalysis.getId();

        defaultPBAnalysisShouldBeFound("id.equals=" + id);
        defaultPBAnalysisShouldNotBeFound("id.notEquals=" + id);

        defaultPBAnalysisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPBAnalysisShouldNotBeFound("id.greaterThan=" + id);

        defaultPBAnalysisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPBAnalysisShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold equals to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.equals=" + DEFAULT_THRESHOLD);

        // Get all the pBAnalysisList where threshold equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.equals=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold not equals to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.notEquals=" + DEFAULT_THRESHOLD);

        // Get all the pBAnalysisList where threshold not equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.notEquals=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold in DEFAULT_THRESHOLD or UPDATED_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.in=" + DEFAULT_THRESHOLD + "," + UPDATED_THRESHOLD);

        // Get all the pBAnalysisList where threshold equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.in=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold is not null
        defaultPBAnalysisShouldBeFound("threshold.specified=true");

        // Get all the pBAnalysisList where threshold is null
        defaultPBAnalysisShouldNotBeFound("threshold.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold is greater than or equal to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.greaterThanOrEqual=" + DEFAULT_THRESHOLD);

        // Get all the pBAnalysisList where threshold is greater than or equal to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.greaterThanOrEqual=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        // Get all the pBAnalysisList where threshold is less than or equal to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.lessThanOrEqual=" + DEFAULT_THRESHOLD);

        // Get all the pBAnalysisList where threshold is less than or equal to SMALLER_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.lessThanOrEqual=" + SMALLER_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByBurstsIsEqualToSomething() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);
        PBBurst bursts = PBBurstResourceIT.createEntity(em);
        em.persist(bursts);
        em.flush();
        pBAnalysis.addBursts(bursts);
        pBAnalysisRepository.saveAndFlush(pBAnalysis);
        Long burstsId = bursts.getId();

        // Get all the pBAnalysisList where bursts equals to burstsId
        defaultPBAnalysisShouldBeFound("burstsId.equals=" + burstsId);

        // Get all the pBAnalysisList where bursts equals to burstsId + 1
        defaultPBAnalysisShouldNotBeFound("burstsId.equals=" + (burstsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPBAnalysisShouldBeFound(String filter) throws Exception {
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].sampleId").value(hasItem(DEFAULT_SAMPLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].protocolId").value(hasItem(DEFAULT_PROTOCOL_ID.intValue())))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD.intValue())));

        // Check, that the count call also returns 1
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPBAnalysisShouldNotBeFound(String filter) throws Exception {
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPBAnalysis() throws Exception {
        // Get the pBAnalysis
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePBAnalysis() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        int databaseSizeBeforeUpdate = pBAnalysisRepository.findAll().size();

        // Update the pBAnalysis
        PBAnalysis updatedPBAnalysis = pBAnalysisRepository.findById(pBAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedPBAnalysis are not directly saved in db
        em.detach(updatedPBAnalysis);
        updatedPBAnalysis
            .threshold(UPDATED_THRESHOLD)
            .protocolId(OTHER_PROTOCOL_ID)
            .sampleId(OTHER_SAMPLE_ID);
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(updatedPBAnalysis);

        restPBAnalysisMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isOk());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeUpdate);
        PBAnalysis testPBAnalysis = pBAnalysisList.get(pBAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(UPDATED_THRESHOLD);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(OTHER_PROTOCOL_ID);
        assertThat(testPBAnalysis.getSampleId()).isEqualTo(OTHER_SAMPLE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPBAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = pBAnalysisRepository.findAll().size();

        // Create the PBAnalysis
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pBAnalysis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPBAnalysisMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePBAnalysis() throws Exception {
        // Initialize the database
        pBAnalysisRepository.saveAndFlush(pBAnalysis);

        int databaseSizeBeforeDelete = pBAnalysisRepository.findAll().size();

        // Delete the pBAnalysis
        restPBAnalysisMockMvc.perform(delete("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pBAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PBAnalysis> pBAnalysisList = pBAnalysisRepository.findAll();
        assertThat(pBAnalysisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
