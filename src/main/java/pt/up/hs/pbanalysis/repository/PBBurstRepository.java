package pt.up.hs.pbanalysis.repository;

import pt.up.hs.pbanalysis.domain.PBBurst;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PBBurst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PBBurstRepository extends JpaRepository<PBBurst, Long>, JpaSpecificationExecutor<PBBurst> {

}
