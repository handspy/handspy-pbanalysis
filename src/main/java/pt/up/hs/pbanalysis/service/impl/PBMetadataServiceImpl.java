package pt.up.hs.pbanalysis.service.impl;

import pt.up.hs.pbanalysis.service.PBMetadataService;
import pt.up.hs.pbanalysis.domain.PBMetadata;
import pt.up.hs.pbanalysis.repository.PBMetadataRepository;
import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;
import pt.up.hs.pbanalysis.service.mapper.PBMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PBMetadata}.
 */
@Service
@Transactional
public class PBMetadataServiceImpl implements PBMetadataService {

    private final Logger log = LoggerFactory.getLogger(PBMetadataServiceImpl.class);

    private final PBMetadataRepository pBMetadataRepository;

    private final PBMetadataMapper pBMetadataMapper;

    public PBMetadataServiceImpl(PBMetadataRepository pBMetadataRepository, PBMetadataMapper pBMetadataMapper) {
        this.pBMetadataRepository = pBMetadataRepository;
        this.pBMetadataMapper = pBMetadataMapper;
    }

    /**
     * Save a pBMetadata.
     *
     * @param pBMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBMetadataDTO save(PBMetadataDTO pBMetadataDTO) {
        log.debug("Request to save PBMetadata : {}", pBMetadataDTO);
        PBMetadata pBMetadata = pBMetadataMapper.toEntity(pBMetadataDTO);
        pBMetadata = pBMetadataRepository.save(pBMetadata);
        return pBMetadataMapper.toDto(pBMetadata);
    }

    /**
     * Get all the pBMetadata.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PBMetadataDTO> findAll() {
        log.debug("Request to get all PBMetadata");
        return pBMetadataRepository.findAll().stream()
            .map(pBMetadataMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pBMetadata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PBMetadataDTO> findOne(Long id) {
        log.debug("Request to get PBMetadata : {}", id);
        return pBMetadataRepository.findById(id)
            .map(pBMetadataMapper::toDto);
    }

    /**
     * Delete the pBMetadata by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PBMetadata : {}", id);
        pBMetadataRepository.deleteById(id);
    }
}
