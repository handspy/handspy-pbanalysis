package pt.up.hs.pbanalysis.service.mapper;


import pt.up.hs.pbanalysis.domain.*;
import pt.up.hs.pbanalysis.service.dto.PBAnalysisDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PBAnalysis} and its DTO {@link PBAnalysisDTO}.
 */
@Mapper(componentModel = "spring", uses = {PBBurstMapper.class})
public interface PBAnalysisMapper extends EntityMapper<PBAnalysisDTO, PBAnalysis> {

    @Mapping(source = "bursts", target = "bursts")
    @Mapping(target = "removeBursts", ignore = true)
    PBAnalysis toEntity(PBAnalysisDTO pBAnalysisDTO);

    @AfterMapping
    default void setBurstAnalysis(@MappingTarget PBAnalysis pbAnalysis) {
        for (PBBurst pbBurst : pbAnalysis.getBursts()) {
            pbBurst.setAnalysis(pbAnalysis);
        }
    }

    default PBAnalysis fromId(Long id) {
        if (id == null) {
            return null;
        }
        PBAnalysis pBAnalysis = new PBAnalysis();
        pBAnalysis.setId(id);
        return pBAnalysis;
    }
}
