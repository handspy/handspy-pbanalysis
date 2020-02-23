package pt.up.hs.pbanalysis.service.mapper;


import pt.up.hs.pbanalysis.domain.*;
import pt.up.hs.pbanalysis.service.dto.PBMetadataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PBMetadata} and its DTO {@link PBMetadataDTO}.
 */
@Mapper(componentModel = "spring", uses = {PBAnalysisMapper.class})
public interface PBMetadataMapper extends EntityMapper<PBMetadataDTO, PBMetadata> {

    @Mapping(source = "analysis.id", target = "analysisId")
    PBMetadataDTO toDto(PBMetadata pBMetadata);

    @Mapping(source = "analysisId", target = "analysis")
    PBMetadata toEntity(PBMetadataDTO pBMetadataDTO);

    default PBMetadata fromId(Long id) {
        if (id == null) {
            return null;
        }
        PBMetadata pBMetadata = new PBMetadata();
        pBMetadata.setId(id);
        return pBMetadata;
    }
}
