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

import pt.up.hs.pbanalysis.domain.PBBurst;
import pt.up.hs.pbanalysis.domain.*; // for static metamodels
import pt.up.hs.pbanalysis.repository.PBBurstRepository;
import pt.up.hs.pbanalysis.service.dto.PBBurstCriteria;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbanalysis.service.mapper.PBBurstMapper;

/**
 * Service for executing complex queries for {@link PBBurst} entities in the database.
 * The main input is a {@link PBBurstCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PBBurstDTO} or a {@link Page} of {@link PBBurstDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PBBurstQueryService extends QueryService<PBBurst> {

    private final Logger log = LoggerFactory.getLogger(PBBurstQueryService.class);

    private final PBBurstRepository pBBurstRepository;

    private final PBBurstMapper pBBurstMapper;

    public PBBurstQueryService(PBBurstRepository pBBurstRepository, PBBurstMapper pBBurstMapper) {
        this.pBBurstRepository = pBBurstRepository;
        this.pBBurstMapper = pBBurstMapper;
    }

    /**
     * Return a {@link List} of {@link PBBurstDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PBBurstDTO> findByCriteria(PBBurstCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PBBurst> specification = createSpecification(criteria);
        return pBBurstMapper.toDto(pBBurstRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PBBurstDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PBBurstDTO> findByCriteria(PBBurstCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PBBurst> specification = createSpecification(criteria);
        return pBBurstRepository.findAll(specification, page)
            .map(pBBurstMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PBBurstCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PBBurst> specification = createSpecification(criteria);
        return pBBurstRepository.count(specification);
    }

    /**
     * Function to convert {@link PBBurstCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PBBurst> createSpecification(PBBurstCriteria criteria) {
        Specification<PBBurst> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PBBurst_.id));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), PBBurst_.text));
            }
            if (criteria.getPauseDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPauseDuration(), PBBurst_.pauseDuration));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), PBBurst_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), PBBurst_.endTime));
            }
            if (criteria.getStartX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartX(), PBBurst_.startX));
            }
            if (criteria.getStartY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartY(), PBBurst_.startY));
            }
            if (criteria.getEndX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndX(), PBBurst_.endX));
            }
            if (criteria.getEndY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndY(), PBBurst_.endY));
            }
            if (criteria.getDistance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDistance(), PBBurst_.distance));
            }
            if (criteria.getDotCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDotCount(), PBBurst_.dotCount));
            }
            if (criteria.getAnalysisId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisId(),
                    root -> root.join(PBBurst_.analysis, JoinType.LEFT).get(PBAnalysis_.id)));
            }
        }
        return specification;
    }
}
