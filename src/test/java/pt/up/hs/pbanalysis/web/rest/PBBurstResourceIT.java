package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.PBAnalysisApp;
import pt.up.hs.pbanalysis.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.repository.PBBurstRepository;
import pt.up.hs.pbanalysis.service.PBBurstService;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.mapper.PBBurstMapper;
import pt.up.hs.pbanalysis.web.rest.errors.ExceptionTranslator;
import pt.up.hs.pbanalysis.service.PBBurstQueryService;

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
 * Integration tests for the {@link PBBurstResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PBAnalysisApp.class})
public class PBBurstResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;
    private static final Long DEFAULT_SAMPLE_ID = 1001L;
    private static final Long OTHER_SAMPLE_ID = 1002L;
    private static final Long DEFAULT_PROTOCOL_ID = 10001L;
    private static final Long OTHER_PROTOCOL_ID = 10002L;
    private static final Long DEFAULT_ANALYSIS_ID = 100001L;
    private static final Long OTHER_ANALYSIS_ID = 100002L;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Long DEFAULT_PAUSE_DURATION = 0L;
    private static final Long UPDATED_PAUSE_DURATION = 1L;
    private static final Long SMALLER_PAUSE_DURATION = 0L - 1L;

    private static final Long DEFAULT_START_TIME = 0L;
    private static final Long UPDATED_START_TIME = 1L;
    private static final Long SMALLER_START_TIME = 0L - 1L;

    private static final Long DEFAULT_END_TIME = 0L;
    private static final Long UPDATED_END_TIME = 1L;
    private static final Long SMALLER_END_TIME = 0L - 1L;

    private static final Double DEFAULT_START_X = 1D;
    private static final Double UPDATED_START_X = 2D;
    private static final Double SMALLER_START_X = 1D - 1D;

    private static final Double DEFAULT_START_Y = 1D;
    private static final Double UPDATED_START_Y = 2D;
    private static final Double SMALLER_START_Y = 1D - 1D;

    private static final Double DEFAULT_END_X = 1D;
    private static final Double UPDATED_END_X = 2D;
    private static final Double SMALLER_END_X = 1D - 1D;

    private static final Double DEFAULT_END_Y = 1D;
    private static final Double UPDATED_END_Y = 2D;
    private static final Double SMALLER_END_Y = 1D - 1D;

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;
    private static final Double SMALLER_DISTANCE = 1D - 1D;

    private static final Integer DEFAULT_DOT_COUNT = 1;
    private static final Integer UPDATED_DOT_COUNT = 2;
    private static final Integer SMALLER_DOT_COUNT = 1 - 1;

    @Autowired
    private PBBurstRepository pbBurstRepository;

    @Autowired
    private PBBurstMapper pbBurstMapper;

    @Autowired
    private PBBurstService pbBurstService;

    @Autowired
    private PBBurstQueryService pbBurstQueryService;

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

    private MockMvc restPBBurstMockMvc;

    private PBAnalysis pbAnalysis;
    private PBBurst pbBurst;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PBBurstResource pBBurstResource = new PBBurstResource(pbBurstService, pbBurstQueryService);
        this.restPBBurstMockMvc = MockMvcBuilders.standaloneSetup(pBBurstResource)
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
     *
     * @param pbAnalysis {@link PBAnalysis} Pause-burst analysis
     */
    public static PBBurst createEntity(PBAnalysis pbAnalysis) {
        return new PBBurst()
            .analysis(pbAnalysis)
            .text(DEFAULT_TEXT)
            .pauseDuration(DEFAULT_PAUSE_DURATION)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .startX(DEFAULT_START_X)
            .startY(DEFAULT_START_Y)
            .endX(DEFAULT_END_X)
            .endY(DEFAULT_END_Y)
            .distance(DEFAULT_DISTANCE)
            .dotCount(DEFAULT_DOT_COUNT);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *
     * @param pbAnalysis {@link PBAnalysis} Pause-burst analysis
     */
    public static PBBurst createUpdatedEntity(PBAnalysis pbAnalysis) {
        return new PBBurst()
            .analysis(pbAnalysis)
            .text(UPDATED_TEXT)
            .pauseDuration(UPDATED_PAUSE_DURATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .startX(UPDATED_START_X)
            .startY(UPDATED_START_Y)
            .endX(UPDATED_END_X)
            .endY(UPDATED_END_Y)
            .distance(UPDATED_DISTANCE)
            .dotCount(UPDATED_DOT_COUNT);
    }

    public static PBAnalysis getPBAnalysis(EntityManager em) {
        PBAnalysis pbAnalysis;
        if (TestUtil.findAll(em, PBAnalysis.class).isEmpty()) {
            pbAnalysis = PBAnalysisResourceIT.createEntity(em);
            em.persist(pbAnalysis);
            em.flush();
        } else {
            pbAnalysis = TestUtil.findAll(em, PBAnalysis.class).get(0);
        }
        return pbAnalysis;
    }

    @BeforeEach
    public void initTest() {
        pbAnalysis = getPBAnalysis(em);
        pbBurst = createEntity(pbAnalysis);
    }

    @Test
    @Transactional
    public void createPBBurst() throws Exception {
        int databaseSizeBeforeCreate = pbBurstRepository.findAll().size();

        // Create the PBBurst
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);
        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isCreated());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeCreate + 1);
        PBBurst testPBBurst = pBBurstList.get(pBBurstList.size() - 1);
        assertThat(testPBBurst.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testPBBurst.getPauseDuration()).isEqualTo(DEFAULT_PAUSE_DURATION);
        assertThat(testPBBurst.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testPBBurst.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testPBBurst.getStartX()).isEqualTo(DEFAULT_START_X);
        assertThat(testPBBurst.getStartY()).isEqualTo(DEFAULT_START_Y);
        assertThat(testPBBurst.getEndX()).isEqualTo(DEFAULT_END_X);
        assertThat(testPBBurst.getEndY()).isEqualTo(DEFAULT_END_Y);
        assertThat(testPBBurst.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testPBBurst.getDotCount()).isEqualTo(DEFAULT_DOT_COUNT);
    }

    @Test
    @Transactional
    public void createPBBurstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pbBurstRepository.findAll().size();

        // Create the PBBurst with an existing ID
        pbBurst.setId(1L);
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPauseDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setPauseDuration(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setStartTime(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setEndTime(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartXIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setStartX(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartYIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setStartY(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndXIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setEndX(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndYIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setEndY(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDotCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = pbBurstRepository.findAll().size();
        // set the field null
        pbBurst.setDotCount(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        restPBBurstMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPBBursts() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts?sort=id,desc", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pbBurst.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].pauseDuration").value(hasItem(DEFAULT_PAUSE_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
            .andExpect(jsonPath("$.[*].startX").value(hasItem(DEFAULT_START_X)))
            .andExpect(jsonPath("$.[*].startY").value(hasItem(DEFAULT_START_Y)))
            .andExpect(jsonPath("$.[*].endX").value(hasItem(DEFAULT_END_X)))
            .andExpect(jsonPath("$.[*].endY").value(hasItem(DEFAULT_END_Y)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].dotCount").value(hasItem(DEFAULT_DOT_COUNT)));
    }

    @Test
    @Transactional
    public void getPBBurst() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get the pBBurst
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId(), pbBurst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pbBurst.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.pauseDuration").value(DEFAULT_PAUSE_DURATION.intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()))
            .andExpect(jsonPath("$.startX").value(DEFAULT_START_X))
            .andExpect(jsonPath("$.startY").value(DEFAULT_START_Y))
            .andExpect(jsonPath("$.endX").value(DEFAULT_END_X))
            .andExpect(jsonPath("$.endY").value(DEFAULT_END_Y))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE))
            .andExpect(jsonPath("$.dotCount").value(DEFAULT_DOT_COUNT));
    }


    @Test
    @Transactional
    public void getPBBurstsByIdFiltering() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        Long id = pbBurst.getId();

        defaultPBBurstShouldBeFound("id.equals=" + id);
        defaultPBBurstShouldNotBeFound("id.notEquals=" + id);

        defaultPBBurstShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPBBurstShouldNotBeFound("id.greaterThan=" + id);

        defaultPBBurstShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPBBurstShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text equals to DEFAULT_TEXT
        defaultPBBurstShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text equals to UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text not equals to DEFAULT_TEXT
        defaultPBBurstShouldNotBeFound("text.notEquals=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text not equals to UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.notEquals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the pBBurstList where text equals to UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text is not null
        defaultPBBurstShouldBeFound("text.specified=true");

        // Get all the pBBurstList where text is null
        defaultPBBurstShouldNotBeFound("text.specified=false");
    }
                @Test
    @Transactional
    public void getAllPBBurstsByTextContainsSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text contains DEFAULT_TEXT
        defaultPBBurstShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text contains UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextNotContainsSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where text does not contain DEFAULT_TEXT
        defaultPBBurstShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text does not contain UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration equals to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.equals=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.equals=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration not equals to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.notEquals=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration not equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.notEquals=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration in DEFAULT_PAUSE_DURATION or UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.in=" + DEFAULT_PAUSE_DURATION + "," + UPDATED_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.in=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration is not null
        defaultPBBurstShouldBeFound("pauseDuration.specified=true");

        // Get all the pBBurstList where pauseDuration is null
        defaultPBBurstShouldNotBeFound("pauseDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration is greater than or equal to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.greaterThanOrEqual=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is greater than or equal to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.greaterThanOrEqual=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration is less than or equal to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.lessThanOrEqual=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is less than or equal to SMALLER_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.lessThanOrEqual=" + SMALLER_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration is less than DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.lessThan=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is less than UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.lessThan=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where pauseDuration is greater than DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.greaterThan=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is greater than SMALLER_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.greaterThan=" + SMALLER_PAUSE_DURATION);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime equals to DEFAULT_START_TIME
        defaultPBBurstShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime equals to UPDATED_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime not equals to DEFAULT_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime not equals to UPDATED_START_TIME
        defaultPBBurstShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultPBBurstShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the pBBurstList where startTime equals to UPDATED_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime is not null
        defaultPBBurstShouldBeFound("startTime.specified=true");

        // Get all the pBBurstList where startTime is null
        defaultPBBurstShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultPBBurstShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime is greater than or equal to UPDATED_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime is less than or equal to DEFAULT_START_TIME
        defaultPBBurstShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime is less than or equal to SMALLER_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime is less than DEFAULT_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime is less than UPDATED_START_TIME
        defaultPBBurstShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startTime is greater than DEFAULT_START_TIME
        defaultPBBurstShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the pBBurstList where startTime is greater than SMALLER_START_TIME
        defaultPBBurstShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime equals to DEFAULT_END_TIME
        defaultPBBurstShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime equals to UPDATED_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime not equals to DEFAULT_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime not equals to UPDATED_END_TIME
        defaultPBBurstShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultPBBurstShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the pBBurstList where endTime equals to UPDATED_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime is not null
        defaultPBBurstShouldBeFound("endTime.specified=true");

        // Get all the pBBurstList where endTime is null
        defaultPBBurstShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime is greater than or equal to DEFAULT_END_TIME
        defaultPBBurstShouldBeFound("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime is greater than or equal to UPDATED_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime is less than or equal to DEFAULT_END_TIME
        defaultPBBurstShouldBeFound("endTime.lessThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime is less than or equal to SMALLER_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime is less than DEFAULT_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.lessThan=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime is less than UPDATED_END_TIME
        defaultPBBurstShouldBeFound("endTime.lessThan=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endTime is greater than DEFAULT_END_TIME
        defaultPBBurstShouldNotBeFound("endTime.greaterThan=" + DEFAULT_END_TIME);

        // Get all the pBBurstList where endTime is greater than SMALLER_END_TIME
        defaultPBBurstShouldBeFound("endTime.greaterThan=" + SMALLER_END_TIME);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX equals to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.equals=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX equals to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.equals=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX not equals to DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.notEquals=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX not equals to UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.notEquals=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX in DEFAULT_START_X or UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.in=" + DEFAULT_START_X + "," + UPDATED_START_X);

        // Get all the pBBurstList where startX equals to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.in=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX is not null
        defaultPBBurstShouldBeFound("startX.specified=true");

        // Get all the pBBurstList where startX is null
        defaultPBBurstShouldNotBeFound("startX.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX is greater than or equal to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.greaterThanOrEqual=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is greater than or equal to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.greaterThanOrEqual=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX is less than or equal to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.lessThanOrEqual=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is less than or equal to SMALLER_START_X
        defaultPBBurstShouldNotBeFound("startX.lessThanOrEqual=" + SMALLER_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX is less than DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.lessThan=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is less than UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.lessThan=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startX is greater than DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.greaterThan=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is greater than SMALLER_START_X
        defaultPBBurstShouldBeFound("startX.greaterThan=" + SMALLER_START_X);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY equals to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.equals=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY equals to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.equals=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY not equals to DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.notEquals=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY not equals to UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.notEquals=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY in DEFAULT_START_Y or UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.in=" + DEFAULT_START_Y + "," + UPDATED_START_Y);

        // Get all the pBBurstList where startY equals to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.in=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY is not null
        defaultPBBurstShouldBeFound("startY.specified=true");

        // Get all the pBBurstList where startY is null
        defaultPBBurstShouldNotBeFound("startY.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY is greater than or equal to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.greaterThanOrEqual=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is greater than or equal to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.greaterThanOrEqual=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY is less than or equal to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.lessThanOrEqual=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is less than or equal to SMALLER_START_Y
        defaultPBBurstShouldNotBeFound("startY.lessThanOrEqual=" + SMALLER_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY is less than DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.lessThan=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is less than UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.lessThan=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where startY is greater than DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.greaterThan=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is greater than SMALLER_START_Y
        defaultPBBurstShouldBeFound("startY.greaterThan=" + SMALLER_START_Y);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX equals to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.equals=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX equals to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.equals=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX not equals to DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.notEquals=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX not equals to UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.notEquals=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX in DEFAULT_END_X or UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.in=" + DEFAULT_END_X + "," + UPDATED_END_X);

        // Get all the pBBurstList where endX equals to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.in=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX is not null
        defaultPBBurstShouldBeFound("endX.specified=true");

        // Get all the pBBurstList where endX is null
        defaultPBBurstShouldNotBeFound("endX.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX is greater than or equal to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.greaterThanOrEqual=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is greater than or equal to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.greaterThanOrEqual=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX is less than or equal to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.lessThanOrEqual=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is less than or equal to SMALLER_END_X
        defaultPBBurstShouldNotBeFound("endX.lessThanOrEqual=" + SMALLER_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX is less than DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.lessThan=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is less than UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.lessThan=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endX is greater than DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.greaterThan=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is greater than SMALLER_END_X
        defaultPBBurstShouldBeFound("endX.greaterThan=" + SMALLER_END_X);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY equals to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.equals=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY equals to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.equals=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY not equals to DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.notEquals=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY not equals to UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.notEquals=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY in DEFAULT_END_Y or UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.in=" + DEFAULT_END_Y + "," + UPDATED_END_Y);

        // Get all the pBBurstList where endY equals to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.in=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY is not null
        defaultPBBurstShouldBeFound("endY.specified=true");

        // Get all the pBBurstList where endY is null
        defaultPBBurstShouldNotBeFound("endY.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY is greater than or equal to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.greaterThanOrEqual=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is greater than or equal to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.greaterThanOrEqual=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY is less than or equal to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.lessThanOrEqual=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is less than or equal to SMALLER_END_Y
        defaultPBBurstShouldNotBeFound("endY.lessThanOrEqual=" + SMALLER_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY is less than DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.lessThan=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is less than UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.lessThan=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where endY is greater than DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.greaterThan=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is greater than SMALLER_END_Y
        defaultPBBurstShouldBeFound("endY.greaterThan=" + SMALLER_END_Y);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance equals to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.equals=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance equals to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.equals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance not equals to DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.notEquals=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance not equals to UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.notEquals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance in DEFAULT_DISTANCE or UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.in=" + DEFAULT_DISTANCE + "," + UPDATED_DISTANCE);

        // Get all the pBBurstList where distance equals to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.in=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance is not null
        defaultPBBurstShouldBeFound("distance.specified=true");

        // Get all the pBBurstList where distance is null
        defaultPBBurstShouldNotBeFound("distance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance is greater than or equal to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.greaterThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is greater than or equal to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.greaterThanOrEqual=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance is less than or equal to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.lessThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is less than or equal to SMALLER_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.lessThanOrEqual=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance is less than DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.lessThan=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is less than UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.lessThan=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where distance is greater than DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.greaterThan=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is greater than SMALLER_DISTANCE
        defaultPBBurstShouldBeFound("distance.greaterThan=" + SMALLER_DISTANCE);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount equals to DEFAULT_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.equals=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount equals to UPDATED_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.equals=" + UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount not equals to DEFAULT_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.notEquals=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount not equals to UPDATED_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.notEquals=" + UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsInShouldWork() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount in DEFAULT_DOT_COUNT or UPDATED_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.in=" + DEFAULT_DOT_COUNT + "," + UPDATED_DOT_COUNT);

        // Get all the pBBurstList where dotCount equals to UPDATED_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.in=" + UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount is not null
        defaultPBBurstShouldBeFound("dotCount.specified=true");

        // Get all the pBBurstList where dotCount is null
        defaultPBBurstShouldNotBeFound("dotCount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount is greater than or equal to DEFAULT_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.greaterThanOrEqual=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount is greater than or equal to UPDATED_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.greaterThanOrEqual=" + UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount is less than or equal to DEFAULT_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.lessThanOrEqual=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount is less than or equal to SMALLER_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.lessThanOrEqual=" + SMALLER_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsLessThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount is less than DEFAULT_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.lessThan=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount is less than UPDATED_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.lessThan=" + UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDotCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        // Get all the pBBurstList where dotCount is greater than DEFAULT_DOT_COUNT
        defaultPBBurstShouldNotBeFound("dotCount.greaterThan=" + DEFAULT_DOT_COUNT);

        // Get all the pBBurstList where dotCount is greater than SMALLER_DOT_COUNT
        defaultPBBurstShouldBeFound("dotCount.greaterThan=" + SMALLER_DOT_COUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPBBurstShouldBeFound(String filter) throws Exception {
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pbBurst.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].pauseDuration").value(hasItem(DEFAULT_PAUSE_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
            .andExpect(jsonPath("$.[*].startX").value(hasItem(DEFAULT_START_X)))
            .andExpect(jsonPath("$.[*].startY").value(hasItem(DEFAULT_START_Y)))
            .andExpect(jsonPath("$.[*].endX").value(hasItem(DEFAULT_END_X)))
            .andExpect(jsonPath("$.[*].endY").value(hasItem(DEFAULT_END_Y)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.[*].dotCount").value(hasItem(DEFAULT_DOT_COUNT)));

        // Check, that the count call also returns 1
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPBBurstShouldNotBeFound(String filter) throws Exception {
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPBBurst() throws Exception {
        // Get the pBBurst
        restPBBurstMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId(), Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePBBurst() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        int databaseSizeBeforeUpdate = pbBurstRepository.findAll().size();

        // Update the pBBurst
        PBBurst updatedPBBurst = pbBurstRepository.findById(pbBurst.getId()).get();
        // Disconnect from session so that the updates on updatedPBBurst are not directly saved in db
        em.detach(updatedPBBurst);
        updatedPBBurst
            .text(UPDATED_TEXT)
            .pauseDuration(UPDATED_PAUSE_DURATION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .startX(UPDATED_START_X)
            .startY(UPDATED_START_Y)
            .endX(UPDATED_END_X)
            .endY(UPDATED_END_Y)
            .distance(UPDATED_DISTANCE)
            .dotCount(UPDATED_DOT_COUNT);
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(updatedPBBurst);

        restPBBurstMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isOk());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeUpdate);
        PBBurst testPBBurst = pBBurstList.get(pBBurstList.size() - 1);
        assertThat(testPBBurst.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPBBurst.getPauseDuration()).isEqualTo(UPDATED_PAUSE_DURATION);
        assertThat(testPBBurst.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testPBBurst.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testPBBurst.getStartX()).isEqualTo(UPDATED_START_X);
        assertThat(testPBBurst.getStartY()).isEqualTo(UPDATED_START_Y);
        assertThat(testPBBurst.getEndX()).isEqualTo(UPDATED_END_X);
        assertThat(testPBBurst.getEndY()).isEqualTo(UPDATED_END_Y);
        assertThat(testPBBurst.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testPBBurst.getDotCount()).isEqualTo(UPDATED_DOT_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingPBBurst() throws Exception {
        int databaseSizeBeforeUpdate = pbBurstRepository.findAll().size();

        // Create the PBBurst
        PBBurstDTO pBBurstDTO = pbBurstMapper.toDto(pbBurst);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPBBurstMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePBBurst() throws Exception {
        // Initialize the database
        pbBurstRepository.saveAndFlush(pbBurst);

        int databaseSizeBeforeDelete = pbBurstRepository.findAll().size();

        // Delete the pBBurst
        restPBBurstMockMvc.perform(delete("/api/projects/{projectId}/samples/{sampleId}/protocols/{protocolId}/pb-analyses/{analysisId}/pb-bursts/{id}", DEFAULT_PROJECT_ID, DEFAULT_SAMPLE_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId(), pbBurst.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PBBurst> pBBurstList = pbBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
