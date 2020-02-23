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

import pt.up.hs.pbanalysis.domain.PBMetadata;
import pt.up.hs.pbanalysis.domain.*; // for static metamodels
import pt.up.hs.pbanalysis.repository.PBMetadataRepository;
import pt.up.hs.pbanalysis.service.dto.PBMetadataCriteria;
import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;
import pt.up.hs.pbanalysis.service.mapper.PBMetadataMapper;

/**
 * Service for executing complex queries for {@link PBMetadata} entities in the database.
 * The main input is a {@link PBMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PBMetadataDTO} or a {@link Page} of {@link PBMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PBMetadataQueryService extends QueryService<PBMetadata> {

    private final Logger log = LoggerFactory.getLogger(PBMetadataQueryService.class);

    private final PBMetadataRepository pBMetadataRepository;

    private final PBMetadataMapper pBMetadataMapper;

    public PBMetadataQueryService(PBMetadataRepository pBMetadataRepository, PBMetadataMapper pBMetadataMapper) {
        this.pBMetadataRepository = pBMetadataRepository;
        this.pBMetadataMapper = pBMetadataMapper;
    }

    /**
     * Return a {@link List} of {@link PBMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PBMetadataDTO> findByCriteria(PBMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PBMetadata> specification = createSpecification(criteria);
        return pBMetadataMapper.toDto(pBMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PBMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PBMetadataDTO> findByCriteria(PBMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PBMetadata> specification = createSpecification(criteria);
        return pBMetadataRepository.findAll(specification, page)
            .map(pBMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PBMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PBMetadata> specification = createSpecification(criteria);
        return pBMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link PBMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PBMetadata> createSpecification(PBMetadataCriteria criteria) {
        Specification<PBMetadata> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PBMetadata_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), PBMetadata_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), PBMetadata_.value));
            }
            if (criteria.getAnalysisId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnalysisId(),
                    root -> root.join(PBMetadata_.analysis, JoinType.LEFT).get(PBAnalysis_.id)));
            }
        }
        return specification;
    }
}
