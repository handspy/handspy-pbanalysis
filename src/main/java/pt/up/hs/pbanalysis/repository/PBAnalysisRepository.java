package pt.up.hs.pbanalysis.repository;

import pt.up.hs.pbanalysis.domain.PBAnalysis;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PBAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PBAnalysisRepository extends JpaRepository<PBAnalysis, Long>, JpaSpecificationExecutor<PBAnalysis> {

}
