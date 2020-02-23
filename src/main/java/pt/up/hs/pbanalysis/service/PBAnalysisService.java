package pt.up.hs.pbanalysis.service;

import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.pbanalysis.domain.PBAnalysis}.
 */
public interface PBAnalysisService {

    /**
     * Save a pBAnalysis.
     *
     * @param pBAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    PBAnalysisDTO save(PBAnalysisDTO pBAnalysisDTO);

    /**
     * Get all the pBAnalyses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PBAnalysisDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pBAnalysis.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PBAnalysisDTO> findOne(Long id);

    /**
     * Delete the "id" pBAnalysis.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
