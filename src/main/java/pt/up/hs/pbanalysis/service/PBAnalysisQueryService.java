package pt.up.hs.pbanalysis.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.domain.*; // for static metamodels
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisCriteria;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;

/**
 * Service for executing complex queries for {@link PBAnalysis} entities in the database.
 * The main input is a {@link PBAnalysisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PBAnalysisDTO} or a {@link Page} of {@link PBAnalysisDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PBAnalysisQueryService extends QueryService<PBAnalysis> {

    private final Logger log = LoggerFactory.getLogger(PBAnalysisQueryService.class);

    private final PBAnalysisRepository pBAnalysisRepository;

    private final PBAnalysisMapper pBAnalysisMapper;

    public PBAnalysisQueryService(PBAnalysisRepository pBAnalysisRepository, PBAnalysisMapper pBAnalysisMapper) {
        this.pBAnalysisRepository = pBAnalysisRepository;
        this.pBAnalysisMapper = pBAnalysisMapper;
    }

    /**
     * Return a {@link List} of {@link PBAnalysisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PBAnalysisDTO> findByCriteria(PBAnalysisCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PBAnalysis> specification = createSpecification(criteria);
        return pBAnalysisMapper.toDto(pBAnalysisRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PBAnalysisDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PBAnalysisDTO> findByCriteria(PBAnalysisCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PBAnalysis> specification = createSpecification(criteria);
        return pBAnalysisRepository.findAll(specification, page)
            .map(pBAnalysisMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PBAnalysisCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PBAnalysis> specification = createSpecification(criteria);
        return pBAnalysisRepository.count(specification);
    }

    /**
     * Function to convert {@link PBAnalysisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PBAnalysis> createSpecification(PBAnalysisCriteria criteria) {
        Specification<PBAnalysis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PBAnalysis_.id));
            }
            if (criteria.getSample() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSample(), PBAnalysis_.sample));
            }
            if (criteria.getThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getThreshold(), PBAnalysis_.threshold));
            }
        }
        return specification;
    }
}
