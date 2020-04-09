package pt.up.hs.pbanalysis.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Qualifier;
import pt.up.hs.pbanalysis.service.dto.PBBurstDTO;
import pt.up.hs.pbb.models.Burst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Mapper for the entity {@link PBBurstDTO} and the pause-burst-builder
 * library's {@link Burst}.
 */
@Mapper(componentModel = "spring")
public interface BurstMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "analysisId", ignore = true)
    @Mapping(target = "text", ignore = true)
    @Mapping(source = "extraFeatures", target = "pressure",  qualifiedBy = Pressure.class)
    @Mapping(source = "captureCount", target = "dotCount")
    PBBurstDTO burstToPBBurstDto(Burst burst);

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Pressure {
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Extra {
    }

    @Pressure
    default Double pressure(Map<String, Object> in){
        return (Double) in.get("pressure");
    }
}
