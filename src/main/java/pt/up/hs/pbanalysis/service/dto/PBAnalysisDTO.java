package pt.up.hs.pbanalysis.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.pbanalysis.domain.PBAnalysis} entity.
 */
@ApiModel(description = "Analysis of pauses and busts in handwritten data.\n\n@author José Carlos Paiva")
public class PBAnalysisDTO implements Serializable {

    private Long id;

    /**
     * Sample to which the pause-burst analysis belongs
     */
    @ApiModelProperty(value = "Sample to which the pause-burst analysis belongs")
    private Long sample;

    /**
     * Threshold used to identify bursts
     */
    @ApiModelProperty(value = "Threshold used to identify bursts")
    private Long threshold;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSample() {
        return sample;
    }

    public void setSample(Long sample) {
        this.sample = sample;
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PBAnalysisDTO pBAnalysisDTO = (PBAnalysisDTO) o;
        if (pBAnalysisDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pBAnalysisDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PBAnalysisDTO{" +
            "id=" + getId() +
            ", sample=" + getSample() +
            ", threshold=" + getThreshold() +
            "}";
    }
}
