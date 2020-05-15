package pt.up.hs.pbanalysis.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.pbanalysis.client.sampling.SamplingFeignClient;
import pt.up.hs.pbanalysis.client.sampling.dto.Protocol;
import pt.up.hs.pbanalysis.client.sampling.dto.Stroke;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.mapper.BurstMapper;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;
import pt.up.hs.pbb.builder.PauseBurstBuilder;
import pt.up.hs.pbb.models.Burst;
import pt.up.hs.pbb.reducer.DoubleAverageReducer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PBAnalysis}.
 */
@Service
@Transactional
public class PBAnalysisServiceImpl implements PBAnalysisService {

    private final Logger log = LoggerFactory.getLogger(PBAnalysisServiceImpl.class);

    private final PBAnalysisRepository pbAnalysisRepository;
    private final PBAnalysisMapper pbAnalysisMapper;

    private final SamplingFeignClient samplingFeignClient;
    private final BurstMapper burstMapper;

    public PBAnalysisServiceImpl(
        PBAnalysisRepository pbAnalysisRepository,
        PBAnalysisMapper pbAnalysisMapper,
        SamplingFeignClient samplingFeignClient,
        BurstMapper burstMapper
    ) {
        this.pbAnalysisRepository = pbAnalysisRepository;
        this.pbAnalysisMapper = pbAnalysisMapper;
        this.samplingFeignClient = samplingFeignClient;
        this.burstMapper = burstMapper;
    }

    /**
     * Perform pause-burst analysis and save its result merged with
     * provided {@link PBAnalysisDTO} entity.
     *
     * @param projectId     ID of the project to which the analysis belongs.
     * @param protocolId    ID of the protocol to which the analysis belongs.
     * @param pbAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBAnalysisDTO analyze(
        Long projectId,
        Long protocolId,
        PBAnalysisDTO pbAnalysisDTO
    ) {
        log.debug("Request to analyze pause-burst analysis {} in protocol {} of project {}", pbAnalysisDTO, protocolId, projectId);

        PauseBurstBuilder pbb = new PauseBurstBuilder(pbAnalysisDTO.getThreshold())
            .extraReducer("pressure", DoubleAverageReducer.class);

        Protocol data = samplingFeignClient
            .getProtocolData(projectId, protocolId);

        List<Stroke> strokes = data.getStrokes();

        strokes
            .parallelStream()
            .map(Stroke::getDots)
            .flatMap(Collection::parallelStream)
            .forEachOrdered(dot -> {
                Map<String, Object> extraFeatures = new HashMap<>();
                extraFeatures.put("pressure", dot.getPressure());
                pbb.addCapture(dot.getX(), dot.getY(), dot.getTimestamp(), extraFeatures);
            });

        List<Burst> bursts = pbb.conclude();

        List<PBBurstDTO> pbBurstDTOs = bursts.parallelStream()
            .map(burstMapper::burstToPBBurstDto)
            .collect(Collectors.toList());

        pbAnalysisDTO.setBursts(pbBurstDTOs);

        return save(projectId, protocolId, pbAnalysisDTO);
    }

    /**
     * Save a pause-burst analysis.
     *
     * @param projectId     ID of the project to which the analysis belongs.
     * @param protocolId    ID of the protocol to which the analysis belongs.
     * @param pbAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBAnalysisDTO save(
        Long projectId,
        Long protocolId,
        PBAnalysisDTO pbAnalysisDTO
    ) {
        log.debug("Request to save pause-burst analysis {} of protocol {} of project {}", pbAnalysisDTO, protocolId, projectId);
        PBAnalysis pbAnalysis = pbAnalysisMapper.toEntity(pbAnalysisDTO);
        pbAnalysis.setProjectId(projectId);
        pbAnalysis.setProtocolId(protocolId);
        pbAnalysis = pbAnalysisRepository.save(pbAnalysis);
        return pbAnalysisMapper.toDto(pbAnalysis);
    }

    /**
     * Get all the pBAnalyses.
     *
     * @param projectId  ID of the project to which the analyses belong.
     * @param protocolId ID of the protocol to which the analyses belong.
     * @param pageable   the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PBAnalysisDTO> findAll(
        Long projectId, Long protocolId, Pageable pageable
    ) {
        log.debug("Request to get all pause-burst analyses of protocol {} of project {}", protocolId, projectId);
        return pbAnalysisRepository.findAll(pageable)
            .map(pbAnalysisMapper::toDto);
    }

    /**
     * Get one pBAnalysis by id.
     *
     * @param projectId  ID of the project to which the analysis belongs.
     * @param protocolId ID of the protocol to which the analysis belongs.
     * @param id         the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PBAnalysisDTO> findOne(
        Long projectId, Long protocolId, Long id
    ) {
        log.debug("Request to get pause-burst analysis {} of protocol {} of project {}", id, protocolId, projectId);
        return pbAnalysisRepository.findById(id)
            .map(pbAnalysisMapper::toDto);
    }

    /**
     * Delete the pBAnalysis by id.
     *
     * @param projectId  ID of the project to which the analysis belongs.
     * @param protocolId ID of the protocol to which the analysis belongs.
     * @param id         the id of the entity.
     */
    @Override
    public void delete(
        Long projectId, Long protocolId, Long id
    ) {
        log.debug("Request to delete pause-burst analysis {} of protocol {} of project {}", id, protocolId, projectId);
        pbAnalysisRepository.deleteById(id);
    }
}
