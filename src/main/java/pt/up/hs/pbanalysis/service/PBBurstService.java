package pt.up.hs.pbanalysis.service;

import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.pbanalysis.domain.PBBurst}.
 */
public interface PBBurstService {

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
    PBBurstDTO save(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        PBBurstDTO pbBurstDTO
    );

    /**
     * Get all the pause-burst burst.
     *
     * @param projectId  ID of the project to which the bursts belong.
     * @param sampleId   ID of the sample to which the bursts belong.
     * @param protocolId ID of the protocol to which the bursts belong.
     * @param analysisId ID of the analysis to which the bursts belong.
     * @param pageable   the pagination information.
     * @return the list of entities.
     */
    Page<PBBurstDTO> findAll(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Pageable pageable
    );

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
    Optional<PBBurstDTO> findOne(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Long id
    );

    /**
     * Delete the "id" pause-burst burst.
     *
     * @param projectId  ID of the project to which the burst belongs.
     * @param sampleId   ID of the sample to which the burst belongs.
     * @param protocolId ID of the protocol to which the burst belongs.
     * @param analysisId ID of the analysis to which the burst belongs.
     * @param id         the id of the entity.
     */
    void delete(
        Long projectId, Long sampleId, Long protocolId, Long analysisId,
        Long id
    );
}
