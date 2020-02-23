package pt.up.hs.pbanalysis.web.rest;

import pt.up.hs.pbanalysis.PbanalysisApp;
import pt.up.hs.pbanalysis.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.repository.PBBurstRepository;
import pt.up.hs.pbanalysis.service.PBBurstService;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.mapper.PBBurstMapper;
import pt.up.hs.pbanalysis.web.rest.errors.ExceptionTranslator;
import pt.up.hs.pbanalysis.service.dto.PBBurstCriteria;
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
import java.time.Duration;
import java.util.List;

import static pt.up.hs.pbanalysis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PBBurstResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, PbanalysisApp.class})
public class PBBurstResourceIT {

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_DURATION = Duration.ofHours(5);

    private static final Duration DEFAULT_PAUSE_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_PAUSE_DURATION = Duration.ofHours(12);
    private static final Duration SMALLER_PAUSE_DURATION = Duration.ofHours(5);

    private static final Integer DEFAULT_START_X = 1;
    private static final Integer UPDATED_START_X = 2;
    private static final Integer SMALLER_START_X = 1 - 1;

    private static final Integer DEFAULT_START_Y = 1;
    private static final Integer UPDATED_START_Y = 2;
    private static final Integer SMALLER_START_Y = 1 - 1;

    private static final Integer DEFAULT_END_X = 1;
    private static final Integer UPDATED_END_X = 2;
    private static final Integer SMALLER_END_X = 1 - 1;

    private static final Integer DEFAULT_END_Y = 1;
    private static final Integer UPDATED_END_Y = 2;
    private static final Integer SMALLER_END_Y = 1 - 1;

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;
    private static final Double SMALLER_DISTANCE = 1D - 1D;

    private static final Double DEFAULT_AVG_SPEED = 1D;
    private static final Double UPDATED_AVG_SPEED = 2D;
    private static final Double SMALLER_AVG_SPEED = 1D - 1D;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private PBBurstRepository pBBurstRepository;

    @Autowired
    private PBBurstMapper pBBurstMapper;

    @Autowired
    private PBBurstService pBBurstService;

    @Autowired
    private PBBurstQueryService pBBurstQueryService;

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

