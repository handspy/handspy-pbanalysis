package pt.up.hs.pbanalysis.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import pt.up.hs.pbanalysis.PBAnalysisApp;
import pt.up.hs.pbanalysis.client.sampling.SamplingFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Dot;
import pt.up.hs.pbanalysis.client.sampling.dto.DotType;
import pt.up.hs.pbanalysis.client.sampling.dto.Protocol;
import pt.up.hs.pbanalysis.client.sampling.dto.Stroke;
import pt.up.hs.pbanalysis.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.PBAnalysisQueryService;
import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;
import pt.up.hs.pbanalysis.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pt.up.hs.pbanalysis.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link PBAnalysisResource} REST controller.
 */
@SpringBootTest(
    classes = {SecurityBeanOverrideConfiguration.class, PBAnalysisApp.class},
    properties = {"feign.hystrix.enabled=true"}
)
public class PBAnalysisResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;
    private static final Long DEFAULT_PROTOCOL_ID = 10001L;
    private static final Long OTHER_PROTOCOL_ID = 10002L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_THRESHOLD = 1L;
    private static final Long UPDATED_THRESHOLD = 150L;
    private static final Long SMALLER_THRESHOLD = 1L - 1L;

    private static final String DEFAULT_CREATED_BY = "system";
    private static final String UPDATED_CREATED_BY = "system";

    private static final String DEFAULT_LAST_MODIFIED_BY = "system";
    private static final String UPDATED_LAST_MODIFIED_BY = "system";

    @Autowired
    private PBAnalysisRepository pbAnalysisRepository;

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

    @MockBean
    private SamplingFeignClient samplingFeignClient;

    private PBAnalysis pbAnalysis;

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

    public Protocol getProtocol() {

        Protocol protocol = new Protocol();

        protocol.setHeight(1000D);
        protocol.setWidth(1000D);

        protocol.setStrokes(getStrokes());

        return protocol;
    }

    public List<Stroke> getStrokes() {

        List<Stroke> strokes = new ArrayList<>();

        Stroke stroke1 = new Stroke();
        stroke1.setStartTime(0L);
        stroke1.setEndTime(999L);
        stroke1.setDots(getRandomDots(5, 0L, 999L));
        strokes.add(stroke1);

        Stroke stroke2 = new Stroke();
        stroke2.setStartTime(1000L);
        stroke2.setEndTime(2499L);
        stroke2.setDots(getRandomDots(10, 1000L, 2499L));
        strokes.add(stroke2);

        Stroke stroke3 = new Stroke();
        stroke3.setStartTime(2500L);
        stroke3.setEndTime(3000L);
        stroke3.setDots(getRandomDots(7, 2500L, 3000L));
        strokes.add(stroke3);

        return strokes;
    }

    public List<Dot> getRandomDots(int count, long startTime, long endTime) {
        List<Dot> dots = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            long captureInterval = (long) (((double) endTime - startTime) / count);
            Dot dot = new Dot();
            dot.setTimestamp(startTime + captureInterval * i);
            dot.setX(ThreadLocalRandom.current().nextDouble(1000D));
            dot.setY(ThreadLocalRandom.current().nextDouble(1000D));
            dot.setPressure(ThreadLocalRandom.current().nextDouble());
            dot.setType(DotType.DOWN);
            dots.add(dot);
        }
        return dots;
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBAnalysis createEntity(EntityManager em) {
        return new PBAnalysis()
            .projectId(DEFAULT_PROJECT_ID)
            .protocolId(DEFAULT_PROTOCOL_ID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .threshold(DEFAULT_THRESHOLD);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PBAnalysis createUpdatedEntity(EntityManager em) {
        return new PBAnalysis()
            .projectId(DEFAULT_PROJECT_ID)
            .protocolId(DEFAULT_PROTOCOL_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .threshold(UPDATED_THRESHOLD);
    }

    @BeforeEach
    public void initTest() {
        pbAnalysis = createEntity(em);
    }

    @Test
    @Transactional
    public void createPBAnalysis() throws Exception {
        int databaseSizeBeforeCreate = pbAnalysisRepository.findAll().size();

        // Create the Pause-Burst Analysis
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pbAnalysis);
        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?analyze=false", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isCreated());

        // Validate the Pause-Burst Analysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        PBAnalysis testPBAnalysis = pbAnalysisList.get(pbAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPBAnalysis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(DEFAULT_THRESHOLD);
    }

    @Test
    @Transactional
    public void createPBAnalysisWithAnalyze() throws Exception {

        when(
            samplingFeignClient.getProtocolData(DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
        ).thenReturn(getProtocol());

        int databaseSizeBeforeCreate = pbAnalysisRepository.findAll().size();

        // Create the Pause-Burst Analysis
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pbAnalysis);
        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isCreated());

        // Validate the Pause-Burst Analysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeCreate + 1);
        PBAnalysis testPBAnalysis = pbAnalysisList.get(pbAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPBAnalysis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(DEFAULT_THRESHOLD);
        assertThat(testPBAnalysis.getBursts()).hasSize(22);
    }

    @Test
    @Transactional
    public void createPBAnalysisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pbAnalysisRepository.findAll().size();

        // Create the PBAnalysis with an existing ID
        pbAnalysis.setId(1L);
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pbAnalysis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProtocolIdIsFilledByPathParam() throws Exception {
        int databaseSizeBeforeTest = pbAnalysisRepository.findAll().size();

        // set the field null
        pbAnalysis.setProtocolId(null);

        // Create the PB Analysis, which fails.
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pbAnalysis);

        restPBAnalysisMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?analyze=false", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().is2xxSuccessful());

        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeTest + 1);

        PBAnalysis testPBAnalysis = pbAnalysisList.get(pbAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPBAnalysis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(DEFAULT_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalyses() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?sort=id,desc", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pbAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].protocolId").value(hasItem(DEFAULT_PROTOCOL_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    public void getPBAnalysis() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get the pBAnalysis
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pbAnalysis.getId().intValue()))
            .andExpect(jsonPath("$.protocolId").value(DEFAULT_PROTOCOL_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.threshold").value(DEFAULT_THRESHOLD.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    @Transactional
    public void getPBAnalysesByIdFiltering() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        Long id = pbAnalysis.getId();

        defaultPBAnalysisShouldBeFound("id.equals=" + id);
        defaultPBAnalysisShouldNotBeFound("id.notEquals=" + id);

        defaultPBAnalysisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPBAnalysisShouldNotBeFound("id.greaterThan=" + id);

        defaultPBAnalysisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPBAnalysisShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name equals to DEFAULT_NAME
        defaultPBAnalysisShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pbAnalysisList where name equals to UPDATED_NAME
        defaultPBAnalysisShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name not equals to DEFAULT_NAME
        defaultPBAnalysisShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the pbAnalysisList where name not equals to UPDATED_NAME
        defaultPBAnalysisShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPBAnalysisShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pbAnalysisList where name equals to UPDATED_NAME
        defaultPBAnalysisShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name is not null
        defaultPBAnalysisShouldBeFound("name.specified=true");

        // Get all the pbAnalysisList where name is null
        defaultPBAnalysisShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameContainsSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name contains DEFAULT_NAME
        defaultPBAnalysisShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pbAnalysisList where name contains UPDATED_NAME
        defaultPBAnalysisShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where name does not contain DEFAULT_NAME
        defaultPBAnalysisShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pbAnalysisList where name does not contain UPDATED_NAME
        defaultPBAnalysisShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description equals to DEFAULT_DESCRIPTION
        defaultPBAnalysisShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the pbAnalysisList where description equals to UPDATED_DESCRIPTION
        defaultPBAnalysisShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description not equals to DEFAULT_DESCRIPTION
        defaultPBAnalysisShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the pbAnalysisList where description not equals to UPDATED_DESCRIPTION
        defaultPBAnalysisShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPBAnalysisShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the pbAnalysisList where description equals to UPDATED_DESCRIPTION
        defaultPBAnalysisShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description is not null
        defaultPBAnalysisShouldBeFound("description.specified=true");

        // Get all the pbAnalysisList where description is null
        defaultPBAnalysisShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description contains DEFAULT_DESCRIPTION
        defaultPBAnalysisShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the pbAnalysisList where description contains UPDATED_DESCRIPTION
        defaultPBAnalysisShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where description does not contain DEFAULT_DESCRIPTION
        defaultPBAnalysisShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the pbAnalysisList where description does not contain UPDATED_DESCRIPTION
        defaultPBAnalysisShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold equals to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.equals=" + DEFAULT_THRESHOLD);

        // Get all the pbAnalysisList where threshold equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.equals=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold not equals to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.notEquals=" + DEFAULT_THRESHOLD);

        // Get all the pbAnalysisList where threshold not equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.notEquals=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold in DEFAULT_THRESHOLD or UPDATED_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.in=" + DEFAULT_THRESHOLD + "," + UPDATED_THRESHOLD);

        // Get all the pbAnalysisList where threshold equals to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.in=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold is not null
        defaultPBAnalysisShouldBeFound("threshold.specified=true");

        // Get all the pbAnalysisList where threshold is null
        defaultPBAnalysisShouldNotBeFound("threshold.specified=false");
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold is greater than or equal to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.greaterThanOrEqual=" + DEFAULT_THRESHOLD);

        // Get all the pbAnalysisList where threshold is greater than or equal to UPDATED_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.greaterThanOrEqual=" + UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        // Get all the pbAnalysisList where threshold is less than or equal to DEFAULT_THRESHOLD
        defaultPBAnalysisShouldBeFound("threshold.lessThanOrEqual=" + DEFAULT_THRESHOLD);

        // Get all the pbAnalysisList where threshold is less than or equal to SMALLER_THRESHOLD
        defaultPBAnalysisShouldNotBeFound("threshold.lessThanOrEqual=" + SMALLER_THRESHOLD);
    }

    @Test
    @Transactional
    public void getAllPBAnalysesByBurstsIsEqualToSomething() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);
        PBBurst bursts = PBBurstResourceIT.createEntity(pbAnalysis);
        em.persist(bursts);
        em.flush();
        pbAnalysis.addBursts(bursts);
        pbAnalysisRepository.saveAndFlush(pbAnalysis);
        Long burstsId = bursts.getId();

        // Get all the pbAnalysisList where bursts equals to burstsId
        defaultPBAnalysisShouldBeFound("burstsId.equals=" + burstsId);

        // Get all the pbAnalysisList where bursts equals to burstsId + 1
        defaultPBAnalysisShouldNotBeFound("burstsId.equals=" + (burstsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPBAnalysisShouldBeFound(String filter) throws Exception {
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pbAnalysis.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].protocolId").value(hasItem(DEFAULT_PROTOCOL_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].threshold").value(hasItem(DEFAULT_THRESHOLD.intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPBAnalysisShouldNotBeFound(String filter) throws Exception {
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPBAnalysis() throws Exception {
        // Get the pBAnalysis
        restPBAnalysisMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePBAnalysis() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        int databaseSizeBeforeUpdate = pbAnalysisRepository.findAll().size();

        // Update the pBAnalysis
        PBAnalysis updatedPBAnalysis = pbAnalysisRepository.findById(pbAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedPBAnalysis are not directly saved in db
        em.detach(updatedPBAnalysis);
        updatedPBAnalysis
            .projectId(OTHER_PROJECT_ID)
            .protocolId(OTHER_PROTOCOL_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .threshold(UPDATED_THRESHOLD);
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(updatedPBAnalysis);

        restPBAnalysisMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isOk());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeUpdate);
        PBAnalysis testPBAnalysis = pbAnalysisList.get(pbAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPBAnalysis.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(UPDATED_THRESHOLD);
    }

    @Test
    @Transactional
    public void updatePBAnalysisWithAnalyze() throws Exception {

        when(
            samplingFeignClient.getProtocolData(DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
        ).thenReturn(getProtocol());

        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        int databaseSizeBeforeUpdate = pbAnalysisRepository.findAll().size();

        // Update the pBAnalysis
        PBAnalysis updatedPBAnalysis = pbAnalysisRepository.findById(pbAnalysis.getId()).get();
        // Disconnect from session so that the updates on updatedPBAnalysis are not directly saved in db
        em.detach(updatedPBAnalysis);
        updatedPBAnalysis
            .projectId(OTHER_PROJECT_ID)
            .protocolId(OTHER_PROTOCOL_ID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .threshold(UPDATED_THRESHOLD);
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(updatedPBAnalysis);

        restPBAnalysisMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses?analyze=true", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isOk());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeUpdate);
        PBAnalysis testPBAnalysis = pbAnalysisList.get(pbAnalysisList.size() - 1);
        assertThat(testPBAnalysis.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testPBAnalysis.getProtocolId()).isEqualTo(DEFAULT_PROTOCOL_ID);
        assertThat(testPBAnalysis.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPBAnalysis.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPBAnalysis.getThreshold()).isEqualTo(UPDATED_THRESHOLD);
        assertThat(testPBAnalysis.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPBAnalysis.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPBAnalysis.getBursts()).hasSize(7);
    }

    @Test
    @Transactional
    public void updateNonExistingPBAnalysis() throws Exception {
        int databaseSizeBeforeUpdate = pbAnalysisRepository.findAll().size();

        // Create the PBAnalysis
        PBAnalysisDTO pBAnalysisDTO = pBAnalysisMapper.toDto(pbAnalysis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPBAnalysisMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(pBAnalysisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PBAnalysis in the database
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePBAnalysis() throws Exception {
        // Initialize the database
        pbAnalysisRepository.saveAndFlush(pbAnalysis);

        int databaseSizeBeforeDelete = pbAnalysisRepository.findAll().size();

        // Delete the pBAnalysis
        restPBAnalysisMockMvc.perform(delete("/api/projects/{projectId}/protocols/{protocolId}/pb-analyses/{id}", DEFAULT_PROJECT_ID, DEFAULT_PROTOCOL_ID, pbAnalysis.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PBAnalysis> pbAnalysisList = pbAnalysisRepository.findAll();
        assertThat(pbAnalysisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
