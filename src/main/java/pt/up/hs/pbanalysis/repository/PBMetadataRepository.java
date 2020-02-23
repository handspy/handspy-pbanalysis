package pt.up.hs.pbanalysis.repository;

import pt.up.hs.pbanalysis.domain.PBMetadata;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PBMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PBMetadataRepository extends JpaRepository<PBMetadata, Long>, JpaSpecificationExecutor<PBMetadata> {

}
