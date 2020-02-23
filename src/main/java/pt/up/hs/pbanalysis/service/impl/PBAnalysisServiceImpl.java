package pt.up.hs.pbanalysis.service.impl;

import pt.up.hs.pbanalysis.service.PBAnalysisService;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PBAnalysis}.
 */
@Service
@Transactional
public class PBAnalysisServiceImpl implements PBAnalysisService {

    private final Logger log = LoggerFactory.getLogger(PBAnalysisServiceImpl.class);

    private final PBAnalysisRepository pBAnalysisRepository;

    private final PBAnalysisMapper pBAnalysisMapper;

    public PBAnalysisServiceImpl(PBAnalysisRepository pBAnalysisRepository, PBAnalysisMapper pBAnalysisMapper) {
        this.pBAnalysisRepository = pBAnalysisRepository;
        this.pBAnalysisMapper = pBAnalysisMapper;
    }

    /**
     * Save a pBAnalysis.
     *
     * @param pBAnalysisDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PBAnalysisDTO save(PBAnalysisDTO pBAnalysisDTO) {
        log.debug("Request to save PBAnalysis : {}", pBAnalysisDTO);
        PBAnalysis pBAnalysis = pBAnalysisMapper.toEntity(pBAnalysisDTO);
        pBAnalysis = pBAnalysisRepository.save(pBAnalysis);
        return pBAnalysisMapper.toDto(pBAnalysis);
    }

    /**
     * Get all the pBAnalyses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PBAnalysisDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PBAnalyses");
        return pBAnalysisRepository.findAll(pageable)
            .map(pBAnalysisMapper::toDto);
    }

    /**
     * Get one pBAnalysis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PBAnalysisDTO> findOne(Long id) {
        log.debug("Request to get PBAnalysis : {}", id);
        return pBAnalysisRepository.findById(id)
            .map(pBAnalysisMapper::toDto);
    }

    /**
     * Delete the pBAnalysis by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PBAnalysis : {}", id);
        pBAnalysisRepository.deleteById(id);
    }
}
