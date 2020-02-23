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
     * Save a pBBurst.
     *
     * @param pBBurstDTO the entity to save.
     * @return the persisted entity.
     */
    PBBurstDTO save(PBBurstDTO pBBurstDTO);

    /**
     * Get all the pBBursts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PBBurstDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pBBurst.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PBBurstDTO> findOne(Long id);

    /**
     * Delete the "id" pBBurst.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
