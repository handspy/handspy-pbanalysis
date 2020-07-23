package pt.up.hs.pbanalysis.repository;

import pt.up.hs.pbanalysis.domain.PBAnalysis;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the PBAnalysis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PBAnalysisRepository extends JpaRepository<PBAnalysis, Long>, JpaSpecificationExecutor<PBAnalysis> {

    List<PBAnalysis> findByProjectIdAndProtocolId(@NotNull Long projectId, @NotNull Long protocolId);

    Optional<PBAnalysis> findFirstByProjectIdAndProtocolIdOrderByLastModifiedDateDesc(
        @NotNull Long projectId,
        @NotNull Long protocolId
    );

    Optional<PBAnalysis> findByProjectIdAndProtocolIdAndId(
        @NotNull Long projectId,
        @NotNull Long protocolId,
        @NotNull Long id
    );

    @Modifying()
    void deleteByProjectIdAndProtocolId(@NotNull Long projectId, @NotNull Long protocolId);

    @Modifying()
    void deleteByProjectIdAndProtocolIdAndId(
        @NotNull Long projectId,
        @NotNull Long protocolId,
        @NotNull Long id
    );

}
