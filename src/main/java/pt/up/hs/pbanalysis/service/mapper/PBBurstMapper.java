package pt.up.hs.pbanalysis.service.mapper;


import pt.up.hs.pbanalysis.domain.*;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PBBurst} and its DTO {@link PBBurstDTO}.
 */
@Mapper(componentModel = "spring", uses = {PBAnalysisMapper.class})
public interface PBBurstMapper extends EntityMapper<PBBurstDTO, PBBurst> {

    @Mapping(source = "analysis.id", target = "analysisId")
    PBBurstDTO toDto(PBBurst pBBurst);

    @Mapping(source = "analysisId", target = "analysis")
    PBBurst toEntity(PBBurstDTO pBBurstDTO);

    default PBBurst fromId(Long id) {
        if (id == null) {
            return null;
        }
        PBBurst pBBurst = new PBBurst();
        pBBurst.setId(id);
        return pBBurst;
    }
}
