package pt.up.hs.pbanalysis.service;

import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.pbanalysis.domain.PBAnalysis;
import pt.up.hs.pbanalysis.domain.PBAnalysis_;
import pt.up.hs.pbanalysis.domain.PBBurst_;
import pt.up.hs.pbanalysis.repository.PBAnalysisRepository;
import pt.up.hs.pbanalysis.service.dto.LengthUnit;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisCriteria;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;
import pt.up.hs.pbanalysis.service.dto.TimeUnit;
import pt.up.hs.pbanalysis.service.mapper.PBAnalysisMapper;

import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.stream.Collectors;

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

    private final PBAnalysisRepository pbAnalysisRepository;
    private final PBAnalysisMapper pbAnalysisMapper;

    public PBAnalysisQueryService(PBAnalysisRepository pbAnalysisRepository, PBAnalysisMapper pbAnalysisMapper) {
        this.pbAnalysisRepository = pbAnalysisRepository;
        this.pbAnalysisMapper = pbAnalysisMapper;
    }

    /**
     * Return a {@link List} of {@link PBAnalysisDTO} which matches the criteria from the database.
     *
     * @param projectId  ID of the project of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria   The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PBAnalysisDTO> findByCriteria(
        Long projectId, Long protocolId,
        TimeUnit timeUnit, LengthUnit lengthUnit,
        PBAnalysisCriteria criteria
    ) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PBAnalysis> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId))
            .and(equalsSpecification(root -> root.get("protocolId"), protocolId));
        return pbAnalysisMapper.toDto(pbAnalysisRepository.findAll(specification))
            .parallelStream()
            .peek(pbAnalysis ->
                pbAnalysis.setBursts(pbAnalysis.getBursts().parallelStream().peek(pbBurst -> {
                    pbBurst.setStartTime(pbBurst.getStartTime() * timeUnit.getRate());
                    pbBurst.setEndTime(pbBurst.getEndTime() * timeUnit.getRate());
                    pbBurst.setPauseDuration(pbBurst.getPauseDuration() * timeUnit.getRate());
                    pbBurst.setDistance(pbBurst.getDistance() * lengthUnit.getRate());
                }).collect(Collectors.toList()))
            )
            .collect(Collectors.toList());
    }

    /**
     * Return a {@link Page} of {@link PBAnalysisDTO} which matches the criteria from the database.
     *
     * @param projectId  ID of the project of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria   The object which holds all the filters, which the entities should match.
     * @param page       The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PBAnalysisDTO> findByCriteria(
        Long projectId, Long protocolId,
        TimeUnit timeUnit, LengthUnit lengthUnit,
        PBAnalysisCriteria criteria, Pageable page
    ) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PBAnalysis> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId))
            .and(equalsSpecification(root -> root.get("protocolId"), protocolId));
        return pbAnalysisRepository.findAll(specification, page)
            .map(pbAnalysisMapper::toDto)
            .map(pbAnalysis -> {
                pbAnalysis.setBursts(pbAnalysis.getBursts().parallelStream().peek(pbBurst -> {
                    pbBurst.setStartTime(pbBurst.getStartTime() * timeUnit.getRate());
                    pbBurst.setEndTime(pbBurst.getEndTime() * timeUnit.getRate());
                    pbBurst.setPauseDuration(pbBurst.getPauseDuration() * timeUnit.getRate());
                    pbBurst.setDistance(pbBurst.getDistance() * lengthUnit.getRate());
                }).collect(Collectors.toList()));
                return pbAnalysis;
            });
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId  ID of the project of the analyses.
     * @param protocolId ID of the protocol of the analyses.
     * @param criteria   The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(
        Long projectId, Long protocolId,
        PBAnalysisCriteria criteria
    ) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PBAnalysis> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId))
            .and(equalsSpecification(root -> root.get("protocolId"), protocolId));
        return pbAnalysisRepository.count(specification);
    }

    /**
     * Function to convert {@link PBAnalysisCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PBAnalysis> createSpecification(PBAnalysisCriteria criteria) {
        Specification<PBAnalysis> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PBAnalysis_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PBAnalysis_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PBAnalysis_.description));
            }
            if (criteria.getThreshold() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getThreshold(), PBAnalysis_.threshold));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), PBAnalysis_.createdDate));
            }
            if (criteria.getBurstsId() != null) {
                specification = specification.and(buildSpecification(criteria.getBurstsId(),
                    root -> root.join(PBAnalysis_.bursts, JoinType.LEFT).get(PBBurst_.id)));
            }
        }
        return specification;
    }
}
