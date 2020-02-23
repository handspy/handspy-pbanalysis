package pt.up.hs.pbanalysis.service;

import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.pbanalysis.domain.PBMetadata}.
 */
public interface PBMetadataService {

    /**
     * Save a pBMetadata.
     *
     * @param pBMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    PBMetadataDTO save(PBMetadataDTO pBMetadataDTO);

    /**
     * Get all the pBMetadata.
     *
     * @return the list of entities.
     */
    List<PBMetadataDTO> findAll();

    /**
     * Get the "id" pBMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PBMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" pBMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
