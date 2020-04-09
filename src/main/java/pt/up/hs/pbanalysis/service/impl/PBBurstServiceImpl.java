package pt.up.hs.pbanalysis.service.impl;

import pt.up.hs.pbanalysis.service.PBBurstService;
import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.repository.PBBurstRepository;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.mapper.PBBurstMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PBBurst}.
 */
@Service
@Transactional
public class PBBurstServiceImpl implements PBBurstService {

    private final Logger log = LoggerFactory.getLogger(PBBurstServiceImpl.class);

    private final PBBurstRepository pbBurstRepository;
    private final PBBurstMapper pbBurstMapper;

    public PBBurstServiceImpl(PBBurstRepository pbBurstRepository, PBBurstMapper pbBurstMapper) {
        this.pbBurstRepository = pbBurstRepository;
        this.pbBurstMapper = pbBurstMapper;
    }

    /**
     * Save a pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param sampleId   ID of the sample to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param pbBurstDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBBurstDTO save(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        PBBurstDTO pbBurstDTO
    ) {
        log.debug("Request to save pause-burst burst {} of project {} of sample {} of protocol {} of analysis {}", pbBurstDTO, projectId, sampleId, protocolId, analysisId);
        PBBurst pBBurst = pbBurstMapper.toEntity(pbBurstDTO);
        pBBurst = pbBurstRepository.save(pBBurst);
        return pbBurstMapper.toDto(pBBurst);
    }

    /**
     * Get all the pause-burst bursts.
     *
     * @param projectId  ID of the project to which the bursts belong.
     * @param sampleId   ID of the sample to which the bursts belong.
     * @param protocolId ID of the protocol to which the bursts belong.
     * @param analysisId ID of the analysis to which the bursts belong.
     * @param pageable   the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PBBurstDTO> findAll(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Pageable pageable
    ) {
        log.debug("Request to get all pause-burst bursts of project {} of sample {} of protocol {} of analysis {}", projectId, sampleId, protocolId, analysisId);
        return pbBurstRepository.findAll(pageable)
            .map(pbBurstMapper::toDto);
    }

    /**
     * Get the "id" pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param sampleId   ID of the sample to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param id         the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PBBurstDTO> findOne(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Long id
    ) {
        log.debug("Request to get pause-burst burst {} of project {} of sample {} of protocol {} of analysis {}", id, projectId, sampleId, protocolId, analysisId);
        return pbBurstRepository.findById(id)
            .map(pbBurstMapper::toDto);
    }

    /**
     * Delete the "id" pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param sampleId   ID of the sample to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param id         the id of the entity.
     */
    @Override
    public void delete(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Long id
    ) {
        log.debug("Request to delete pause-burst burst {} of project {} of sample {} of protocol {} of analysis {}", id, projectId, sampleId, protocolId, analysisId);
        pbBurstRepository.deleteById(id);
    }
}