    private PBBurst pBBurst;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PBBurstResource pBBurstResource = new PBBurstResource(pBBurstService, pBBurstQueryService);
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
     */
    public static PBBurst createEntity(EntityManager em) {
        PBBurst pBBurst = new PBBurst()
            .duration(DEFAULT_DURATION)
            .pauseDuration(DEFAULT_PAUSE_DURATION)
            .startX(DEFAULT_START_X)
            .startY(DEFAULT_START_Y)
            .endX(DEFAULT_END_X)
            .endY(DEFAULT_END_Y)
            .distance(DEFAULT_DISTANCE)
            .avgSpeed(DEFAULT_AVG_SPEED)
            .text(DEFAULT_TEXT);
        return pBBurst;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBBurst createUpdatedEntity(EntityManager em) {
        PBBurst pBBurst = new PBBurst()
            .duration(UPDATED_DURATION)
            .pauseDuration(UPDATED_PAUSE_DURATION)
            .startX(UPDATED_START_X)
            .startY(UPDATED_START_Y)
            .endX(UPDATED_END_X)
            .endY(UPDATED_END_Y)
            .distance(UPDATED_DISTANCE)
            .avgSpeed(UPDATED_AVG_SPEED)
            .text(UPDATED_TEXT);
        return pBBurst;
    }

    @BeforeEach
    public void initTest() {
        pBBurst = createEntity(em);
    }

    @Test
    @Transactional
    public void createPBBurst() throws Exception {
        int databaseSizeBeforeCreate = pBBurstRepository.findAll().size();

        // Create the PBBurst
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);
        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isCreated());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeCreate + 1);
        PBBurst testPBBurst = pBBurstList.get(pBBurstList.size() - 1);
        assertThat(testPBBurst.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testPBBurst.getPauseDuration()).isEqualTo(DEFAULT_PAUSE_DURATION);
        assertThat(testPBBurst.getStartX()).isEqualTo(DEFAULT_START_X);
        assertThat(testPBBurst.getStartY()).isEqualTo(DEFAULT_START_Y);
        assertThat(testPBBurst.getEndX()).isEqualTo(DEFAULT_END_X);
        assertThat(testPBBurst.getEndY()).isEqualTo(DEFAULT_END_Y);
        assertThat(testPBBurst.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testPBBurst.getAvgSpeed()).isEqualTo(DEFAULT_AVG_SPEED);
        assertThat(testPBBurst.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createPBBurstWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pBBurstRepository.findAll().size();

        // Create the PBBurst with an existing ID
        pBBurst.setId(1L);
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setDuration(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPauseDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setPauseDuration(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartXIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setStartX(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartYIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setStartY(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndXIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setEndX(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndYIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setEndY(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setDistance(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAvgSpeedIsRequired() throws Exception {
        int databaseSizeBeforeTest = pBBurstRepository.findAll().size();
        // set the field null
        pBBurst.setAvgSpeed(null);

        // Create the PBBurst, which fails.
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        restPBBurstMockMvc.perform(post("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPBBursts() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList
        restPBBurstMockMvc.perform(get("/api/pb-bursts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBBurst.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].pauseDuration").value(hasItem(DEFAULT_PAUSE_DURATION.toString())))
            .andExpect(jsonPath("$.[*].startX").value(hasItem(DEFAULT_START_X)))
            .andExpect(jsonPath("$.[*].startY").value(hasItem(DEFAULT_START_Y)))
            .andExpect(jsonPath("$.[*].endX").value(hasItem(DEFAULT_END_X)))
            .andExpect(jsonPath("$.[*].endY").value(hasItem(DEFAULT_END_Y)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].avgSpeed").value(hasItem(DEFAULT_AVG_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }
    
    @Test
    @Transactional
    public void getPBBurst() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get the pBBurst
        restPBBurstMockMvc.perform(get("/api/pb-bursts/{id}", pBBurst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pBBurst.getId().intValue()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.pauseDuration").value(DEFAULT_PAUSE_DURATION.toString()))
            .andExpect(jsonPath("$.startX").value(DEFAULT_START_X))
            .andExpect(jsonPath("$.startY").value(DEFAULT_START_Y))
            .andExpect(jsonPath("$.endX").value(DEFAULT_END_X))
            .andExpect(jsonPath("$.endY").value(DEFAULT_END_Y))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.avgSpeed").value(DEFAULT_AVG_SPEED.doubleValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }


    @Test
    @Transactional
    public void getPBBurstsByIdFiltering() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        Long id = pBBurst.getId();

        defaultPBBurstShouldBeFound("id.equals=" + id);
        defaultPBBurstShouldNotBeFound("id.notEquals=" + id);

        defaultPBBurstShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPBBurstShouldNotBeFound("id.greaterThan=" + id);

        defaultPBBurstShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPBBurstShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration equals to DEFAULT_DURATION
        defaultPBBurstShouldBeFound("duration.equals=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration equals to UPDATED_DURATION
        defaultPBBurstShouldNotBeFound("duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration not equals to DEFAULT_DURATION
        defaultPBBurstShouldNotBeFound("duration.notEquals=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration not equals to UPDATED_DURATION
        defaultPBBurstShouldBeFound("duration.notEquals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration in DEFAULT_DURATION or UPDATED_DURATION
        defaultPBBurstShouldBeFound("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION);

        // Get all the pBBurstList where duration equals to UPDATED_DURATION
        defaultPBBurstShouldNotBeFound("duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration is not null
        defaultPBBurstShouldBeFound("duration.specified=true");

        // Get all the pBBurstList where duration is null
        defaultPBBurstShouldNotBeFound("duration.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration is greater than or equal to DEFAULT_DURATION
        defaultPBBurstShouldBeFound("duration.greaterThanOrEqual=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration is greater than or equal to UPDATED_DURATION
        defaultPBBurstShouldNotBeFound("duration.greaterThanOrEqual=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration is less than or equal to DEFAULT_DURATION
        defaultPBBurstShouldBeFound("duration.lessThanOrEqual=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration is less than or equal to SMALLER_DURATION
        defaultPBBurstShouldNotBeFound("duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration is less than DEFAULT_DURATION
        defaultPBBurstShouldNotBeFound("duration.lessThan=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration is less than UPDATED_DURATION
        defaultPBBurstShouldBeFound("duration.lessThan=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where duration is greater than DEFAULT_DURATION
        defaultPBBurstShouldNotBeFound("duration.greaterThan=" + DEFAULT_DURATION);

        // Get all the pBBurstList where duration is greater than SMALLER_DURATION
        defaultPBBurstShouldBeFound("duration.greaterThan=" + SMALLER_DURATION);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration equals to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.equals=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.equals=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration not equals to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.notEquals=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration not equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.notEquals=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration in DEFAULT_PAUSE_DURATION or UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.in=" + DEFAULT_PAUSE_DURATION + "," + UPDATED_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration equals to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.in=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration is not null
        defaultPBBurstShouldBeFound("pauseDuration.specified=true");

        // Get all the pBBurstList where pauseDuration is null
        defaultPBBurstShouldNotBeFound("pauseDuration.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration is greater than or equal to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.greaterThanOrEqual=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is greater than or equal to UPDATED_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.greaterThanOrEqual=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration is less than or equal to DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.lessThanOrEqual=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is less than or equal to SMALLER_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.lessThanOrEqual=" + SMALLER_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration is less than DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.lessThan=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is less than UPDATED_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.lessThan=" + UPDATED_PAUSE_DURATION);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByPauseDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where pauseDuration is greater than DEFAULT_PAUSE_DURATION
        defaultPBBurstShouldNotBeFound("pauseDuration.greaterThan=" + DEFAULT_PAUSE_DURATION);

        // Get all the pBBurstList where pauseDuration is greater than SMALLER_PAUSE_DURATION
        defaultPBBurstShouldBeFound("pauseDuration.greaterThan=" + SMALLER_PAUSE_DURATION);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX equals to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.equals=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX equals to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.equals=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX not equals to DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.notEquals=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX not equals to UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.notEquals=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX in DEFAULT_START_X or UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.in=" + DEFAULT_START_X + "," + UPDATED_START_X);

        // Get all the pBBurstList where startX equals to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.in=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX is not null
        defaultPBBurstShouldBeFound("startX.specified=true");

        // Get all the pBBurstList where startX is null
        defaultPBBurstShouldNotBeFound("startX.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX is greater than or equal to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.greaterThanOrEqual=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is greater than or equal to UPDATED_START_X
        defaultPBBurstShouldNotBeFound("startX.greaterThanOrEqual=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX is less than or equal to DEFAULT_START_X
        defaultPBBurstShouldBeFound("startX.lessThanOrEqual=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is less than or equal to SMALLER_START_X
        defaultPBBurstShouldNotBeFound("startX.lessThanOrEqual=" + SMALLER_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX is less than DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.lessThan=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is less than UPDATED_START_X
        defaultPBBurstShouldBeFound("startX.lessThan=" + UPDATED_START_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startX is greater than DEFAULT_START_X
        defaultPBBurstShouldNotBeFound("startX.greaterThan=" + DEFAULT_START_X);

        // Get all the pBBurstList where startX is greater than SMALLER_START_X
        defaultPBBurstShouldBeFound("startX.greaterThan=" + SMALLER_START_X);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY equals to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.equals=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY equals to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.equals=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY not equals to DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.notEquals=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY not equals to UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.notEquals=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY in DEFAULT_START_Y or UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.in=" + DEFAULT_START_Y + "," + UPDATED_START_Y);

        // Get all the pBBurstList where startY equals to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.in=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY is not null
        defaultPBBurstShouldBeFound("startY.specified=true");

        // Get all the pBBurstList where startY is null
        defaultPBBurstShouldNotBeFound("startY.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY is greater than or equal to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.greaterThanOrEqual=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is greater than or equal to UPDATED_START_Y
        defaultPBBurstShouldNotBeFound("startY.greaterThanOrEqual=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY is less than or equal to DEFAULT_START_Y
        defaultPBBurstShouldBeFound("startY.lessThanOrEqual=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is less than or equal to SMALLER_START_Y
        defaultPBBurstShouldNotBeFound("startY.lessThanOrEqual=" + SMALLER_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY is less than DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.lessThan=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is less than UPDATED_START_Y
        defaultPBBurstShouldBeFound("startY.lessThan=" + UPDATED_START_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByStartYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where startY is greater than DEFAULT_START_Y
        defaultPBBurstShouldNotBeFound("startY.greaterThan=" + DEFAULT_START_Y);

        // Get all the pBBurstList where startY is greater than SMALLER_START_Y
        defaultPBBurstShouldBeFound("startY.greaterThan=" + SMALLER_START_Y);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX equals to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.equals=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX equals to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.equals=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX not equals to DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.notEquals=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX not equals to UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.notEquals=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX in DEFAULT_END_X or UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.in=" + DEFAULT_END_X + "," + UPDATED_END_X);

        // Get all the pBBurstList where endX equals to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.in=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX is not null
        defaultPBBurstShouldBeFound("endX.specified=true");

        // Get all the pBBurstList where endX is null
        defaultPBBurstShouldNotBeFound("endX.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX is greater than or equal to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.greaterThanOrEqual=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is greater than or equal to UPDATED_END_X
        defaultPBBurstShouldNotBeFound("endX.greaterThanOrEqual=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX is less than or equal to DEFAULT_END_X
        defaultPBBurstShouldBeFound("endX.lessThanOrEqual=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is less than or equal to SMALLER_END_X
        defaultPBBurstShouldNotBeFound("endX.lessThanOrEqual=" + SMALLER_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX is less than DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.lessThan=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is less than UPDATED_END_X
        defaultPBBurstShouldBeFound("endX.lessThan=" + UPDATED_END_X);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endX is greater than DEFAULT_END_X
        defaultPBBurstShouldNotBeFound("endX.greaterThan=" + DEFAULT_END_X);

        // Get all the pBBurstList where endX is greater than SMALLER_END_X
        defaultPBBurstShouldBeFound("endX.greaterThan=" + SMALLER_END_X);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY equals to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.equals=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY equals to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.equals=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY not equals to DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.notEquals=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY not equals to UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.notEquals=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY in DEFAULT_END_Y or UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.in=" + DEFAULT_END_Y + "," + UPDATED_END_Y);

        // Get all the pBBurstList where endY equals to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.in=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY is not null
        defaultPBBurstShouldBeFound("endY.specified=true");

        // Get all the pBBurstList where endY is null
        defaultPBBurstShouldNotBeFound("endY.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY is greater than or equal to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.greaterThanOrEqual=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is greater than or equal to UPDATED_END_Y
        defaultPBBurstShouldNotBeFound("endY.greaterThanOrEqual=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY is less than or equal to DEFAULT_END_Y
        defaultPBBurstShouldBeFound("endY.lessThanOrEqual=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is less than or equal to SMALLER_END_Y
        defaultPBBurstShouldNotBeFound("endY.lessThanOrEqual=" + SMALLER_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY is less than DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.lessThan=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is less than UPDATED_END_Y
        defaultPBBurstShouldBeFound("endY.lessThan=" + UPDATED_END_Y);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByEndYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where endY is greater than DEFAULT_END_Y
        defaultPBBurstShouldNotBeFound("endY.greaterThan=" + DEFAULT_END_Y);

        // Get all the pBBurstList where endY is greater than SMALLER_END_Y
        defaultPBBurstShouldBeFound("endY.greaterThan=" + SMALLER_END_Y);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance equals to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.equals=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance equals to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.equals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance not equals to DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.notEquals=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance not equals to UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.notEquals=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance in DEFAULT_DISTANCE or UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.in=" + DEFAULT_DISTANCE + "," + UPDATED_DISTANCE);

        // Get all the pBBurstList where distance equals to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.in=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance is not null
        defaultPBBurstShouldBeFound("distance.specified=true");

        // Get all the pBBurstList where distance is null
        defaultPBBurstShouldNotBeFound("distance.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance is greater than or equal to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.greaterThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is greater than or equal to UPDATED_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.greaterThanOrEqual=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance is less than or equal to DEFAULT_DISTANCE
        defaultPBBurstShouldBeFound("distance.lessThanOrEqual=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is less than or equal to SMALLER_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.lessThanOrEqual=" + SMALLER_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance is less than DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.lessThan=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is less than UPDATED_DISTANCE
        defaultPBBurstShouldBeFound("distance.lessThan=" + UPDATED_DISTANCE);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByDistanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where distance is greater than DEFAULT_DISTANCE
        defaultPBBurstShouldNotBeFound("distance.greaterThan=" + DEFAULT_DISTANCE);

        // Get all the pBBurstList where distance is greater than SMALLER_DISTANCE
        defaultPBBurstShouldBeFound("distance.greaterThan=" + SMALLER_DISTANCE);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed equals to DEFAULT_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.equals=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed equals to UPDATED_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.equals=" + UPDATED_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed not equals to DEFAULT_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.notEquals=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed not equals to UPDATED_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.notEquals=" + UPDATED_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed in DEFAULT_AVG_SPEED or UPDATED_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.in=" + DEFAULT_AVG_SPEED + "," + UPDATED_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed equals to UPDATED_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.in=" + UPDATED_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed is not null
        defaultPBBurstShouldBeFound("avgSpeed.specified=true");

        // Get all the pBBurstList where avgSpeed is null
        defaultPBBurstShouldNotBeFound("avgSpeed.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed is greater than or equal to DEFAULT_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.greaterThanOrEqual=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed is greater than or equal to UPDATED_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.greaterThanOrEqual=" + UPDATED_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed is less than or equal to DEFAULT_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.lessThanOrEqual=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed is less than or equal to SMALLER_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.lessThanOrEqual=" + SMALLER_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsLessThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed is less than DEFAULT_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.lessThan=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed is less than UPDATED_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.lessThan=" + UPDATED_AVG_SPEED);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByAvgSpeedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where avgSpeed is greater than DEFAULT_AVG_SPEED
        defaultPBBurstShouldNotBeFound("avgSpeed.greaterThan=" + DEFAULT_AVG_SPEED);

        // Get all the pBBurstList where avgSpeed is greater than SMALLER_AVG_SPEED
        defaultPBBurstShouldBeFound("avgSpeed.greaterThan=" + SMALLER_AVG_SPEED);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text equals to DEFAULT_TEXT
        defaultPBBurstShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text equals to UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text not equals to DEFAULT_TEXT
        defaultPBBurstShouldNotBeFound("text.notEquals=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text not equals to UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.notEquals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the pBBurstList where text equals to UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text is not null
        defaultPBBurstShouldBeFound("text.specified=true");

        // Get all the pBBurstList where text is null
        defaultPBBurstShouldNotBeFound("text.specified=false");
    }
                @Test
    @Transactional
    public void getAllPBBurstsByTextContainsSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text contains DEFAULT_TEXT
        defaultPBBurstShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text contains UPDATED_TEXT
        defaultPBBurstShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllPBBurstsByTextNotContainsSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        // Get all the pBBurstList where text does not contain DEFAULT_TEXT
        defaultPBBurstShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the pBBurstList where text does not contain UPDATED_TEXT
        defaultPBBurstShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }


    @Test
    @Transactional
    public void getAllPBBurstsByAnalysisIsEqualToSomething() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);
        PBAnalysis analysis = PBAnalysisResourceIT.createEntity(em);
        em.persist(analysis);
        em.flush();
        pBBurst.setAnalysis(analysis);
        pBBurstRepository.saveAndFlush(pBBurst);
        Long analysisId = analysis.getId();

        // Get all the pBBurstList where analysis equals to analysisId
        defaultPBBurstShouldBeFound("analysisId.equals=" + analysisId);

        // Get all the pBBurstList where analysis equals to analysisId + 1
        defaultPBBurstShouldNotBeFound("analysisId.equals=" + (analysisId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPBBurstShouldBeFound(String filter) throws Exception {
        restPBBurstMockMvc.perform(get("/api/pb-bursts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pBBurst.getId().intValue())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].pauseDuration").value(hasItem(DEFAULT_PAUSE_DURATION.toString())))
            .andExpect(jsonPath("$.[*].startX").value(hasItem(DEFAULT_START_X)))
            .andExpect(jsonPath("$.[*].startY").value(hasItem(DEFAULT_START_Y)))
            .andExpect(jsonPath("$.[*].endX").value(hasItem(DEFAULT_END_X)))
            .andExpect(jsonPath("$.[*].endY").value(hasItem(DEFAULT_END_Y)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].avgSpeed").value(hasItem(DEFAULT_AVG_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));

        // Check, that the count call also returns 1
        restPBBurstMockMvc.perform(get("/api/pb-bursts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPBBurstShouldNotBeFound(String filter) throws Exception {
        restPBBurstMockMvc.perform(get("/api/pb-bursts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPBBurstMockMvc.perform(get("/api/pb-bursts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPBBurst() throws Exception {
        // Get the pBBurst
        restPBBurstMockMvc.perform(get("/api/pb-bursts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePBBurst() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        int databaseSizeBeforeUpdate = pBBurstRepository.findAll().size();

        // Update the pBBurst
        PBBurst updatedPBBurst = pBBurstRepository.findById(pBBurst.getId()).get();
        // Disconnect from session so that the updates on updatedPBBurst are not directly saved in db
        em.detach(updatedPBBurst);
        updatedPBBurst
            .duration(UPDATED_DURATION)
            .pauseDuration(UPDATED_PAUSE_DURATION)
            .startX(UPDATED_START_X)
            .startY(UPDATED_START_Y)
            .endX(UPDATED_END_X)
            .endY(UPDATED_END_Y)
            .distance(UPDATED_DISTANCE)
            .avgSpeed(UPDATED_AVG_SPEED)
            .text(UPDATED_TEXT);
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(updatedPBBurst);

        restPBBurstMockMvc.perform(put("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isOk());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeUpdate);
        PBBurst testPBBurst = pBBurstList.get(pBBurstList.size() - 1);
        assertThat(testPBBurst.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testPBBurst.getPauseDuration()).isEqualTo(UPDATED_PAUSE_DURATION);
        assertThat(testPBBurst.getStartX()).isEqualTo(UPDATED_START_X);
        assertThat(testPBBurst.getStartY()).isEqualTo(UPDATED_START_Y);
        assertThat(testPBBurst.getEndX()).isEqualTo(UPDATED_END_X);
        assertThat(testPBBurst.getEndY()).isEqualTo(UPDATED_END_Y);
        assertThat(testPBBurst.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testPBBurst.getAvgSpeed()).isEqualTo(UPDATED_AVG_SPEED);
        assertThat(testPBBurst.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void updateNonExistingPBBurst() throws Exception {
        int databaseSizeBeforeUpdate = pBBurstRepository.findAll().size();

        // Create the PBBurst
        PBBurstDTO pBBurstDTO = pBBurstMapper.toDto(pBBurst);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPBBurstMockMvc.perform(put("/api/pb-bursts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBBurstDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBBurst in the database
        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePBBurst() throws Exception {
        // Initialize the database
        pBBurstRepository.saveAndFlush(pBBurst);

        int databaseSizeBeforeDelete = pBBurstRepository.findAll().size();

        // Delete the pBBurst
        restPBBurstMockMvc.perform(delete("/api/pb-bursts/{id}", pBBurst.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PBBurst> pBBurstList = pBBurstRepository.findAll();
        assertThat(pBBurstList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
