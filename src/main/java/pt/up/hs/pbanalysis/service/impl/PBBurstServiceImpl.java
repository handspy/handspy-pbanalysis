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

    private final PBBurstRepository pBBurstRepository;

    private final PBBurstMapper pBBurstMapper;

    public PBBurstServiceImpl(PBBurstRepository pBBurstRepository, PBBurstMapper pBBurstMapper) {
        this.pBBurstRepository = pBBurstRepository;
        this.pBBurstMapper = pBBurstMapper;
    }

    /**
     * Save a pBBurst.
     *
     * @param pBBurstDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBBurstDTO save(PBBurstDTO pBBurstDTO) {
        log.debug("Request to save PBBurst : {}", pBBurstDTO);
        PBBurst pBBurst = pBBurstMapper.toEntity(pBBurstDTO);
        pBBurst = pBBurstRepository.save(pBBurst);
        return pBBurstMapper.toDto(pBBurst);
    }

    /**
     * Get all the pBBursts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PBBurstDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PBBursts");
        return pBBurstRepository.findAll(pageable)
            .map(pBBurstMapper::toDto);
    }

    /**
     * Get one pBBurst by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PBBurstDTO> findOne(Long id) {
        log.debug("Request to get PBBurst : {}", id);
        return pBBurstRepository.findById(id)
            .map(pBBurstMapper::toDto);
    }

    /**
     * Delete the pBBurst by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PBBurst : {}", id);
        pBBurstRepository.deleteById(id);
    }
}
