package pt.up.hs.pbanalysis.service;

import org.springframework.data.domain.Pageable;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.pbanalysis.domain.PBAnalysis}.
 */
public interface PBAnalysisService {

    /**
     * Perform pause-burst analysis and save its result merged with
     * provided {@link PBAnalysisDTO} entity.
     *
     * @param projectId     ID of the project to which the analysis belongs.
     * @param protocolId    ID of the protocol to which the analysis belongs.
     * @param pbAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    PBAnalysisDTO analyze(
        Long projectId, Long protocolId,
        PBAnalysisDTO pbAnalysisDTO
    );

    /**
     * Save a Pause-Burst Analysis.
     *
     * @param projectId     ID of the project to which the analysis belongs.
     * @param protocolId    ID of the protocol to which the analysis belongs.
     * @param pbAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    PBAnalysisDTO save(
        Long projectId, Long protocolId,
        PBAnalysisDTO pbAnalysisDTO
    );

    /**
     * Get all the Pause-Burst Analysis.
     *
     * @param projectId  ID of the project to which the analyses belong.
     * @param protocolId ID of the protocol to which the analyses belong.
     * @param pageable   the pagination information.
     * @return the list of entities.
     */
    List<PBAnalysisDTO> findAll(
        Long projectId, Long protocolId,
        TimeUnit timeUnit, LengthUnit lengthUnit,
        Pageable pageable
    );

    /**
     * Get the "id" Pause-Burst Analysis.
     *
     * @param projectId  ID of the project to which the analysis belongs.
     * @param protocolId ID of the protocol to which the analysis belongs.
     * @param id         the id of the entity.
     * @return the entity.
     */
    Optional<PBAnalysisDTO> findOne(
        Long projectId, Long protocolId, Long id,
        TimeUnit timeUnit, LengthUnit lengthUnit
    );

    /**
     * Delete the "id" Pause-Burst Analysis.
     *
     * @param projectId  ID of the project to which the analysis belongs.
     * @param protocolId ID of the protocol to which the analysis belongs.
     * @param id         the id of the entity.
     */
    void delete(
        Long projectId, Long protocolId, Long id
    );
}
